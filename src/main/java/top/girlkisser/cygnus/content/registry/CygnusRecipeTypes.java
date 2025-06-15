package top.girlkisser.cygnus.content.registry;

import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeType;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import org.jetbrains.annotations.ApiStatus;
import top.girlkisser.cygnus.Cygnus;
import top.girlkisser.cygnus.content.crafting.RecipeChroniteBlasting;
import top.girlkisser.cygnus.content.crafting.RecipeSpaceStationCrafting;

@SuppressWarnings("unused")
@ApiStatus.NonExtendable
public interface CygnusRecipeTypes
{
	DeferredRegister<RecipeType<?>> R = DeferredRegister.create(Registries.RECIPE_TYPE, Cygnus.MODID);

	static <T extends Recipe<?>> DeferredHolder<RecipeType<?>, RecipeType<T>> reg(String id, RecipeType<T> type)
	{
		return R.register(id, () -> type);
	}

	DeferredHolder<RecipeType<?>, RecipeType<RecipeSpaceStationCrafting>> SPACE_STATION_CRAFTING =
		reg("space_station_crafting", RecipeSpaceStationCrafting.TYPE);

	DeferredHolder<RecipeType<?>, RecipeType<RecipeChroniteBlasting>> CHRONITE_BLASTING =
		reg("chronite_blasting", RecipeChroniteBlasting.TYPE);
}
