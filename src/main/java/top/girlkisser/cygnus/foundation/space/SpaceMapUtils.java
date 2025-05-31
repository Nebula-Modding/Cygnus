package top.girlkisser.cygnus.foundation.space;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.core.RegistryAccess;
import net.minecraft.resources.ResourceLocation;
import org.joml.Vector2i;
import top.girlkisser.cygnus.Cygnus;
import top.girlkisser.cygnus.client.CygnusClient;

public final class SpaceMapUtils
{
	public static final int DEFAULT_PLANET_SIZE = 32;
	public static final int DEFAULT_STAR_SIZE = 32;
	public static final int DEFAULT_GALAXY_SIZE = 64;
	public static final ResourceLocation PLANET_SELECTION = Cygnus.id("planet/planet_selection");

	private SpaceMapUtils()
	{ }

	public static void renderPlanetOnMap(GuiGraphics graphics, int x, int y, int width, int height, float scale, Planet planet)
	{
		RenderSystem.enableBlend();
		graphics.blitSprite(planet.mapTexture().withPrefix("planet/"), (int)(x * scale), (int)(y * scale), (int)(width * scale), (int)(height * scale));
		RenderSystem.disableBlend();
	}

	public static void renderPlanetAndMoonsOnMap(GuiGraphics graphics, int x, int y, int width, int height, float scale, Planet planet, RegistryAccess registryAccess)
	{
		renderPlanetOnMap(graphics, x, y, width, height, scale, planet);

		// Render moons in orbit
		int orbitDistance = (int)(16 * scale);
		for (ResourceLocation id : planet.moons())
		{
			var moon = Planet.getPlanetByIdOrThrow(registryAccess, id);
			double moonX = x + Math.sin(CygnusClient.clientTicks / moon.yearLength()) * orbitDistance;
			double moonY = y + Math.cos(CygnusClient.clientTicks / moon.yearLength()) * orbitDistance;

			renderPlanetAndMoonsOnMap(graphics, (int)moonX, (int)moonY, DEFAULT_PLANET_SIZE, DEFAULT_PLANET_SIZE, scale, moon, registryAccess);

			orbitDistance += (int)(16 * scale);
		}
	}

	public static void renderPlanetAndMoonsOnMapWithHighlightedMoon(GuiGraphics graphics, int x, int y, int width, int height, float scale, Planet planet, RegistryAccess registryAccess, ResourceLocation highlighted)
	{
		renderPlanetOnMap(graphics, x, y, width, height, scale, planet);

		// Render moons in orbit
		int orbitDistance = (int)(16 * scale);
		for (ResourceLocation id : planet.moons())
		{
			var moon = Planet.getPlanetByIdOrThrow(registryAccess, id);
			double moonX = x + Math.sin(CygnusClient.clientTicks / moon.yearLength()) * orbitDistance;
			double moonY = y + Math.cos(CygnusClient.clientTicks / moon.yearLength()) * orbitDistance;

			renderPlanetAndMoonsOnMapWithHighlightedMoon(graphics, (int)moonX, (int)moonY, DEFAULT_PLANET_SIZE, DEFAULT_PLANET_SIZE, scale, moon, registryAccess, highlighted);

			// Render highlight (if applicable)
			if (highlighted.equals(id))
			{
				RenderSystem.enableBlend();
				graphics.blitSprite(PLANET_SELECTION, (int)((moonX - 2) * scale), (int)((moonY - 2) * scale), (int)((width + 4) * scale), (int)((height + 4) * scale));
				RenderSystem.disableBlend();
			}

			orbitDistance += (int)(16 * scale);
		}
	}

	public static void renderStarOnMap(GuiGraphics graphics, int x, int y, int width, int height, float scale, Star star)
	{
		RenderSystem.enableBlend();
		graphics.blitSprite(star.mapTexture().withPrefix("star/"), (int)(x * scale), (int)(y * scale), (int)(width * scale), (int)(height * scale));
		RenderSystem.disableBlend();
	}

	public static void renderSolarSystemOnMap(GuiGraphics graphics, int x, int y, int width, int height, float scale, Star star, RegistryAccess registryAccess)
	{
		renderStarOnMap(graphics, x, y, width, height, scale, star);

		// Render planets in orbit
		int orbitDistance = (int)(32 * scale);
		for (ResourceLocation id : star.planets())
		{
			var planet = Planet.getPlanetByIdOrThrow(registryAccess, id);
			double planetX = x + Math.sin(CygnusClient.clientTicks / planet.yearLength()) * orbitDistance;
			double planetY = y + Math.cos(CygnusClient.clientTicks / planet.yearLength()) * orbitDistance;

			renderPlanetAndMoonsOnMap(graphics, (int)planetX, (int)planetY, DEFAULT_PLANET_SIZE, DEFAULT_PLANET_SIZE, scale, planet, registryAccess);

			orbitDistance += (int)(32 * scale);
		}
	}

	public static void renderSolarSystemOnMapWithHighlight(GuiGraphics graphics, int x, int y, int width, int height, float scale, Star star, RegistryAccess registryAccess, ResourceLocation highlighted)
	{
		renderStarOnMap(graphics, x, y, width, height, scale, star);

		// Render planets in orbit
		int orbitDistance = (int)(32 * scale);
		for (ResourceLocation id : star.planets())
		{
			var planet = Planet.getPlanetByIdOrThrow(registryAccess, id);
			double planetX = x + Math.sin(CygnusClient.clientTicks / planet.yearLength()) * orbitDistance;
			double planetY = y + Math.cos(CygnusClient.clientTicks / planet.yearLength()) * orbitDistance;

			renderPlanetAndMoonsOnMapWithHighlightedMoon(graphics, (int)planetX, (int)planetY, DEFAULT_PLANET_SIZE, DEFAULT_PLANET_SIZE, scale, planet, registryAccess, highlighted);

			// Render highlight (if applicable)
			if (highlighted.equals(id))
			{
				RenderSystem.enableBlend();
				graphics.blitSprite(PLANET_SELECTION, (int)((planetX - 2) * scale), (int)((planetY - 2) * scale), (int)((width + 4) * scale), (int)((height + 4) * scale));
				RenderSystem.disableBlend();
			}

			orbitDistance += (int)(32 * scale);
		}
	}

	public static void renderGalaxyOnMap(GuiGraphics graphics, int x, int y, int width, int height, float scale, Galaxy galaxy)
	{
		RenderSystem.enableBlend();
		graphics.blitSprite(galaxy.mapTexture().withPrefix("galaxy/"), (int)(scale * x), (int)(scale * y), (int)(scale * width), (int)(scale * height));
		RenderSystem.disableBlend();
	}

	public static Vector2i getCentreForSprite(int minX, int minY, int maxX, int maxY, int spriteWidth, int spriteHeight)
	{
		return new Vector2i(
			minX + (maxX - minX) / 2 - spriteWidth / 2,
			minY + (maxY - minY) / 2 - spriteHeight / 2
		);
	}
}
