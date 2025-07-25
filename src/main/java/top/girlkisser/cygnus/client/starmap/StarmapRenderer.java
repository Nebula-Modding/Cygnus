package top.girlkisser.cygnus.client.starmap;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.Rect2i;
import net.minecraft.core.RegistryAccess;
import net.minecraft.resources.ResourceLocation;
import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector2i;
import top.girlkisser.cygnus.Cygnus;
import top.girlkisser.cygnus.api.space.Planet;
import top.girlkisser.cygnus.api.space.Star;
import top.girlkisser.lazuli.api.client.LazuliClientHelpers;

import javax.annotation.Nullable;

public class StarmapRenderer
{
	public static final ResourceLocation PLANET_SELECTION = Cygnus.id("textures/gui/sprites/planet/planet_selection.png");

	protected GuiGraphics graphics;
	protected Rect2i region;
	protected float scale;
	public ResourceLocation highlighted;

	public StarmapRenderer(GuiGraphics graphics, Rect2i region, float scale)
	{
		this.graphics = graphics;
		this.region = region;
		this.scale = scale;
	}

	public void renderBody(float orbitX, float orbitY, double yearLength, float orbitDistance, float size, ResourceLocation texture, @Nullable ResourceLocation bodyId)
	{
		double orbitProgress = LazuliClientHelpers.clientTicks / yearLength;
		double x = orbitX + (Math.sin(orbitProgress) * orbitDistance);
		double y = orbitY + (Math.cos(orbitProgress) * orbitDistance);

		float x1 = (float)(x - (size * scale) / 2d);
		float x2 = (float)(x + (size * scale) / 2d);
		float y1 = (float)(y - (size * scale) / 2d);
		float y2 = (float)(y + (size * scale) / 2d);

		PoseStack poses = graphics.pose();

		RenderSystem.enableBlend();
//		RenderSystem.setShaderColor(1, 1, 1, 1);

		RenderSystem.setShaderTexture(0, texture);
		RenderSystem.setShader(GameRenderer::getPositionTexShader);
		Matrix4f pose = poses.last().pose();
		BufferBuilder buffer = Tesselator.getInstance().begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX);
		buffer.addVertex(pose, x1, y1, 0).setUv(0, 0);
		buffer.addVertex(pose, x1, y2, 0).setUv(0, 1);
		buffer.addVertex(pose, x2, y2, 0).setUv(1, 1);
		buffer.addVertex(pose, x2, y1, 0).setUv(1, 0);
		BufferUploader.drawWithShader(buffer.buildOrThrow());

		if (bodyId != null && bodyId.equals(highlighted))
		{
			RenderSystem.setShaderTexture(0, PLANET_SELECTION);
			buffer = Tesselator.getInstance().begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX);
			float s = (float)Math.sin((LazuliClientHelpers.clientTicks % 60) / 2f) + 5;
			buffer.addVertex(pose, x1-s, y1-s, 0).setUv(0, 0);
			buffer.addVertex(pose, x1-s, y2+s, 0).setUv(0, 1);
			buffer.addVertex(pose, x2+s, y2+s, 0).setUv(1, 1);
			buffer.addVertex(pose, x2+s, y1-s, 0).setUv(1, 0);
			BufferUploader.drawWithShader(buffer.buildOrThrow());
		}

		RenderSystem.disableBlend();
	}

	public void renderPlanet(float orbitX, float orbitY, ResourceLocation planetId, Planet planet, StarmapPlanetConfig renderConfig, boolean renderMoons)
	{
		renderBody(orbitX, orbitY, planet.yearLength(), renderConfig.orbitDistance() * scale, renderConfig.size(), renderConfig.texture(), planetId);
		if (renderMoons)
		{
			assert Minecraft.getInstance().level != null;
			RegistryAccess ra = Minecraft.getInstance().level.registryAccess();
			double orbitProgress = LazuliClientHelpers.clientTicks / planet.yearLength();
			double planetX = orbitX + (Math.sin(orbitProgress) * renderConfig.orbitDistance() * scale);
			double planetY = orbitY + (Math.cos(orbitProgress) * renderConfig.orbitDistance() * scale);

			for (var moon : planet.moons())
			{
				var rc = StarmapPlanetConfigLoader.getRenderConfigOrThrow(moon);
				renderPlanet((float)planetX, (float)planetY, moon, Planet.getPlanetByIdOrThrow(ra, moon), rc, true);
			}
		}
	}

	public void renderStar(ResourceLocation starId, Star star, StarmapStarConfig renderConfig, boolean renderWholeSolarSystem)
	{
		Vector2i origin = getCentreForSprite(region.getX(), region.getY(), region.getX() + region.getWidth(), region.getY() + region.getHeight(), (int)(renderConfig.size() * scale), (int)(renderConfig.size() * scale));

		renderBody(origin.x, origin.y, 1, 0, renderConfig.size(), renderConfig.texture(), starId);

		if (renderWholeSolarSystem)
		{
			assert Minecraft.getInstance().level != null;
			RegistryAccess ra = Minecraft.getInstance().level.registryAccess();
			for (var planet : star.planets())
			{
				renderPlanet(origin.x, origin.y, planet, Planet.getPlanetByIdOrThrow(ra, planet), StarmapPlanetConfigLoader.getRenderConfigOrThrow(planet), true);
			}
		}
	}

	public void renderGalaxy(StarmapGalaxyConfig renderConfig)
	{
		Vector2i origin = getCentreForSprite(region.getX(), region.getY(), region.getX() + region.getWidth(), region.getY() + region.getHeight(), (int)(renderConfig.size() * scale), (int)(renderConfig.size() * scale));
		renderBody(origin.x, origin.y, 1, 0, renderConfig.size(), renderConfig.texture(), null);
	}

	public static Vector2i getCentreForSprite(int minX, int minY, int maxX, int maxY, int spriteWidth, int spriteHeight)
	{
		return new Vector2i(
			minX + (maxX - minX) / 2 - spriteWidth / 2,
			minY + (maxY - minY) / 2 - spriteHeight / 2
		);
	}

	public static Vector2f getOrbitPositionOfPlanet(Planet planet, StarmapPlanetConfig renderConfig, int orbitX, int orbitY)
	{
		double orbitProgress = LazuliClientHelpers.clientTicks / planet.yearLength();
		double x = orbitX + (Math.sin(orbitProgress) * renderConfig.orbitDistance());
		double y = orbitY + (Math.cos(orbitProgress) * renderConfig.orbitDistance());
		return new Vector2f((float) x, (float) y);
	}
}
