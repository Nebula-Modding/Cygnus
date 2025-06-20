package top.girlkisser.cygnus.foundation.space;

import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.portal.DimensionTransition;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;
import top.girlkisser.cygnus.Cygnus;
import top.girlkisser.cygnus.content.entity.EntityLandingBeam;
import top.girlkisser.cygnus.foundation.CygnusRegistries;
import top.girlkisser.cygnus.foundation.mathematics.CubicBezier;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public record Planet(
	Optional<ResourceLocation> dimension,
	ResourceLocation terminalIcon, // TODO: Move to planet render config
	ResourceLocation terminalIconHover, // TODO: Move to planet render config
	ResourceLocation mapTexture, // TODO: Move to planet render config
	double gravity, // Meters per second^2. Earth is 9.807
	boolean hasOxygen,
	int dayLength, // Ticks
	int nightLength, // Ticks
	double yearLength, // In Minecraft days (there are 24000 ticks per day)
	CubicBezier ambientTemperatureCurve, // Celcius
	float ambientRadiation, // Sieverts will probably be best for this
	List<ResourceLocation> moons,
//	List<Pair<ResourceLocation, Float>> gasses,
	float atmosphericPressure // Bars
)
{
	private static final double METERS_PER_SECOND_SQUARED_TO_MINECRAFT_GRAVITY_UNITS = (0.08d / 9.807d);

	public static final Codec<Planet> CODEC = RecordCodecBuilder.create(it -> it.group(
		ResourceLocation.CODEC.optionalFieldOf("dimension").forGetter(Planet::dimension),
		ResourceLocation.CODEC.optionalFieldOf("terminal_icon", Cygnus.id("terminal/planet")).forGetter(Planet::terminalIcon),
		ResourceLocation.CODEC.optionalFieldOf("terminal_icon_hover", Cygnus.id("terminal/planet_selected")).forGetter(Planet::terminalIconHover),
		ResourceLocation.CODEC.optionalFieldOf("map_texture", Cygnus.id("planet")).forGetter(Planet::mapTexture),
		Codec.DOUBLE.fieldOf("gravity").forGetter(Planet::gravity),
		Codec.BOOL.fieldOf("has_oxygen").forGetter(Planet::hasOxygen),
		Codec.INT.fieldOf("day_length").forGetter(Planet::dayLength),
		Codec.INT.fieldOf("night_length").forGetter(Planet::nightLength),
		Codec.DOUBLE.fieldOf("year_length").forGetter(Planet::yearLength),
		CubicBezier.LIST_CODEC.fieldOf("ambient_temperature_curve").forGetter(Planet::ambientTemperatureCurve),
		Codec.FLOAT.fieldOf("ambient_radiation").forGetter(Planet::ambientRadiation),
		ResourceLocation.CODEC.listOf().fieldOf("moons").forGetter(Planet::moons),
//		Codec.simpleMap(ResourceLocation.CODEC, Codec.FLOAT, Keyable.forStrings()).fieldOf("gasses").forGetter()
		Codec.FLOAT.fieldOf("atmospheric_pressure").forGetter(Planet::atmosphericPressure)
	).apply(it, Planet::new));

	public static final StreamCodec<RegistryFriendlyByteBuf, Planet> STREAM_CODEC = ByteBufCodecs.fromCodecWithRegistries(CODEC);

	public boolean isDayAtTick(long tick)
	{
		return tick % (dayLength + nightLength) < dayLength;
	}

	public boolean isNightAtTick(long tick)
	{
		return tick % (dayLength + nightLength) >= dayLength;
	}

	public float getTimeProgress(long tick)
	{
		return (tick % (float)(dayLength + nightLength)) / (float)(dayLength + nightLength);
	}

	public float getDayProgress(long tick)
	{
		if (isNightAtTick(tick))
			return 1;
		return (tick % (float)(dayLength + nightLength)) / (float)dayLength;
	}

	public float getNightProgress(long tick)
	{
		if (isDayAtTick(tick))
			return 1;
		return (tick % (float)(dayLength + nightLength)) / (float)nightLength;
	}

	public float getTemperatureForTick(long tick)
	{
		return ambientTemperatureCurve.sample(getTimeProgress(tick)).y;
	}

	public double getGravityInVanillaUnits()
	{
		return gravity * METERS_PER_SECOND_SQUARED_TO_MINECRAFT_GRAVITY_UNITS;
	}

	public void teleportPlayerHereViaBeam(ServerPlayer player, boolean randomPos, int x, int z, @Nullable UUID originSpaceStation)
	{
		if (this.dimension.isEmpty())
		{
			return;
		}

		assert player.getServer() != null;
		ServerLevel level = player.getServer().getLevel(ResourceKey.create(Registries.DIMENSION, this.dimension.orElseThrow()));
		assert level != null;

		var pos = new BlockPos(x, 300, z).mutable();
		if (randomPos)
		{
			pos.setX(level.random.nextInt(-1000, 1000));
			pos.setZ(level.random.nextInt(-1000, 1000));
		}

		EntityLandingBeam beam = new EntityLandingBeam(level, true, originSpaceStation);
		player.startRiding(beam, true);
		level.addFreshEntity(beam);
		var transition = new DimensionTransition(
			level,
			pos.getBottomCenter(),
			Vec3.ZERO,
			0f,
			0f,
			DimensionTransition.DO_NOTHING
		);
		beam.changeDimension(transition);
	}

	public void teleportPlayerHereViaBeam(ServerPlayer player, @Nullable UUID originSpaceStation)
	{
		teleportPlayerHereViaBeam(player, true, 0, 0, originSpaceStation);
	}

	public ResourceKey<Planet> getResourceKey(RegistryAccess registryAccess)
	{
		return registryAccess.lookupOrThrow(CygnusRegistries.PLANET)
			.listElements()
			.filter(it -> it.value().equals(this))
			.limit(1)
			.findFirst()
			.orElseThrow()
			.key();
	}

	public static Component getName(ResourceLocation id)
	{
		return Component.translatable(id.toLanguageKey("planet"));
	}

	public static List<Planet> getPlanets(RegistryAccess registryAccess)
	{
		return registryAccess.lookupOrThrow(CygnusRegistries.PLANET)
			.listElements()
			.map(Holder.Reference::value)
			.toList();
	}

	public static List<Pair<ResourceKey<Planet>, Planet>> getPlanetsWithIds(RegistryAccess registryAccess)
	{
		return registryAccess.lookupOrThrow(CygnusRegistries.PLANET)
			.listElements()
			.map(it -> new Pair<>(it.key(), it.value()))
			.toList();
	}

	public static Optional<Holder.Reference<Planet>> getPlanetById(RegistryAccess registryAccess, ResourceKey<Planet> id)
	{
		return registryAccess.lookupOrThrow(CygnusRegistries.PLANET).get(id);
	}

	public static Optional<Holder.Reference<Planet>> getPlanetById(RegistryAccess registryAccess, ResourceLocation id)
	{
		return getPlanetById(registryAccess, ResourceKey.create(CygnusRegistries.PLANET, id));
	}

	public static Planet getPlanetByIdOrThrow(RegistryAccess registryAccess, ResourceLocation id)
	{
		return getPlanetById(registryAccess, id).orElseThrow().value();
	}
}
