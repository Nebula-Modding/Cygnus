package top.girlkisser.cygnus.client.skybox;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import com.mojang.datafixers.util.Pair;
import com.mojang.math.Axis;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.*;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.material.FogType;
import net.minecraft.world.phys.Vec3;
import org.joml.Matrix4f;
import org.joml.Quaternionf;
import org.joml.Vector3f;
import top.girlkisser.cygnus.foundation.colours.UnpackedColour;
import top.girlkisser.cygnus.foundation.space.Planet;
import top.girlkisser.cygnus.foundation.space.PlanetUtils;
import top.girlkisser.cygnus.mixin.client.LevelRendererAccessor;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class SpaceSpecialEffects extends DimensionSpecialEffects
{
	private static final ResourceLocation MOON_LOCATION = ResourceLocation.withDefaultNamespace("textures/environment/moon_phases.png");
	private static final ResourceLocation SUN_LOCATION = ResourceLocation.withDefaultNamespace("textures/environment/sun.png");

	public static final long STAR_SEED = 10842L;

	// This gets set to null when the player changes dimension
	protected static @Nullable PlanetRenderer cachedRenderer = null;
	// We use this instead of the default starBuffer in LevelRenderer so that we can render fancy stars :3
	protected static Map<PlanetRenderer, VertexBuffer> stars = new HashMap<>();

	protected final Minecraft minecraft = Minecraft.getInstance();

	public SpaceSpecialEffects()
	{
		super(Float.NaN, true, SkyType.NONE, false, false);
	}

	// Invalidates data for the *current dimension*
	// This gets called when the player reloads resources (F3+T) or when they change dimensions
	public static void invalidatePerDimensionCaches()
	{
		cachedRenderer = null;
	}

	// Invalidates all cached data for all dimensions
	// This gets called only when the player reloads resources (F3+T)
	public static void invalidateAllCaches()
	{
		invalidatePerDimensionCaches();
		stars.values().forEach(VertexBuffer::close);
		stars.clear();
	}

	public PlanetRenderer getRenderer()
	{
		if (cachedRenderer == null)
		{
			Optional<Pair<ResourceKey<Planet>, Planet>> planet = PlanetUtils.getPlanetAndIdForDimension(level());
			if (planet.isEmpty())
				return PlanetRenderer.DEFAULT;
			if (PlanetRendererLoader.RENDERERS.containsKey(planet.get().getFirst().location()))
				cachedRenderer = PlanetRendererLoader.RENDERERS.get(planet.get().getFirst().location());
			else
				return PlanetRenderer.DEFAULT;
		}
		return cachedRenderer;
	}

	public @Nullable VertexBuffer getStarBuffer()
	{
		@Nullable VertexBuffer starBuffer = stars.getOrDefault(getRenderer(), null);
		if (starBuffer == null)
		{
			VertexBuffer buffer = new VertexBuffer(VertexBuffer.Usage.STATIC);
			buffer.bind();
			if (getRenderer().stars().isEmpty())
				buffer.upload(Tesselator.getInstance().begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_COLOR).buildOrThrow());
			else
				buffer.upload(makeStars(getRenderer().stars().get(), Tesselator.getInstance()));
			VertexBuffer.unbind();
			stars.put(getRenderer(), buffer);
			return buffer;
		}
		return starBuffer;
	}

	private Level level()
	{
		assert this.minecraft.level != null;
		return this.minecraft.level;
	}

	@Override
	public Vec3 getBrightnessDependentFogColor(Vec3 fogColor, float brightness)
	{
		return fogColor;
	}

	@Override
	public boolean isFoggyAt(int x, int y)
	{
		return false;
	}

	@Override
	public boolean renderClouds(ClientLevel level, int ticks, float partialTick, PoseStack poseStack, double camX, double camY, double camZ, Matrix4f modelViewMatrix, Matrix4f projectionMatrix)
	{
//		boolean hasClouds = getRenderer().textures().clouds().isPresent();
//		if (hasClouds)
//		{
//			// todo
//		}
		return true;
	}

	@SuppressWarnings("resource")
	@Override
	public boolean renderSky(ClientLevel level, int ticks, float partialTick, Matrix4f frustumMatrix, Camera camera, Matrix4f projectionMatrix, boolean isFoggy, Runnable setupFog)
	{
		setupFog.run();
		if (isFoggy)
			return true;

		LevelRenderer renderer = Minecraft.getInstance().levelRenderer;
		LevelRendererAccessor lra = (LevelRendererAccessor) renderer;

		FogType fogType = camera.getFluidInCamera();
		if (fogType == FogType.POWDER_SNOW || fogType == FogType.LAVA || lra.cygnus$doesMobEffectBlockSky(camera))
			return true;

		LocalPlayer player = minecraft.player;
		assert player != null;
		Tesselator tes = Tesselator.getInstance();

		PoseStack poses = new PoseStack();
		poses.mulPose(frustumMatrix);

		renderSky(poses, level, partialTick, projectionMatrix);

		float rainLevel = 1.0F - level.getRainLevel(partialTick);
		RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, rainLevel);

		// Render stars
		if (getStarBuffer() != null)
		{
			float starBrightness;

			// wow... that's a lot of getters...
			if (getRenderer().stars().isPresent() && getRenderer().stars().get().brightnessCurve().isPresent())
				starBrightness = getRenderer().stars().get().brightnessCurve().get().sample(level.getTimeOfDay(partialTick)).y * rainLevel;
			else
				starBrightness = level.getStarBrightness(partialTick) * rainLevel;

			if (starBrightness > 0)
			{
				RenderSystem.setShaderColor(starBrightness, starBrightness, starBrightness, starBrightness);
				FogRenderer.setupNoFog();
				getStarBuffer().bind();
				//noinspection DataFlowIssue
				getStarBuffer().drawWithShader(poses.last().pose(), projectionMatrix, GameRenderer.getPositionColorShader());
				VertexBuffer.unbind();
				setupFog.run();
			}
		}

		// Render skybox objects
		// This line changes black pixels into the sky colour
//		RenderSystem.blendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
		poses.pushPose();

		RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);

		for (SkyboxObject obj : getRenderer().objects())
		{
			renderSkyboxObject(obj, poses, level, partialTick, tes);
		}

		RenderSystem.disableBlend();
		RenderSystem.defaultBlendFunc();
		poses.popPose();

		// I honestly have no clue what this part of the sky rendering does, I think it's the horizon?
		RenderSystem.setShaderColor(0.0F, 0.0F, 0.0F, 1.0F);
		double d0 = player.getEyePosition(partialTick).y - level.getLevelData().getHorizonHeight(level);
		if (d0 < 0d)
		{
			poses.pushPose();
			poses.translate(0.0F, 12.0F, 0.0F);
			lra.cygnus$getDarkBuffer().bind();
			//noinspection DataFlowIssue
			lra.cygnus$getDarkBuffer().drawWithShader(poses.last().pose(), projectionMatrix, RenderSystem.getShader());
			VertexBuffer.unbind();
			poses.popPose();
		}

		RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
		RenderSystem.depthMask(true);

		return true;
	}

	@Override
	public boolean renderSnowAndRain(ClientLevel level, int ticks, float partialTick, LightTexture lightTexture, double camX, double camY, double camZ)
	{
		return true;
	}

	@SuppressWarnings("resource")
	private void renderSky(PoseStack poses, ClientLevel level, float partialTick, Matrix4f projectionMatrix)
	{
		LevelRenderer renderer = Minecraft.getInstance().levelRenderer;
		LevelRendererAccessor lra = (LevelRendererAccessor) renderer;
		Tesselator tesselator = Tesselator.getInstance();

		Vec3 skyColor = level.getSkyColor(this.minecraft.gameRenderer.getMainCamera().getPosition(), partialTick);
		float skyR = (float) skyColor.x;
		float skyG = (float) skyColor.y;
		float skyB = (float) skyColor.z;
		FogRenderer.levelFogColor();
		RenderSystem.depthMask(false);
		RenderSystem.setShaderColor(skyR, skyG, skyB, 1.0F);
		lra.cygnus$getSkyBuffer().bind();
		//noinspection DataFlowIssue
		lra.cygnus$getSkyBuffer().drawWithShader(poses.last().pose(), projectionMatrix, RenderSystem.getShader());
		VertexBuffer.unbind();
		RenderSystem.enableBlend();
		float[] sunriseColor = getSunriseColor(level.getTimeOfDay(partialTick), partialTick);
		if (sunriseColor != null)
		{
			RenderSystem.setShader(GameRenderer::getPositionColorShader);
			RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
			poses.pushPose();
			poses.mulPose(Axis.XP.rotationDegrees(90.0F));
			float f3 = Mth.sin(level.getSunAngle(partialTick)) < 0.0F ? 180.0F : 0.0F;
			poses.mulPose(Axis.ZP.rotationDegrees(f3));
			poses.mulPose(Axis.ZP.rotationDegrees(90.0F));
			Matrix4f matrix4f = poses.last().pose();
			BufferBuilder buffer = tesselator.begin(VertexFormat.Mode.TRIANGLE_FAN, DefaultVertexFormat.POSITION_COLOR);
			buffer.addVertex(matrix4f, 0.0F, 100.0F, 0.0F).setColor(sunriseColor[0], sunriseColor[1], sunriseColor[2], sunriseColor[3]);

			for (int i = 0 ; i <= 16 ; i++)
			{
				float f7 = (float) i * ((float) Math.PI * 2F) / 16.0F;
				float f8 = Mth.sin(f7);
				float f9 = Mth.cos(f7);
				buffer.addVertex(matrix4f, f8 * 120.0F, f9 * 120.0F, -f9 * 40.0F * sunriseColor[3]).setColor(sunriseColor[0], sunriseColor[1], sunriseColor[2], 0.0F);
			}

			BufferUploader.drawWithShader(buffer.buildOrThrow());
			poses.popPose();
		}
	}

	private void renderSkyboxObject(SkyboxObject object, PoseStack poses, ClientLevel level, float partialTick, Tesselator tes)
	{
		poses.pushPose();

		poses.mulPose(Axis.XP.rotationDegrees(level.getTimeOfDay(partialTick) * 360.0F + object.skyRotation().x));
		poses.mulPose(Axis.YP.rotationDegrees(-90.0F + object.skyRotation().y));
		poses.mulPose(Axis.ZP.rotationDegrees(object.skyRotation().z));

		// Render the backlight (if there is one)
		if (object.backlight().isPresent())
		{
			SkyboxObject.Backlight backlight = object.backlight().get();
			float size = backlight.size() * object.scale();
			int colour = backlight.color().pack();

			poses.pushPose();
			Matrix4f pose = poses.last().pose();
			RenderSystem.setShader(GameRenderer::getPositionTexColorShader);
			RenderSystem.setShaderTexture(0, backlight.texture());
			BufferBuilder buffer = tes.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX_COLOR);
			buffer.addVertex(pose, -size, 100.0F, -size).setUv(0.0F, 0.0F).setColor(colour);
			buffer.addVertex(pose, size, 100.0F, -size).setUv(1.0F, 0.0F).setColor(colour);
			buffer.addVertex(pose, size, 100.0F, size).setUv(1.0F, 1.0F).setColor(colour);
			buffer.addVertex(pose, -size, 100.0F, size).setUv(0.0F, 1.0F).setColor(colour);
			BufferUploader.drawWithShader(buffer.buildOrThrow());
			poses.popPose();
		}

		for (SkyboxObject.Layer layer : object.layers())
		{
			float size = layer.size() * object.scale();

			poses.pushPose();
			poses.mulPose(Axis.XP.rotationDegrees(layer.skyRotation().x));
			poses.mulPose(Axis.YP.rotationDegrees(layer.skyRotation().y));
			poses.mulPose(Axis.ZP.rotationDegrees(layer.skyRotation().z));

			poses.pushPose();
			poses.rotateAround(Axis.XP.rotationDegrees(object.textureRotation().x + layer.textureRotation().x), 0, 0, 0);
			poses.rotateAround(Axis.YP.rotationDegrees(object.textureRotation().y + layer.textureRotation().y), 0, 0, 0);
			poses.rotateAround(Axis.ZP.rotationDegrees(object.textureRotation().z + layer.textureRotation().z), 0, 0, 0);

			Matrix4f pose = poses.last().pose();
			RenderSystem.setShader(GameRenderer::getPositionTexShader);
			RenderSystem.setShaderTexture(0, layer.texture());
			BufferBuilder buffer = tes.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX);
			buffer.addVertex(pose, -size, 100.0F, -size).setUv(0.0F, 0.0F);
			buffer.addVertex(pose, size, 100.0F, -size).setUv(1.0F, 0.0F);
			buffer.addVertex(pose, size, 100.0F, size).setUv(1.0F, 1.0F);
			buffer.addVertex(pose, -size, 100.0F, size).setUv(0.0F, 1.0F);
			BufferUploader.drawWithShader(buffer.buildOrThrow());

			poses.popPose();
			poses.popPose();
		}

		poses.popPose();
	}

	protected MeshData makeStars(PlanetRenderer.StarInfo stars, Tesselator tes)
	{
		RandomSource rand = RandomSource.create(STAR_SEED);
		BufferBuilder buffer = tes.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_COLOR);

		for (int i = 0 ; i < stars.count() ; i++)
		{
			float x = rand.nextFloat() * 2.0F - 1.0F;
			float y = rand.nextFloat() * 2.0F - 1.0F;
			float z = rand.nextFloat() * 2.0F - 1.0F;
			float size = 0.15F + rand.nextFloat() * 0.1F;
			float f5 = Mth.lengthSquared(x, y, z);
			if (!(f5 <= 0.010000001F) && !(f5 >= 1.0F))
			{
				PlanetRenderer.StarInfo.StarVariant variant = stars.pickVariant(rand);
				UnpackedColour colour = variant.color();

				Vector3f pos = new Vector3f(x, y, z).normalize(100.0F);
				float rotation = (float) (rand.nextDouble() * Math.PI * 2d);
				Quaternionf quaternionf = (new Quaternionf()).rotateTo(new Vector3f(0.0F, 0.0F, -1.0F), pos).rotateZ(rotation);
				buffer.addVertex(pos.add((new Vector3f(size, -size, 0.0F)).rotate(quaternionf))).setColor(colour.r(), colour.g(), colour.b(), colour.a());
				buffer.addVertex(pos.add((new Vector3f(size, size, 0.0F)).rotate(quaternionf))).setColor(colour.r(), colour.g(), colour.b(), colour.a());
				buffer.addVertex(pos.add((new Vector3f(-size, size, 0.0F)).rotate(quaternionf))).setColor(colour.r(), colour.g(), colour.b(), colour.a());
				buffer.addVertex(pos.add((new Vector3f(-size, -size, 0.0F)).rotate(quaternionf))).setColor(colour.r(), colour.g(), colour.b(), colour.a());
			}
		}

		return buffer.buildOrThrow();
	}

	private void renderSun(PoseStack poses, ClientLevel level, float partialTick, Tesselator tes)
	{
		poses.pushPose();
		poses.mulPose(Axis.YP.rotationDegrees(-90.0F));
		poses.mulPose(Axis.XP.rotationDegrees(level.getTimeOfDay(partialTick) * 360.0F));
		Matrix4f pose = poses.last().pose();
		float sunSize = 30.0F;
		RenderSystem.setShader(GameRenderer::getPositionTexShader);
		RenderSystem.setShaderTexture(0, SUN_LOCATION);
		BufferBuilder buffer = tes.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX);
		buffer.addVertex(pose, -sunSize, 100.0F, -sunSize).setUv(0.0F, 0.0F);
		buffer.addVertex(pose, sunSize, 100.0F, -sunSize).setUv(1.0F, 0.0F);
		buffer.addVertex(pose, sunSize, 100.0F, sunSize).setUv(1.0F, 1.0F);
		buffer.addVertex(pose, -sunSize, 100.0F, sunSize).setUv(0.0F, 1.0F);
		BufferUploader.drawWithShader(buffer.buildOrThrow());
		poses.popPose();
	}

	private void renderMoon(PoseStack poses, ClientLevel level, float partialTick, Tesselator tes)
	{
		poses.pushPose();
		poses.mulPose(Axis.YP.rotationDegrees(-90.0F));
		poses.mulPose(Axis.XP.rotationDegrees(level.getTimeOfDay(partialTick) * 360.0F));
		Matrix4f pose = poses.last().pose();
		float sunSize = 20.0F;
		RenderSystem.setShaderTexture(0, MOON_LOCATION);
		int moonPhase = level.getMoonPhase();
		int l = moonPhase % 4;
		int i1 = moonPhase / 4 % 2;
		float f13 = l / 4.0F;
		float f14 = i1 / 2.0F;
		float f15 = (l + 1) / 4.0F;
		float f16 = (i1 + 1) / 2.0F;
		BufferBuilder buffer = tes.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX);
		buffer.addVertex(pose, -sunSize, -100.0F, sunSize).setUv(f15, f16);
		buffer.addVertex(pose, sunSize, -100.0F, sunSize).setUv(f13, f16);
		buffer.addVertex(pose, sunSize, -100.0F, -sunSize).setUv(f13, f14);
		buffer.addVertex(pose, -sunSize, -100.0F, -sunSize).setUv(f15, f14);
		BufferUploader.drawWithShader(buffer.buildOrThrow());
		poses.popPose();
	}

//	private MeshData renderStars(Tesselator tesselator)
//	{
//		RandomSource rand = RandomSource.create(10842L);
//		int starCount = 1500;
//		float f = 100.0F;
//		BufferBuilder buffer = tesselator.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION);
//
//		for (int i = 0 ; i < starCount ; i++)
//		{
//			float x = rand.nextFloat() * 2.0F - 1.0F;
//			float y = rand.nextFloat() * 2.0F - 1.0F;
//			float z = rand.nextFloat() * 2.0F - 1.0F;
//			float size = 0.15F + rand.nextFloat() * 0.1F;
//			float f5 = Mth.lengthSquared(x, y, z);
//			if (!(f5 <= 0.010000001F) && !(f5 >= 1.0F))
//			{
//				Vector3f pos = new Vector3f(x, y, z).normalize(100.0F);
//				float rotation = (float) (rand.nextDouble() * Math.PI * 2d);
//				Quaternionf quaternionf = (new Quaternionf()).rotateTo(new Vector3f(0.0F, 0.0F, -1.0F), pos).rotateZ(rotation);
//				buffer.addVertex(pos.add((new Vector3f(size, -size, 0.0F)).rotate(quaternionf)));
//				buffer.addVertex(pos.add((new Vector3f(size, size, 0.0F)).rotate(quaternionf)));
//				buffer.addVertex(pos.add((new Vector3f(-size, size, 0.0F)).rotate(quaternionf)));
//				buffer.addVertex(pos.add((new Vector3f(-size, -size, 0.0F)).rotate(quaternionf)));
//			}
//		}
//
//		return buffer.buildOrThrow();
//	}
}
