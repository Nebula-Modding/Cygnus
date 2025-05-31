package top.girlkisser.cygnus.foundation.mathematics;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import org.joml.Vector2f;
import top.girlkisser.cygnus.foundation.MiscCygnusCodecs;

import java.util.ArrayList;
import java.util.List;

public record QuadraticBezier(Vector2f start, Vector2f control, Vector2f end)
{
	public static final Codec<QuadraticBezier> CODEC = RecordCodecBuilder.create(it -> it.group(
		MiscCygnusCodecs.VECTOR2F.fieldOf("start").forGetter(QuadraticBezier::start),
		MiscCygnusCodecs.VECTOR2F.fieldOf("control").forGetter(QuadraticBezier::control),
		MiscCygnusCodecs.VECTOR2F.fieldOf("end").forGetter(QuadraticBezier::end)
	).apply(it, QuadraticBezier::new));

	public static final StreamCodec<ByteBuf, QuadraticBezier> STREAM_CODEC = ByteBufCodecs.fromCodec(CODEC);

	public static final Codec<QuadraticBezier> LIST_CODEC = MiscCygnusCodecs.VECTOR2F.listOf(3, 3).xmap(
		(List<Vector2f> value) -> new QuadraticBezier(value.getFirst(), value.get(1), value.get(2)),
		(QuadraticBezier value) -> {
			List<Vector2f> points = new ArrayList<>();
			points.add(value.start);
			points.add(value.control);
			points.add(value.end);
			return points;
		}
	);

	public static final StreamCodec<ByteBuf, QuadraticBezier> LIST_STREAM_CODEC = ByteBufCodecs.fromCodec(LIST_CODEC);

	public QuadraticBezier(Vector2f control1)
	{
		this(new Vector2f(0, 0), control1, new Vector2f(1, 1));
	}

	public QuadraticBezier(float control1X, float control1Y)
	{
		this(new Vector2f(0, 0), new Vector2f(control1X, control1Y), new Vector2f(1, 1));
	}

	public QuadraticBezier(float startX, float startY, float control1X, float control1Y, float endX, float endY)
	{
		this(new Vector2f(startX, startY), new Vector2f(control1X, control1Y), new Vector2f(endX, endY));
	}

	public Vector2f sample(float t)
	{
		float m = 1 - t;
		float x = m * m * start.x + 2 * m * t * control.x + t * t * end.x;
		float y = m * m * start.y + 2 * m * t * control.y + t * t * end.y;
		return new Vector2f(x, y);
	}

	public List<Vector2f> getPoints(float resolution)
	{
		List<Vector2f> points = new ArrayList<>();
		for (float t = resolution; t < 1; t += resolution)
		{
			points.add(sample(t));
		}
		return points;
	}
}
