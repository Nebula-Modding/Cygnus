package top.girlkisser.cygnus.content.crafting;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeInput;
import org.jetbrains.annotations.NotNull;

import java.util.List;

// Technically I could use CraftingInput, but honestly it does more than I need it to :p
public record IngredientListRecipeInput(
	List<ItemStack> stacks
) implements RecipeInput
{
	@Override
	public @NotNull ItemStack getItem(int index)
	{
		return stacks.get(index);
	}

	@Override
	public int size()
	{
		return stacks.size();
	}
}
