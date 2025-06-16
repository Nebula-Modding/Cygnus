package top.girlkisser.cygnus.client.starmap;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;

public record StarmapStarConfig(
	ResourceLocation texture,
	float size
)
{
	public static final Codec<StarmapStarConfig> CODEC = RecordCodecBuilder.create(it -> it.group(
		ResourceLocation.CODEC.fieldOf("texture").forGetter(StarmapStarConfig::texture),
		Codec.FLOAT.fieldOf("size").forGetter(StarmapStarConfig::size)
	).apply(it, StarmapStarConfig::new));

	public static final StreamCodec<ByteBuf, StarmapStarConfig> STREAM_CODEC = ByteBufCodecs.fromCodec(CODEC);
}
