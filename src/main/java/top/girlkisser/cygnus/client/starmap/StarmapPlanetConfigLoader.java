package top.girlkisser.cygnus.client.starmap;

import com.google.gson.Gson;
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

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.HashMap;
import java.util.Map;

public class StarmapPlanetConfigLoader extends SimpleJsonResourceReloadListener
{
	public static final Map<ResourceLocation, StarmapPlanetConfig> PLANETS = new HashMap<>();

	public StarmapPlanetConfigLoader()
	{
		super(new Gson(), Cygnus.MODID + "/starmap/planets");
	}

	@Override
	@ParametersAreNonnullByDefault
	protected void apply(Map<ResourceLocation, JsonElement> object, ResourceManager resourceManager, ProfilerFiller profiler)
	{
		PLANETS.clear();
		object.forEach((id, json) ->
		{
			JsonObject obj = GsonHelper.convertToJsonObject(json, "planets");
			DataResult<StarmapPlanetConfig> config = StarmapPlanetConfig.CODEC.parse(JsonOps.INSTANCE, obj);
			if (config.isError())
				Cygnus.LOGGER.error(config.error().orElseThrow().message());
			else
				PLANETS.put(id, config.getOrThrow());
		});
	}
}
