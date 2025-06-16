package top.girlkisser.cygnus.client.starmap;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;

public record StarmapGalaxyConfig(
	ResourceLocation texture,
	float size
)
{
	public static final Codec<StarmapGalaxyConfig> CODEC = RecordCodecBuilder.create(it -> it.group(
		ResourceLocation.CODEC.fieldOf("texture").forGetter(StarmapGalaxyConfig::texture),
		Codec.FLOAT.fieldOf("size").forGetter(StarmapGalaxyConfig::size)
	).apply(it, StarmapGalaxyConfig::new));

	public static final StreamCodec<ByteBuf, StarmapGalaxyConfig> STREAM_CODEC = ByteBufCodecs.fromCodec(CODEC);
}
