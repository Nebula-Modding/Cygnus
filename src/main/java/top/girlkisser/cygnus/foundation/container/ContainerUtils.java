package top.girlkisser.cygnus.foundation.container;

import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;

import java.util.List;
import java.util.function.Predicate;

public final class ContainerUtils
{
	private ContainerUtils()
	{
	}

	public static int extractItems(Container container, Predicate<ItemStack> matching, int count)
	{
		int remaining = count;

		for (int i = 0 ; i < container.getContainerSize() ; i++)
		{
			ItemStack stack = container.getItem(i);
			if (matching.test(stack))
			{
				int oldStackCount = stack.getCount();
				stack.shrink(remaining);
				int newStackCount = stack.getCount();
				if (stack.isEmpty())
					container.setItem(i, ItemStack.EMPTY);
				int change = oldStackCount - newStackCount;
				remaining -= change;
				if (remaining <= 0)
					break;
			}
		}

		return remaining;
	}

	public static int countItems(Container container, Predicate<ItemStack> matching)
	{
		int count = 0;

		for (int i = 0 ; i < container.getContainerSize() ; i++)
		{
			ItemStack stack = container.getItem(i);
			if (matching.test(stack))
				count += stack.getCount();
		}

		return count;
	}

	public static boolean doesListContainItemMatchingIngredient(List<ItemStack> stacks, Ingredient ingredient)
	{
		for (var stack : stacks)
		{
			if (ingredient.test(stack))
				return true;
		}
		return false;
	}

	// Similar to extractItems but handles crafting remainders
	public static int extractItemsForCrafting(Container container, Predicate<ItemStack> matching, int count)
	{
		int remaining = count;

		for (int i = 0 ; i < container.getContainerSize() ; i++)
		{
			ItemStack stack = container.getItem(i);
			if (matching.test(stack))
			{
				if (stack.hasCraftingRemainingItem())
				{
					container.setItem(i, stack.getCraftingRemainingItem());
					remaining--;
				}
				else
				{
					int oldStackCount = stack.getCount();
					stack.shrink(remaining);
					int newStackCount = stack.getCount();
					if (stack.isEmpty())
						container.setItem(i, ItemStack.EMPTY);
					int change = oldStackCount - newStackCount;
					remaining -= change;
				}
				if (remaining <= 0)
					break;
			}
		}

		return remaining;
	}
}
