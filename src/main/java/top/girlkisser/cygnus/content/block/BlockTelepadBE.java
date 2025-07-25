package top.girlkisser.cygnus.content.block;

import net.minecraft.ChatFormatting;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import top.girlkisser.cygnus.api.client.DustParticlePresets;
import top.girlkisser.cygnus.api.space.Planet;
import top.girlkisser.cygnus.api.space.SpaceStation;
import top.girlkisser.cygnus.client.CygnusClient;
import top.girlkisser.cygnus.content.registry.CygnusBlockEntityTypes;
import top.girlkisser.cygnus.management.SpaceStationManager;
import top.girlkisser.lazuli.api.block.ITickableBE;
import top.girlkisser.lazuli.api.client.LazuliClientHelpers;
import top.girlkisser.lazuli.api.client.particle.ParticleHelper;
import top.girlkisser.lazuli.api.world.AABBHelpers;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

public class BlockTelepadBE extends BlockEntity implements ITickableBE
{
	public static final int TICKS_FOR_TELEPORT = 60;

	// Entities are hashed by an integer ID, so a map is the easiest and probably the fastest structure to use for this.
	// I'm using an AtomicInteger for ease-of-usage (i.e, so I don't have to `Map.put(player, Map.get(player) + 1)`)
	private Map<ServerPlayer, AtomicInteger> teleportingPlayers = new HashMap<>();

	private @Nullable UUID spaceStationDestination = null;

	public BlockTelepadBE(BlockPos pos, BlockState blockState)
	{
		super(CygnusBlockEntityTypes.TELEPAD.get(), pos, blockState);
	}

	public void setDestination(@Nullable UUID uuid)
	{
		this.spaceStationDestination = uuid;
		setChanged();
	}

	public AABB getAABB(BlockPos pos)
	{
		return AABBHelpers.ofBlock(pos);
	}

	public void teleportPlayer(ServerLevel level, ServerPlayer player)
	{
		SpaceStationManager manager = SpaceStationManager.get(level.getServer());

		// Teleporting to a planet
		if (spaceStationDestination == null)
		{
			var maybeSpaceStation = manager.getClosestSpaceStationTo(level, worldPosition);
			if (maybeSpaceStation.isEmpty())
				return;
			SpaceStation spaceStation = maybeSpaceStation.get();

			var maybePlanet = Planet.getPlanetById(level.registryAccess(), spaceStation.orbiting());
			if (maybePlanet.isEmpty())
				return;
			Planet planet = maybePlanet.get().value();

			planet.dimension().ifPresentOrElse(
				it -> planet.teleportPlayerHereViaBeam(player, spaceStation.player()),
				() -> player.sendSystemMessage(Component.translatable("message.cygnus.cannot_land_on_planet").withStyle(ChatFormatting.RED))
			);
		}
		// Teleporting to a space station
		else
		{
			manager.getSpaceStationByOwnerUUID(spaceStationDestination)
				.ifPresent(spaceStation -> spaceStation.teleportPlayerHereViaBeam(player, spaceStationDestination));
		}
	}

	@Override
	public void serverTick(@NotNull ServerLevel level)
	{
		Map<ServerPlayer, AtomicInteger> ticks = new HashMap<>();
		level.getEntitiesOfClass(ServerPlayer.class, getAABB(worldPosition), Entity::isCrouching).forEach(player -> {
			ticks.put(player, teleportingPlayers.containsKey(player) ? teleportingPlayers.get(player) : new AtomicInteger(0));

			if (ticks.get(player).incrementAndGet() >= TICKS_FOR_TELEPORT)
			{
				teleportPlayer(level, player);
				ticks.remove(player);
			}
		});
		teleportingPlayers = ticks;
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public void clientTick(@NotNull ClientLevel level)
	{
		if (
			CygnusClient.mySpaceStation == null ||
			Planet.getPlanetById(level.registryAccess(), CygnusClient.mySpaceStation.orbiting())
				.map(planet -> planet.value().dimension().isPresent())
				.orElse(true)
		)
		{
			float x = Mth.sin(LazuliClientHelpers.clientTicks / 2f) * 0.5f;
			float z = Mth.cos(LazuliClientHelpers.clientTicks / 2f) * 0.5f;
			ParticleHelper.addDust(DustParticlePresets.TELEPAD, level, worldPosition.getCenter().add(x, 0.3d, z), new Vec3(0, 0.7D, 0));
		}
	}

	@Override
	@ParametersAreNonnullByDefault
	protected void saveAdditional(CompoundTag tag, HolderLookup.Provider registries)
	{
		super.saveAdditional(tag, registries);
		if (spaceStationDestination != null)
			tag.putUUID("destination", spaceStationDestination);
	}

	@Override
	@ParametersAreNonnullByDefault
	protected void loadAdditional(CompoundTag tag, HolderLookup.Provider registries)
	{
		super.loadAdditional(tag, registries);
		spaceStationDestination = tag.hasUUID("destination") ? tag.getUUID("destination") : null;
	}

	public static void setTelepadDestination(ServerLevel level, BlockPos pos, UUID destination)
	{
		if (level.getBlockEntity(pos) instanceof BlockTelepadBE telepad)
		{
			telepad.spaceStationDestination = destination;
			telepad.setChanged();
		}
	}
}
