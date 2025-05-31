package top.girlkisser.cygnus.content;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.ApiStatus;
import top.girlkisser.cygnus.Cygnus;

@ApiStatus.NonExtendable
public interface CygnusResourceKeys
{
	ResourceKey<Level> SPACE = ResourceKey.create(Registries.DIMENSION, Cygnus.id("space"));
}
