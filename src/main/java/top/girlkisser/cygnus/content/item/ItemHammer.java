package top.girlkisser.cygnus.content.item;

import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import org.jetbrains.annotations.NotNull;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.List;

public class ItemHammer extends Item
{
	public ItemHammer(Properties properties)
	{
		super(properties);
	}

	@Override
	public boolean hasCraftingRemainingItem(@NotNull ItemStack stack)
	{
		return true;
	}

	@Override
	public @NotNull ItemStack getCraftingRemainingItem(ItemStack stack)
	{
		var s = stack.copy();
		if (s.getDamageValue() >= s.getMaxDamage())
		{
			return ItemStack.EMPTY;
		}
		s.setDamageValue(s.getDamageValue() + 1);
		return s;
	}

	@Override
	@ParametersAreNonnullByDefault
	public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag)
	{
		tooltipComponents.add(Component.translatable("item.cygnus.hammer.tooltip"));
	}
}
