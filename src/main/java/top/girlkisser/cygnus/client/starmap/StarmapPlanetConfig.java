package top.girlkisser.cygnus.client.starmap;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;

public record StarmapPlanetConfig(
	ResourceLocation texture,
	float size,
	float orbitDistance
)
{
	public static final Codec<StarmapPlanetConfig> CODEC = RecordCodecBuilder.create(it -> it.group(
		ResourceLocation.CODEC.fieldOf("texture").forGetter(StarmapPlanetConfig::texture),
		Codec.FLOAT.fieldOf("size").forGetter(StarmapPlanetConfig::size),
		Codec.FLOAT.fieldOf("orbit_distance").forGetter(StarmapPlanetConfig::orbitDistance)
	).apply(it, StarmapPlanetConfig::new));

	public static final StreamCodec<ByteBuf, StarmapPlanetConfig> STREAM_CODEC = ByteBufCodecs.fromCodec(CODEC);
}
