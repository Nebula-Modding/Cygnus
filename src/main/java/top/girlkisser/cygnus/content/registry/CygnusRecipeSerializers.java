package top.girlkisser.cygnus.content.registry;

import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import org.jetbrains.annotations.ApiStatus;
import top.girlkisser.cygnus.Cygnus;
import top.girlkisser.cygnus.content.crafting.RecipeSpaceStationCrafting;

@SuppressWarnings("unused")
@ApiStatus.NonExtendable
public interface CygnusRecipeSerializers
{
	DeferredRegister<RecipeSerializer<?>> R = DeferredRegister.create(Registries.RECIPE_SERIALIZER, Cygnus.MODID);

	static <T extends Recipe<?>> DeferredHolder<RecipeSerializer<?>, RecipeSerializer<T>> reg(String id, RecipeSerializer<T> serializer)
	{
		return R.register(id, () -> serializer);
	}

	DeferredHolder<RecipeSerializer<?>, RecipeSerializer<RecipeSpaceStationCrafting>> SPACE_STATION_CRAFTING =
		reg("space_station_crafting", RecipeSpaceStationCrafting.SERIALIZER);
}
