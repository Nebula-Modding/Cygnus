package top.girlkisser.cygnus.client.skybox;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.datafixers.util.Either;
import com.mojang.serialization.Codec;
import com.mojang.serialization.JsonOps;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.ExtraCodecs;
import top.girlkisser.cygnus.Cygnus;
import top.girlkisser.cygnus.client.skybox.transforms.TransformConstant;
import top.girlkisser.cygnus.client.skybox.transforms.TransformTime;
import top.girlkisser.lazuli.api.codec.LimitedStringCodec;

import java.util.HashMap;
import java.util.Map;

public record SkyboxTransform(
	Axis axis,
	Either<ResourceLocation, Float> value,
	JsonElement args
)
{
	private static final Map<ResourceLocation, Codec<? extends ISkyboxObjectTransformer>> TRANSFORMS = new HashMap<>();

	static
	{
		SkyboxTransform.register(Cygnus.id("constant"), TransformConstant.CODEC);
		SkyboxTransform.register(Cygnus.id("time"), TransformTime.CODEC);
	}

	public static final Codec<SkyboxTransform> CODEC = RecordCodecBuilder.create(it -> it.group(
		LimitedStringCodec.enumStringCodec(Axis.values(), Axis::valueOf).fieldOf("axis").forGetter(SkyboxTransform::axis),
		Codec.either(ResourceLocation.CODEC, Codec.FLOAT).fieldOf("value").forGetter(SkyboxTransform::value),
		ExtraCodecs.JSON.optionalFieldOf("args", new JsonObject()).forGetter(SkyboxTransform::args)
	).apply(it, SkyboxTransform::new));

	public ISkyboxObjectTransformer getTransformer()
	{
		var transformerCodec = TRANSFORMS.get(this.value.left().orElse(Cygnus.id("constant")));
		if (this.value.right().isPresent() && !args.getAsJsonObject().has("value"))
		{
			var newArgs = args.deepCopy();
			newArgs.getAsJsonObject().add("value", new JsonPrimitive(this.value.right().get()));
			return transformerCodec.decode(JsonOps.INSTANCE, newArgs).getOrThrow().getFirst();
		}
		return transformerCodec.decode(JsonOps.INSTANCE, args)
			.getOrThrow((msg) -> new IllegalStateException("Invalid arguments for " + this.value.left().orElseThrow() + ": " + msg))
			.getFirst();
	}

	public void apply(SkyboxObject object, PoseStack poses, ClientLevel level, float partialTick)
	{
		getTransformer().apply(object, this, poses, level, partialTick);
	}

	public static void register(ResourceLocation id, Codec<? extends ISkyboxObjectTransformer> codec)
	{
		TRANSFORMS.put(id, codec);
	}

	public enum Axis
	{
		X, Y, Z
	}
}
