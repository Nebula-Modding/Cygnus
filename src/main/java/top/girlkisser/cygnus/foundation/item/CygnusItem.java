package top.girlkisser.cygnus.foundation.item;

import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.FluidUtil;
import net.neoforged.neoforge.fluids.capability.IFluidHandlerItem;
import net.neoforged.neoforge.fluids.capability.templates.FluidHandlerItemStack;
import top.girlkisser.cygnus.content.registry.CygnusDataComponents;

public interface CygnusItem
{
	default boolean hasFluidTank()
	{
		return false;
	}

	default int getDefaultFluidHandlerCapacity()
	{
		return 1000;
	}

	default IFluidHandlerItem makeFluidHandler(ItemStack stack)
	{
		return new FluidHandlerItemStack(CygnusDataComponents.GENERIC_FLUID, stack, getDefaultFluidHandlerCapacity());
	}

	default IFluidHandlerItem getFluidHandler(ItemStack stack)
	{
		if (!hasFluidTank())
			return null;
		return FluidUtil.getFluidHandler(stack).orElseThrow();
	}

	default boolean hasFluid(ItemStack stack)
	{
		return hasFluidTank() && !getFluid(stack).isEmpty();
	}

	default FluidStack getFluid(ItemStack stack, int tank)
	{
		if (!hasFluidTank())
			return null;
		return getFluidHandler(stack).getFluidInTank(tank);
	}

	default FluidStack getFluid(ItemStack stack)
	{
		return getFluid(stack, 0);
	}

	default int getFluidTankCapacity(ItemStack stack, int tank)
	{
		if (!hasFluidTank())
			return -1;
		return getFluidHandler(stack).getTankCapacity(tank);
	}

	default int getFluidTankCapacity(ItemStack stack)
	{
		return getFluidTankCapacity(stack, 0);
	}
}
