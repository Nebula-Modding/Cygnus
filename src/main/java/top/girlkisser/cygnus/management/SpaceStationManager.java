package top.girlkisser.cygnus.management;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.StructureBlockEntity;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
import net.minecraft.world.level.saveddata.SavedData;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import top.girlkisser.cygnus.Cygnus;
import top.girlkisser.cygnus.content.CygnusResourceKeys;
import top.girlkisser.cygnus.content.registry.CygnusBlocks;
import top.girlkisser.cygnus.foundation.space.SpaceStation;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.*;

public final class SpaceStationManager extends SavedData
{
	// Path is at `<world folder>/data/cygnus_space_stations.dat`
	private static final String ID = Cygnus.MODID + "_space_stations";

	private final Map<UUID, SpaceStation> spaceStations = new HashMap<>();
	private BlockPos.MutableBlockPos nextOrigin = new BlockPos.MutableBlockPos(0, 100, 0);

	private SpaceStationManager()
	{
	}

	public SpaceStation getOrCreateSpaceStationForPlayer(ServerPlayer player, ResourceLocation structure)
	{
		return getSpaceStationForPlayer(player).orElseGet(() ->
		{
			createSpaceStationForPlayer(player, structure);
			return getSpaceStationForPlayer(player).orElseThrow();
		});
	}

	public Optional<SpaceStation> getSpaceStationForPlayer(ServerPlayer player)
	{
		return getSpaceStationByOwnerUUID(player.getUUID());
	}

	public Optional<SpaceStation> getSpaceStationByOwnerUUID(UUID uuid)
	{
		if (!spaceStations.containsKey(uuid))
			return Optional.empty();
		return Optional.of(spaceStations.get(uuid));
	}

	public ServerLevel getSpaceLevel(MinecraftServer server)
	{
		return server.getLevel(CygnusResourceKeys.SPACE);
	}

	public void createSpaceStationForPlayer(ServerPlayer player, ResourceLocation structure)
	{
		ServerLevel level = getSpaceLevel(player.server);

		StructureTemplate template = level.getStructureManager().get(structure).orElseThrow();
		BlockPos centre = BlockPos.containing(
			nextOrigin.getX() - (template.getSize().getX() / 2f),
			100 - (template.getSize().getY() / 2f),
			nextOrigin.getZ() - (template.getSize().getZ() / 2f)
		);
		StructurePlaceSettings settings = new StructurePlaceSettings();
		boolean successful = template.placeInWorld(level, centre, centre, settings, StructureBlockEntity.createRandom(level.getSeed()), Block.UPDATE_CLIENTS);

		if (!successful)
		{
			player.sendSystemMessage(Component.translatable("message.cygnus.failed_to_send_space_station"));
			return;
		}

		List<BlockPos> telepadList = template
			.filterBlocks(centre, settings, CygnusBlocks.TELEPAD.get(), true)
			.stream()
			.map(StructureTemplate.StructureBlockInfo::pos)
			.toList();

		String playerName = player.getName().getString();
		String name = playerName + (playerName.charAt(playerName.length() - 1) == 's' ? "'" : "'s") + " Space Station";

		spaceStations.put(player.getUUID(), new SpaceStation(
			player.getUUID(),
			name,
			nextOrigin.immutable(),
			Cygnus.id("earth"),
			structure,
			telepadList
		));

		nextOrigin.move(Direction.NORTH, 50_000);
		setDirty();
	}

	public Optional<SpaceStation> getClosestSpaceStationTo(ServerLevel level, BlockPos pos)
	{
		if (!level.dimension().location().equals(Cygnus.id("space")))
		{
			return Optional.empty();
		}

		return spaceStations
			.values()
			.stream()
			.min(Comparator.comparingInt(a -> a.origin().distManhattan(pos)));
	}

	@ApiStatus.Internal
	@Override
	@ParametersAreNonnullByDefault
	public @NotNull CompoundTag save(CompoundTag tag, HolderLookup.Provider registries)
	{
		CompoundTag spaceStationsTag = new CompoundTag();
		spaceStations.forEach(((uuid, spaceStation) -> spaceStationsTag.put(uuid.toString(), spaceStation.save())));
		tag.put("space_stations", spaceStationsTag);
		tag.put("next_origin", NbtUtils.writeBlockPos(nextOrigin));
		return tag;
	}

	@ApiStatus.Internal
	public static SpaceStationManager load(CompoundTag tag, HolderLookup.Provider ignoredRegistries)
	{
		SpaceStationManager manager = new SpaceStationManager();
		manager.spaceStations.clear();
		CompoundTag spaceStationsTag = tag.getCompound("space_stations");
		spaceStationsTag.getAllKeys().forEach(key ->
		{
			var s = SpaceStation.load(spaceStationsTag.getCompound(key));
			manager.spaceStations.put(s.player(), s);
		});
		manager.nextOrigin = NbtUtils.readBlockPos(tag, "next_origin").orElseThrow().mutable();
		return manager;
	}

	public static SpaceStationManager get(MinecraftServer server)
	{
		return server.overworld().getDataStorage().computeIfAbsent(new Factory<>(SpaceStationManager::new, SpaceStationManager::load), ID);
	}
}
