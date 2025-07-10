package top.girlkisser.cygnus.content.registry;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;
import net.neoforged.neoforge.fluids.SimpleFluidContent;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;
import org.jetbrains.annotations.ApiStatus;
import top.girlkisser.cygnus.Cygnus;
import top.girlkisser.cygnus.content.CygnusFoods;
import top.girlkisser.cygnus.content.item.ItemChronite;
import top.girlkisser.cygnus.content.item.ItemHammer;
import top.girlkisser.cygnus.content.item.ItemKey;
import top.girlkisser.cygnus.content.item.ItemOxygenDrill;

import java.util.function.Supplier;
import java.util.function.UnaryOperator;

@SuppressWarnings("unused")
@ApiStatus.NonExtendable
public interface CygnusItems
{
	DeferredRegister.Items R = DeferredRegister.createItems(Cygnus.MODID);

	static <T extends Item> DeferredItem<T> reg(String id, Supplier<T> item)
	{
		return R.register(id, item);
	}

	static DeferredItem<?> reg(String id, UnaryOperator<Item.Properties> properties)
	{
		return R.register(id, () -> new Item(properties.apply(new Item.Properties())));
	}

	static DeferredItem<?> reg(String id)
	{
		return R.register(id, () -> new Item(new Item.Properties()));
	}

	// Resources
	DeferredItem<?>
		STEEL_NUGGET = reg("steel_nugget"),
		STEEL_INGOT = reg("steel_ingot"),
		STEEL_SHEET = reg("steel_sheet"),
		STEEL_ROD = reg("steel_rod"),
		RAW_ALUMINIUM = reg("raw_aluminum"),
		ALUMINIUM_NUGGET = reg("aluminum_nugget"),
		ALUMINIUM_INGOT = reg("aluminum_ingot"),
		ALUMINIUM_SHEET = reg("aluminum_sheet"),
		ALUMINIUM_ROD = reg("aluminum_rod"),
		RAW_TITANIUM = reg("raw_titanium"),
		TITANIUM_NUGGET = reg("titanium_nugget"),
		TITANIUM_INGOT = reg("titanium_ingot"),
		TITANIUM_SHEET = reg("titanium_sheet"),
		TITANIUM_ROD = reg("titanium_rod"),
		CHRONITE_SHARD = reg("chronite_shard", () -> new ItemChronite(new Item.Properties().rarity(Rarity.UNCOMMON)));

	// Ingredients
	DeferredItem<?>
		ELECTRONIC_CIRCUIT = reg("electronic_circuit"),
		CHRONITE_CIRCUIT = reg("chronite_circuit", p -> p.rarity(Rarity.UNCOMMON));

	// Foods
	DeferredItem<?>
		APOLLO_CHEESE = reg("apollo_cheese", p -> p.food(CygnusFoods.APOLLO_CHEESE));

	// Tools
	DeferredItem<?>
		HAMMER = reg("hammer", () -> new ItemHammer(new Item.Properties().stacksTo(1).durability(512))),
		KEY = reg("key", () -> new ItemKey(new Item.Properties().stacksTo(1))),
		OXYGEN_DRILL = reg("oxygen_drill", () -> new ItemOxygenDrill(new Item.Properties()
			.stacksTo(1)
			.component(CygnusDataComponents.GENERIC_FLUID, SimpleFluidContent.EMPTY)));
}
