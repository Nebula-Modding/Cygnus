package top.girlkisser.cygnus.content.registry;

import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.Registries;
import net.neoforged.neoforge.fluids.SimpleFluidContent;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import org.jetbrains.annotations.ApiStatus;
import top.girlkisser.cygnus.Cygnus;

import java.util.function.UnaryOperator;

@SuppressWarnings("unused")
@ApiStatus.NonExtendable
public interface CygnusDataComponents
{
	DeferredRegister.DataComponents R = DeferredRegister.createDataComponents(Registries.DATA_COMPONENT_TYPE, Cygnus.MODID);

	static <T> DeferredHolder<DataComponentType<?>, DataComponentType<T>> reg(String id, UnaryOperator<DataComponentType.Builder<T>> builder)
	{
		return R.registerComponentType(id, builder);
	}

	DeferredHolder<DataComponentType<?>, DataComponentType<SimpleFluidContent>> GENERIC_FLUID = reg("fluid",
		b -> b
			.persistent(SimpleFluidContent.CODEC)
			.networkSynchronized(SimpleFluidContent.STREAM_CODEC));
}
