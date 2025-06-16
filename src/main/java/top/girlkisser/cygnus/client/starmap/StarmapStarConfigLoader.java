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

public class StarmapStarConfigLoader extends SimpleJsonResourceReloadListener
{
	public static final Map<ResourceLocation, StarmapStarConfig> STARS = new HashMap<>();

	public StarmapStarConfigLoader()
	{
		super(new Gson(), Cygnus.MODID + "/starmap/stars");
	}

	@Override
	@ParametersAreNonnullByDefault
	protected void apply(Map<ResourceLocation, JsonElement> object, ResourceManager resourceManager, ProfilerFiller profiler)
	{
		STARS.clear();
		object.forEach((id, json) ->
		{
			JsonObject obj = GsonHelper.convertToJsonObject(json, "stars");
			DataResult<StarmapStarConfig> config = StarmapStarConfig.CODEC.parse(JsonOps.INSTANCE, obj);
			if (config.isError())
				Cygnus.LOGGER.error(config.error().orElseThrow().message());
			else
				STARS.put(id, config.getOrThrow());
		});
	}
}
