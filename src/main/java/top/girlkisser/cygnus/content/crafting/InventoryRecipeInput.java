package top.girlkisser.cygnus.content.crafting;

import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeInput;
import org.jetbrains.annotations.NotNull;

public record InventoryRecipeInput(Inventory inventory) implements RecipeInput
{
	@Override
	public @NotNull ItemStack getItem(int index)
	{
		return inventory.getItem(index);
	}

	@Override
	public int size()
	{
		return inventory.getContainerSize();
	}
}
