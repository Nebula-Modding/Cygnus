package top.girlkisser.cygnus.client.skybox;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.JsonOps;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.util.GsonHelper;
import net.minecraft.util.profiling.ProfilerFiller;
import top.girlkisser.cygnus.Cygnus;
import top.girlkisser.cygnus.client.starmap.StarmapGalaxyConfig;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.HashMap;
import java.util.Map;

public class PlanetRendererLoader extends SimpleJsonResourceReloadListener
{
	public static final Map<ResourceLocation, PlanetRenderer> RENDERERS = new HashMap<>();

	public PlanetRendererLoader()
	{
		super(new Gson(), Cygnus.MODID + "/planet_renderers");
	}

	@Override
	@ParametersAreNonnullByDefault
	protected void apply(Map<ResourceLocation, JsonElement> object, ResourceManager resourceManager, ProfilerFiller profiler)
	{
		SpaceSpecialEffects.invalidatePerDimensionCaches();
		SpaceSpecialEffects.invalidateAllCaches();
		RENDERERS.clear();
		object.forEach((id, json) ->
		{
			JsonObject obj = GsonHelper.convertToJsonObject(json, "planets");
			DataResult<PlanetRenderer> renderer = PlanetRenderer.CODEC.parse(JsonOps.INSTANCE, obj);
			if (renderer.isError())
				Cygnus.LOGGER.error(renderer.error().orElseThrow().message());
			else
				RENDERERS.put(id, renderer.getOrThrow());
		});
	}
}
