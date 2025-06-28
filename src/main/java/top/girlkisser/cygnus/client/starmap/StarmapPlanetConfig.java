package top.girlkisser.cygnus.client.starmap;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;
import top.girlkisser.cygnus.Cygnus;

public record StarmapPlanetConfig(
	ResourceLocation texture,
	ResourceLocation terminalIconTexture,
	ResourceLocation terminalIconHoverTexture,
	float size,
	float orbitDistance
)
{
	public static final Codec<StarmapPlanetConfig> CODEC = RecordCodecBuilder.create(it -> it.group(
		ResourceLocation.CODEC.fieldOf("texture").forGetter(StarmapPlanetConfig::texture),
		ResourceLocation.CODEC.optionalFieldOf("terminal_icon_texture", Cygnus.id("terminal/planet")).forGetter(StarmapPlanetConfig::terminalIconTexture),
		ResourceLocation.CODEC.optionalFieldOf("terminal_icon_hover_texture", Cygnus.id("terminal/planet_selected")).forGetter(StarmapPlanetConfig::terminalIconHoverTexture),
		Codec.FLOAT.fieldOf("size").forGetter(StarmapPlanetConfig::size),
		Codec.FLOAT.fieldOf("orbit_distance").forGetter(StarmapPlanetConfig::orbitDistance)
	).apply(it, StarmapPlanetConfig::new));

	public static final StreamCodec<ByteBuf, StarmapPlanetConfig> STREAM_CODEC = ByteBufCodecs.fromCodec(CODEC);
}
