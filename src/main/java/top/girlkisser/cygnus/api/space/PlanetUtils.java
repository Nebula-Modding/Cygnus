package top.girlkisser.cygnus.api.space;

import com.mojang.datafixers.util.Pair;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.Level;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class PlanetUtils
{
	private static final Map<ResourceKey<Level>, Optional<Pair<ResourceKey<Planet>, Planet>>> planetCache = new HashMap<>();

	private static void cache(Level level)
	{
		var planet = Planet.getPlanetsWithIds(level.registryAccess())
			.stream()
			.filter(p ->
				p.getSecond().dimension().isPresent() &&
				p.getSecond().dimension().get().equals(level.dimension().location())
			)
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
		return getPlanetAndIdForDimension(level).map(Pair::getSecond);
	}

	public static Optional<ResourceKey<Planet>> getPlanetIdForDimension(Level level)
	{
		return getPlanetAndIdForDimension(level).map(Pair::getFirst);
	}

	public static Optional<Pair<ResourceKey<Planet>, Planet>> getPlanetAndIdForDimension(Level level)
	{
		if (!planetCache.containsKey(level.dimension()))
			cache(level);
		return planetCache.get(level.dimension());
	}
}
