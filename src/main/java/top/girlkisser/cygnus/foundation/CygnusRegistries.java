package top.girlkisser.cygnus.foundation;

import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import org.jetbrains.annotations.ApiStatus;
import top.girlkisser.cygnus.Cygnus;
import top.girlkisser.cygnus.foundation.space.Galaxy;
import top.girlkisser.cygnus.foundation.space.Planet;
import top.girlkisser.cygnus.foundation.space.Star;

@SuppressWarnings("unused")
@ApiStatus.NonExtendable
public interface CygnusRegistries
{
	ResourceKey<Registry<Galaxy>> GALAXY = ResourceKey.createRegistryKey(Cygnus.id("galaxies"));
	ResourceKey<Registry<Star>> STAR = ResourceKey.createRegistryKey(Cygnus.id("stars"));
	ResourceKey<Registry<Planet>> PLANET = ResourceKey.createRegistryKey(Cygnus.id("planets"));
}
