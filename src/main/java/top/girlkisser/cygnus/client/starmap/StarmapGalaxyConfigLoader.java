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

public class StarmapGalaxyConfigLoader extends SimpleJsonResourceReloadListener
{
	public static final Map<ResourceLocation, StarmapGalaxyConfig> GALAXIES = new HashMap<>();

	public StarmapGalaxyConfigLoader()
	{
		super(new Gson(), Cygnus.MODID + "/starmap/galaxies");
	}

	@Override
	@ParametersAreNonnullByDefault
	protected void apply(Map<ResourceLocation, JsonElement> object, ResourceManager resourceManager, ProfilerFiller profiler)
	{
		GALAXIES.clear();
		object.forEach((id, json) ->
		{
			JsonObject obj = GsonHelper.convertToJsonObject(json, "galaxies");
			DataResult<StarmapGalaxyConfig> config = StarmapGalaxyConfig.CODEC.parse(JsonOps.INSTANCE, obj);
			if (config.isError())
				Cygnus.LOGGER.error(config.error().orElseThrow().message());
			else
				GALAXIES.put(id, config.getOrThrow());
		});
	}
}
