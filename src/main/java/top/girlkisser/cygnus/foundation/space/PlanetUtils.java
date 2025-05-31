package top.girlkisser.cygnus.foundation.space;

import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.Level;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class PlanetUtils
{
	private static final Map<ResourceKey<Level>, Optional<Planet>> planetCache = new HashMap<>();

	private static void cache(Level level)
	{
		var planet = Planet.getPlanets(level.registryAccess())
			.stream()
			.filter(p -> p.dimension().equals(level.dimension().location()))
			.findFirst();
		planetCache.put(level.dimension(), planet);
	}

	public static void clearCache()
	{
		planetCache.clear();
	}

	public static boolean doesDimensionHavePlanet(Level level)
	{
		return getPlanetForDimension(level).isPresent();
	}

	public static Optional<Planet> getPlanetForDimension(Level level)
	{
		if (!planetCache.containsKey(level.dimension()))
			cache(level);
		return planetCache.get(level.dimension());
	}
}
