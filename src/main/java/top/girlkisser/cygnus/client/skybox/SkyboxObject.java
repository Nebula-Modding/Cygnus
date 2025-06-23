package top.girlkisser.cygnus.client.skybox;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.ExtraCodecs;
import org.joml.Vector3f;
import top.girlkisser.lazuli.api.colour.UnpackedColour;

import java.util.List;
import java.util.Optional;

public record SkyboxObject(
	List<SkyboxObject.Layer> layers,
	float scale,
	float distance,
	List<SkyboxTransform> transforms,
	Vector3f textureRotation,
	Optional<UnpackedColour> sunsetColor,
	Optional<SkyboxObject.Backlight> backlight,
	boolean isForOrbit
)
{
	public static final Codec<SkyboxObject> CODEC = RecordCodecBuilder.create(it -> it.group(
		SkyboxObject.Layer.CODEC.listOf().fieldOf("layers").forGetter(SkyboxObject::layers),
		Codec.FLOAT.optionalFieldOf("scale", 1f).forGetter(SkyboxObject::scale),
		Codec.FLOAT.optionalFieldOf("distance", 100f).forGetter(SkyboxObject::distance),
		SkyboxTransform.CODEC.listOf().optionalFieldOf("transforms", List.of()).forGetter(SkyboxObject::transforms),
		ExtraCodecs.VECTOR3F.optionalFieldOf("texture_rotation", new Vector3f(0, 0, 0)).forGetter(SkyboxObject::textureRotation),
		UnpackedColour.FLEXIBLE_CODEC.optionalFieldOf("sunset_color").forGetter(SkyboxObject::sunsetColor),
		SkyboxObject.Backlight.CODEC.optionalFieldOf("backlight").forGetter(SkyboxObject::backlight),
		Codec.BOOL.optionalFieldOf("is_for_orbit", false).forGetter(SkyboxObject::isForOrbit)
	).apply(it, SkyboxObject::new));

	public static final StreamCodec<ByteBuf, SkyboxObject> STREAM_CODEC = ByteBufCodecs.fromCodec(CODEC);

	public record Layer(
		ResourceLocation texture,
		float size,
		float distance,
		List<SkyboxTransform> transforms,
		Vector3f textureRotation
	)
	{
		public static final Codec<Layer> CODEC = RecordCodecBuilder.create(it -> it.group(
			ResourceLocation.CODEC.fieldOf("texture").forGetter(Layer::texture),
			Codec.FLOAT.fieldOf("size").forGetter(Layer::size),
			Codec.FLOAT.optionalFieldOf("distance", 0f).forGetter(Layer::distance),
			SkyboxTransform.CODEC.listOf().optionalFieldOf("transforms", List.of()).forGetter(Layer::transforms),
			ExtraCodecs.VECTOR3F.optionalFieldOf("texture_rotation", new Vector3f(0, 0, 0)).forGetter(Layer::textureRotation)
		).apply(it, Layer::new));

		public static final StreamCodec<ByteBuf, Layer> STREAM_CODEC = ByteBufCodecs.fromCodec(CODEC);
	}

	public record Backlight(
		ResourceLocation texture,
		float size,
		float distance,
		List<SkyboxTransform> transforms,
		Vector3f textureRotation,
		UnpackedColour color
	)
	{
		public static final Codec<Backlight> CODEC = RecordCodecBuilder.create(it -> it.group(
			ResourceLocation.CODEC.fieldOf("texture").forGetter(Backlight::texture),
			Codec.FLOAT.fieldOf("size").forGetter(Backlight::size),
			Codec.FLOAT.optionalFieldOf("distance", 0f).forGetter(Backlight::distance),
			SkyboxTransform.CODEC.listOf().optionalFieldOf("transforms", List.of()).forGetter(Backlight::transforms),
			ExtraCodecs.VECTOR3F.optionalFieldOf("texture_rotation", new Vector3f(0, 0, 0)).forGetter(Backlight::textureRotation),
			UnpackedColour.FLEXIBLE_CODEC.fieldOf("color").forGetter(Backlight::color)
		).apply(it, Backlight::new));

		public static final StreamCodec<ByteBuf, Backlight> STREAM_CODEC = ByteBufCodecs.fromCodec(CODEC);
	}
}
