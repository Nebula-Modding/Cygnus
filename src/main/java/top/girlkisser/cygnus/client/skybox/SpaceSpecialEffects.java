package top.girlkisser.cygnus.client.skybox;

import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.Tesselator;
import com.mojang.blaze3d.vertex.VertexBuffer;
import com.mojang.blaze3d.vertex.VertexFormat;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceKey;
import top.girlkisser.cygnus.api.space.Planet;
import top.girlkisser.cygnus.api.space.PlanetUtils;
import top.girlkisser.cygnus.client.CygnusClient;
import top.girlkisser.cygnus.content.CygnusResourceKeys;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class SpaceSpecialEffects extends AbstractSpaceSpecialEffects
{
	// This gets set to null when the player changes dimension
	protected static @Nullable PlanetRenderer cachedRenderer = null;
	// We use this instead of the default starBuffer in LevelRenderer so that we can render fancy stars :3
	protected static Map<PlanetRenderer, VertexBuffer> stars = new HashMap<>();


	public PlanetRenderer getRenderer()
	{
		if (cachedRenderer == null)
		{
			var level = level();
			Optional<ResourceKey<Planet>> planet;
			if (level.dimension().equals(CygnusResourceKeys.SPACE) && CygnusClient.mySpaceStation != null)
				planet = Planet.getPlanetById(level.registryAccess(), CygnusClient.mySpaceStation.orbiting()).map(Holder.Reference::key);
			else
				planet = PlanetUtils.getPlanetIdForDimension(level);
			if (planet.isPresent() && PlanetRendererLoader.RENDERERS.containsKey(planet.get().location()))
				cachedRenderer = PlanetRendererLoader.RENDERERS.get(planet.get().location());
			else
				return PlanetRenderer.DEFAULT;
		}
		return cachedRenderer;
	}

	@Override
	protected List<SkyboxObject> getSkyboxObjects()
	{
		return getRenderer().objects();
	}

	@Override
	protected @Nullable VertexBuffer getStarBuffer()
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

	@Override
	protected boolean useOrbitSkyboxObjects()
	{
		return level().dimension().equals(CygnusResourceKeys.SPACE) && CygnusClient.mySpaceStation != null;
	}

	@Override
	protected float getStarBrightness(float partialTick)
	{
		var level = level();
		float rainLevel = 1.0F - level.getRainLevel(partialTick);
		// wow... that's a lot of getters...
		if (getRenderer().stars().isPresent() && getRenderer().stars().get().brightnessCurve().isPresent())
			return getRenderer().stars().get().brightnessCurve().get().sample(level.getDayTime() / 24000f).y * rainLevel;
		else
			return level.getStarBrightness(partialTick) * rainLevel;
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
}
