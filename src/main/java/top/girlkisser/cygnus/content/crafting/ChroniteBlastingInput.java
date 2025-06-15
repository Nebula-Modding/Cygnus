package top.girlkisser.cygnus.content.crafting;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeInput;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public record ChroniteBlastingInput(List<ItemStack> stacks) implements RecipeInput
{
	public boolean isEmpty()
	{
		return stacks.stream().allMatch(ItemStack::isEmpty);
	}

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
