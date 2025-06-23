package top.girlkisser.cygnus.content.registry;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.FastColor;
import net.neoforged.neoforge.fluids.FluidType;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;
import org.jetbrains.annotations.ApiStatus;
import org.joml.Vector3f;
import top.girlkisser.cygnus.Cygnus;
import top.girlkisser.lazuli.api.fluid.BasicFluidType;

@SuppressWarnings("unused")
@ApiStatus.NonExtendable
public interface CygnusFluidTypes
{
	DeferredRegister<FluidType> R = DeferredRegister.create(NeoForgeRegistries.FLUID_TYPES, Cygnus.MODID);

	ResourceLocation
		WATER_STILL_TEXTURE = ResourceLocation.withDefaultNamespace("block/water_still"),
		WATER_FLOWING = ResourceLocation.withDefaultNamespace("block/water_flow"),
		WATER_OVERLAY = ResourceLocation.withDefaultNamespace("block/water_overlay");

	static <T extends FluidType> DeferredHolder<FluidType, T> reg(String id, T fluidType)
	{
		return R.register(id, () -> fluidType);
	}

	static DeferredHolder<FluidType, BasicFluidType> reg(String id, int colour, FluidType.Properties properties)
	{
		return reg(id, new BasicFluidType(
			WATER_STILL_TEXTURE,
			WATER_FLOWING,
			WATER_OVERLAY,
			colour,
			new Vector3f(
				FastColor.ARGB32.red(colour),
				FastColor.ARGB32.green(colour),
				FastColor.ARGB32.blue(colour)
			),
			properties
		));
	}

	DeferredHolder<FluidType, BasicFluidType> OXYGEN_GAS = reg("oxygen_gas", 0xEFEFEFDD, FluidType.Properties.create());
}
