package top.girlkisser.cygnus.foundation;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import org.jetbrains.annotations.ApiStatus;
import org.joml.Vector2f;

@ApiStatus.NonExtendable
public interface MiscCygnusCodecs
{
	Codec<Vector2f> VECTOR2F = RecordCodecBuilder.create(it -> it.group(
		Codec.FLOAT.fieldOf("x").forGetter(Vector2f::x),
		Codec.FLOAT.fieldOf("y").forGetter(Vector2f::y)
	).apply(it, Vector2f::new));

	StreamCodec<ByteBuf, Vector2f> VECTOR2F_STREAM_CODEC = ByteBufCodecs.fromCodec(VECTOR2F);
}
