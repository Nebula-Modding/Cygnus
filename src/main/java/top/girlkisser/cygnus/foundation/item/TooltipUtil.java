package top.girlkisser.cygnus.foundation.item;

import net.minecraft.network.chat.Component;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.IFluidTank;

public final class TooltipUtil
{
	public static Component getFluidTankTooltip(IFluidTank tank)
	{
		return getFluidTankTooltip(tank.getFluid(), tank.getCapacity());
	}

	public static Component getFluidTankTooltip(FluidStack stack, int maxCapacity)
	{
		return stack.getHoverName().copy().append(": " + stack.getAmount() + "/" + maxCapacity + "mB");
	}
}
