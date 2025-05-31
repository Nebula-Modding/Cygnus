package top.girlkisser.cygnus.content.item;

import net.minecraft.core.BlockPos;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.PickaxeItem;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.Tool;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.common.ItemAbilities;
import net.neoforged.neoforge.common.ItemAbility;
import net.neoforged.neoforge.common.SimpleTier;
import net.neoforged.neoforge.common.Tags;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;
import net.neoforged.neoforge.fluids.capability.IFluidHandlerItem;
import net.neoforged.neoforge.fluids.capability.templates.FluidHandlerItemStack;
import top.girlkisser.cygnus.content.CygnusTags;
import top.girlkisser.cygnus.content.registry.CygnusDataComponents;
import top.girlkisser.cygnus.foundation.item.CygnusItem;
import top.girlkisser.cygnus.foundation.item.TooltipUtil;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.List;

@ParametersAreNonnullByDefault
public class ItemOxygenDrill extends PickaxeItem implements CygnusItem
{
	public static final Tier TIER = new SimpleTier(CygnusTags.Blocks.INCORRECT_FOR_OXYGEN_DRILl, 2048, 10f, 2f, 20, () -> Ingredient.of(Tags.Items.INGOTS_IRON));

	public ItemOxygenDrill(Properties properties)
	{
		super(TIER, properties);
	}

	@Override
	public boolean hasFluidTank()
	{
		return true;
	}

	@Override
	public IFluidHandlerItem makeFluidHandler(ItemStack stack)
	{
		return new FluidHandlerItemStack(CygnusDataComponents.GENERIC_FLUID, stack, getDefaultFluidHandlerCapacity())
		{
			@Override
			public boolean isFluidValid(int tank, FluidStack stack)
			{
				return stack.is(CygnusTags.Fluids.OXYGEN);
			}
		};
	}

	public boolean canPerformAction(ItemStack stack, ItemAbility itemAbility)
	{
		return ItemAbilities.DEFAULT_PICKAXE_ACTIONS.contains(itemAbility) && !getFluid(stack).isEmpty();
	}

	@Override
	public boolean mineBlock(ItemStack stack, Level level, BlockState state, BlockPos pos, LivingEntity miningEntity)
	{
		Tool tool = stack.get(DataComponents.TOOL);

		if (tool != null && !level.isClientSide && state.getDestroySpeed(level, pos) != 0.0F && tool.damagePerBlock() > 0)
		{
			consumeFuel(miningEntity, stack, 1);
			stack.hurtAndBreak(tool.damagePerBlock(), miningEntity, EquipmentSlot.MAINHAND);
			return true;
		}

		return false;
	}

	@Override
	public float getDestroySpeed(ItemStack stack, BlockState state)
	{
		if (getFluid(stack).isEmpty())
			return 1f;

		return super.getDestroySpeed(stack, state);
	}

	@Override
	public boolean isCorrectToolForDrops(ItemStack stack, BlockState state)
	{
		if (getFluid(stack).isEmpty())
			return false;

		return super.isCorrectToolForDrops(stack, state);
	}

	public void consumeFuel(LivingEntity entity, ItemStack stack, int amount)
	{
		if (!(stack.getItem() instanceof ItemOxygenDrill))
			return;

		if (entity instanceof Player player && player.isCreative())
			return;

		getFluidHandler(stack).drain(amount, IFluidHandler.FluidAction.EXECUTE);
	}

	@Override
	public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> text, TooltipFlag tooltipFlag)
	{
		text.add(TooltipUtil.getFluidTankTooltip(getFluid(stack), getFluidTankCapacity(stack)));
		text.add(Component.translatable("item.cygnus.oxygen_drill.tooltip"));
	}

	@Override
	public boolean isBarVisible(ItemStack stack)
	{
		return hasFluid(stack);
	}

	@Override
	public int getBarWidth(ItemStack stack)
	{
		return (int) ((double)getFluid(stack).getAmount() / getFluidTankCapacity(stack) * 13);
	}

	@Override
	public int getBarColor(ItemStack stack)
	{
		return 0xAFAFAF;
	}

	public boolean shouldCauseReequipAnimation(ItemStack old, ItemStack newStack, boolean slotChanged)
	{
		return false;
	}
}
