package top.girlkisser.cygnus.content.registry;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;
import net.neoforged.neoforge.fluids.SimpleFluidContent;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;
import org.jetbrains.annotations.ApiStatus;
import top.girlkisser.cygnus.Cygnus;
import top.girlkisser.cygnus.content.item.ItemOxygenDrill;

import java.util.function.Supplier;

@SuppressWarnings("unused")
@ApiStatus.NonExtendable
public interface CygnusItems
{
	DeferredRegister.Items R = DeferredRegister.createItems(Cygnus.MODID);

	static <T extends Item> DeferredItem<T> reg(String id, Supplier<T> item)
	{
		return R.register(id, item);
	}

	static DeferredItem<?> reg(String id)
	{
		return R.register(id, () -> new Item(new Item.Properties()));
	}

	DeferredItem<?>
		// Resources
		STEEL_INGOT = reg("steel_ingot"),
		STEEL_SHEET = reg("steel_sheet"),
		STEEL_ROD = reg("steel_rod"),
		STEEL_NUGGET = reg("steel_nugget"),
		ALUMINIUM_INGOT = reg("aluminium_ingot"),
		ALUMINIUM_SHEET = reg("aluminium_sheet"),
		ALUMINIUM_ROD = reg("aluminium_rod"),
		ALUMINIUM_NUGGET = reg("aluminium_nugget"),
		TITANIUM_INGOT = reg("titanium_ingot"),
		TITANIUM_SHEET = reg("titanium_sheet"),
		TITANIUM_ROD = reg("titanium_rod"),
		TITANIUM_NUGGET = reg("titanium_nugget"),

		ELECTRONIC_CIRCUIT = reg("electronic_circuit"),

		// Tools
		OXYGEN_DRILL = reg("oxygen_drill", () -> new ItemOxygenDrill(new Item.Properties()
			.stacksTo(1)
			.rarity(Rarity.RARE)
			.component(CygnusDataComponents.GENERIC_FLUID, SimpleFluidContent.EMPTY)));
}
