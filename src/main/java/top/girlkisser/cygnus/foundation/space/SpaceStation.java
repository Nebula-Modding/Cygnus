package top.girlkisser.cygnus.foundation.space;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.core.UUIDUtil;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.portal.DimensionTransition;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;
import top.girlkisser.cygnus.content.CygnusResourceKeys;
import top.girlkisser.cygnus.content.entity.EntityLandingBeam;
import top.girlkisser.cygnus.management.SpaceStationManager;

import java.util.*;

public final class SpaceStation
{
	public static final Codec<SpaceStation> CODEC = RecordCodecBuilder.create(it -> it.group(
		UUIDUtil.CODEC.fieldOf("player").forGetter(SpaceStation::player),
		Codec.STRING.fieldOf("name").forGetter(SpaceStation::name),
		BlockPos.CODEC.fieldOf("origin").forGetter(SpaceStation::origin),
		ResourceLocation.CODEC.fieldOf("orbiting").forGetter(SpaceStation::orbiting),
		ResourceLocation.CODEC.fieldOf("structure").forGetter(SpaceStation::structure),
		BlockPos.CODEC.listOf()
			.xmap(
				list -> list,
				ArrayList::new // We do this so that the list is made mutable
			)
			.fieldOf("telepads")
			.forGetter(SpaceStation::telepads)
	).apply(it, SpaceStation::new));

	public static final StreamCodec<RegistryFriendlyByteBuf, SpaceStation> STREAM_CODEC = ByteBufCodecs.fromCodecWithRegistries(CODEC);

	private UUID player;
	private String name;
	private BlockPos origin;
	private ResourceLocation orbiting;
	private ResourceLocation structure;
	private List<BlockPos> telepads;

	public SpaceStation(
		UUID player,
		String name,
		BlockPos origin,
		ResourceLocation orbiting,
		ResourceLocation structure,
		List<BlockPos> telepads
	)
	{
		this.player = player;
		this.name = name;
		this.origin = origin;
		this.orbiting = orbiting;
		this.structure = structure;
		this.telepads = telepads;
	}

	public void teleportPlayerHereViaBeam(ServerPlayer player, @Nullable UUID originSpaceStation)
	{
		EntityLandingBeam beam = new EntityLandingBeam(player.level(), false, originSpaceStation);
		beam.setPos(player.position());
		player.startRiding(beam, true);
		//noinspection resource
		player.level().addFreshEntity(beam);
	}

	public void teleportEntityHere(Entity entity)
	{
		assert entity.getServer() != null;
		ServerLevel level = entity.getServer().getLevel(CygnusResourceKeys.SPACE);
		assert level != null;
		entity.changeDimension(new DimensionTransition(
			level,
			(telepads.isEmpty() ? origin : telepads.getFirst()).above().getBottomCenter(),
			Vec3.ZERO,
			0f,
			0f,
			DimensionTransition.DO_NOTHING
		));
	}

	public CompoundTag save()
	{
		CompoundTag tag = new CompoundTag();

		tag.putUUID("player", player);
		tag.putString("name", name);
		tag.put("origin", NbtUtils.writeBlockPos(origin));
		tag.putString("orbiting", orbiting.toString());
		tag.putString("structure", structure.toString());

		CompoundTag telepadsTag = new CompoundTag();
		for (int i = 0 ; i < telepads.size() ; i++)
		{
			BlockPos pos = telepads.get(i);
			telepadsTag.put(Integer.toString(i), NbtUtils.writeBlockPos(pos));
		}
		tag.put("telepads", telepadsTag);

		return tag;
	}

	public static SpaceStation load(CompoundTag tag)
	{
		CompoundTag telepadsTag = tag.getCompound("telepads");
		List<BlockPos> telepads = new ArrayList<>();
		for (String key : telepadsTag.getAllKeys())
			telepads.add(NbtUtils.readBlockPos(telepadsTag, key).orElseThrow());

		return new SpaceStation(
			tag.getUUID("player"),
			tag.getString("name"),
			NbtUtils.readBlockPos(tag, "origin").orElseThrow(),
			ResourceLocation.parse(tag.getString("orbiting")),
			ResourceLocation.parse(tag.getString("structure")),
			telepads
		);
	}

	public UUID player()
	{
		return player;
	}

	public String name()
	{
		return name;
	}

	public BlockPos origin()
	{
		return origin;
	}

	public ResourceLocation orbiting()
	{
		return orbiting;
	}

	public ResourceLocation structure()
	{
		return structure;
	}

	public List<BlockPos> telepads()
	{
		return telepads;
	}

	public void setPlayer(MinecraftServer server, UUID value)
	{
		player = value;
		SpaceStationManager.get(server).setDirty();
	}

	public void setName(MinecraftServer server, String value)
	{
		name = value;
		SpaceStationManager.get(server).setDirty();
	}

	public void setOrigin(MinecraftServer server, BlockPos value)
	{
		origin = value;
		SpaceStationManager.get(server).setDirty();
	}

	public void setOrbiting(MinecraftServer server, ResourceLocation value)
	{
		orbiting = value;
		SpaceStationManager.get(server).setDirty();
	}

	public void setStructure(MinecraftServer server, ResourceLocation value)
	{
		structure = value;
		SpaceStationManager.get(server).setDirty();
	}

	public void setTelepads(MinecraftServer server, List<BlockPos> value)
	{
		telepads = value;
		SpaceStationManager.get(server).setDirty();
	}

	@Override
	public boolean equals(Object obj)
	{
		if (obj == this)
			return true;
		if (obj == null || obj.getClass() != this.getClass())
			return false;
		var that = (SpaceStation) obj;
		return Objects.equals(this.player, that.player) &&
			Objects.equals(this.name, that.name) &&
			Objects.equals(this.origin, that.origin) &&
			Objects.equals(this.orbiting, that.orbiting) &&
			Objects.equals(this.structure, that.structure) &&
			Objects.equals(this.telepads, that.telepads);
	}

	@Override
	public int hashCode()
	{
		return Objects.hash(player, name, origin, orbiting, structure, telepads);
	}

	@Override
	public String toString()
	{
		return "SpaceStation[" +
			"player=" + player + ", " +
			"name=" + name + ", " +
			"origin=" + origin + ", " +
			"orbiting=" + orbiting + ", " +
			"structure=" + structure + ", " +
			"telepads=" + telepads + ']';
	}
}
