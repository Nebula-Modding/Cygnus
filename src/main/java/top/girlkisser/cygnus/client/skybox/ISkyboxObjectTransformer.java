package top.girlkisser.cygnus.client.skybox;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.multiplayer.ClientLevel;

public interface ISkyboxObjectTransformer
{
	void apply(SkyboxObject object, SkyboxTransform transform, PoseStack poses, ClientLevel level, float partialTick);
}
