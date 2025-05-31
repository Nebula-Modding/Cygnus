package top.girlkisser.cygnus.foundation.mathematics;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.util.ExtraCodecs;
import org.joml.Vector2f;
import top.girlkisser.cygnus.foundation.MiscCygnusCodecs;

import java.util.ArrayList;
import java.util.List;

public record CubicBezier(Vector2f start, Vector2f control1, Vector2f control2, Vector2f end)
{
	public static final Codec<CubicBezier> CODEC = RecordCodecBuilder.create(it -> it.group(
		MiscCygnusCodecs.VECTOR2F.fieldOf("start").forGetter(CubicBezier::start),
		MiscCygnusCodecs.VECTOR2F.fieldOf("control1").forGetter(CubicBezier::control1),
		MiscCygnusCodecs.VECTOR2F.fieldOf("control2").forGetter(CubicBezier::control2),
		MiscCygnusCodecs.VECTOR2F.fieldOf("end").forGetter(CubicBezier::end)
	).apply(it, CubicBezier::new));

	public static final StreamCodec<ByteBuf, CubicBezier> STREAM_CODEC = ByteBufCodecs.fromCodec(CODEC);

	public static final Codec<CubicBezier> LIST_CODEC = MiscCygnusCodecs.VECTOR2F.listOf(4, 4).xmap(
		(List<Vector2f> value) -> new CubicBezier(value.getFirst(), value.get(1), value.get(2), value.get(3)),
		(CubicBezier value) -> {
			List<Vector2f> points = new ArrayList<>();
			points.add(value.start);
			points.add(value.control1);
			points.add(value.control2);
			points.add(value.end);
			return points;
		}
	);

	public static final StreamCodec<ByteBuf, CubicBezier> LIST_STREAM_CODEC = ByteBufCodecs.fromCodec(LIST_CODEC);

	public CubicBezier(Vector2f control1, Vector2f control2)
	{
		this(new Vector2f(0, 0), control1, control2, new Vector2f(1, 1));
	}

	public CubicBezier(float control1X, float control1Y, float control2X, float control2Y)
	{
		this(new Vector2f(0, 0), new Vector2f(control1X, control1Y), new Vector2f(control2X, control2Y), new Vector2f(1, 1));
	}

	public CubicBezier(float startX, float startY, float control1X, float control1Y, float control2X, float control2Y, float endX, float endY)
	{
		this(new Vector2f(startX, startY), new Vector2f(control1X, control1Y), new Vector2f(control2X, control2Y), new Vector2f(endX, endY));
	}

	public Vector2f sample(float t)
	{
		float m = 1 - t;
		float x = m * m * m * start.x + 3 * m * m * t * control1.x + 3 * m * t * t * control2.x + t * t * t * end.x;
		float y = m * m * m * start.y + 3 * m * m * t * control1.y + 3 * m * t * t * control2.y + t * t * t * end.y;
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
