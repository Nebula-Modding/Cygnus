package top.girlkisser.cygnus.content.item;

import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.List;

public class ItemChronite extends Item
{
	public ItemChronite(Properties properties)
	{
		super(properties);
	}

	@ParametersAreNonnullByDefault
	public void inventoryTick(ItemStack stack, Level level, Entity entity, int slotId, boolean isSelected)
	{
		if ((isSelected || (entity instanceof Player p && p.getOffhandItem().is(this))) && !entity.fireImmune())
			entity.hurt(level.damageSources().hotFloor(), 1.0F);
	}

	@Override
	@ParametersAreNonnullByDefault
	public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag)
	{
		tooltipComponents.add(Component.translatable("item.cygnus.chronite.tooltip"));
	}
}
