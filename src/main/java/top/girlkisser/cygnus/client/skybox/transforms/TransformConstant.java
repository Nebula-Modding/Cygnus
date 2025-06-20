package top.girlkisser.cygnus.client.skybox.transforms;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.client.multiplayer.ClientLevel;
import top.girlkisser.cygnus.client.skybox.ISkyboxObjectTransformer;
import top.girlkisser.cygnus.client.skybox.SkyboxObject;
import top.girlkisser.cygnus.client.skybox.SkyboxTransform;

public record TransformConstant(float value) implements ISkyboxObjectTransformer
{
	public static final Codec<TransformConstant> CODEC = RecordCodecBuilder.create(it -> it.group(
		Codec.FLOAT.fieldOf("value").forGetter(TransformConstant::value)
	).apply(it, TransformConstant::new));

	@Override
	public void apply(SkyboxObject object, SkyboxTransform transform, PoseStack poses, ClientLevel level, float partialTick)
	{
		switch (transform.axis())
		{
			case SkyboxTransform.Axis.X -> poses.mulPose(Axis.XP.rotationDegrees(value));
			case SkyboxTransform.Axis.Y -> poses.mulPose(Axis.YP.rotationDegrees(value));
			case SkyboxTransform.Axis.Z -> poses.mulPose(Axis.ZP.rotationDegrees(value));
		}
	}
}
