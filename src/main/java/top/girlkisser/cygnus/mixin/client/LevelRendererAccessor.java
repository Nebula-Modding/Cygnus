package top.girlkisser.cygnus.mixin.client;

import com.mojang.blaze3d.vertex.VertexBuffer;
import net.minecraft.client.Camera;
import net.minecraft.client.renderer.LevelRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(LevelRenderer.class)
public interface LevelRendererAccessor
{
	@Invoker(value = "doesMobEffectBlockSky")
	boolean cygnus$doesMobEffectBlockSky(Camera camera);

	@Accessor(value = "cloudBuffer")
	VertexBuffer cygnus$getCloudBuffer();

	@Accessor(value = "darkBuffer")
	VertexBuffer cygnus$getDarkBuffer();

	@Accessor(value = "skyBuffer")
	VertexBuffer cygnus$getSkyBuffer();

	@Accessor(value = "starBuffer")
	VertexBuffer cygnus$getStarBuffer();
}
