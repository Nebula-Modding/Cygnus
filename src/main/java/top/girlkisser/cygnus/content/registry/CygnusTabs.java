package top.girlkisser.cygnus.content.registry;

import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.valueproviders.ConstantInt;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.DropExperienceBlock;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import org.jetbrains.annotations.ApiStatus;
import top.girlkisser.cygnus.Cygnus;
import top.girlkisser.fractal.api.CreativeSubTab;
import top.girlkisser.fractal.api.CreativeSubTabStyle;

import java.util.List;

import static top.girlkisser.cygnus.content.registry.CygnusBlocks.*;
import static top.girlkisser.cygnus.content.registry.CygnusItems.*;

@SuppressWarnings("unused")
@ApiStatus.NonExtendable
public interface CygnusTabs
{
	DeferredRegister<CreativeModeTab> R = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, Cygnus.MODID);

	static CreativeSubTabStyle makePlanetStyle(String modId, String planetId)
	{
		return new CreativeSubTabStyle.Builder()
			.subtab(
				ResourceLocation.fromNamespaceAndPath(modId, "container/creative_inventory/" + planetId + "_subtab_selected_left"),
				ResourceLocation.fromNamespaceAndPath(modId, "container/creative_inventory/" + planetId + "_subtab_unselected_left"),
				ResourceLocation.fromNamespaceAndPath(modId, "container/creative_inventory/" + planetId + "_subtab_selected_right"),
				ResourceLocation.fromNamespaceAndPath(modId, "container/creative_inventory/" + planetId + "_subtab_unselected_right")
			)
			.build();
	}

	CreativeModeTab TAB = CreativeModeTab.builder()
		.icon(() -> CygnusBlocks.TELEPAD.asItem().getDefaultInstance())
		.title(Component.translatable("itemGroup.cygnus"))
		.displayItems((params, output) ->
			CygnusItems.R.getEntries().forEach(item -> output.accept(item.get())))
		.build();

	DeferredHolder<CreativeModeTab, CreativeModeTab> TAB_HOLDER = R.register("tab", () -> TAB);

	// @formatter:off
	CreativeModeTab SUBTAB_MACHINERY = new CreativeSubTab.Builder(TAB, Cygnus.id("machinery"), Component.translatable("itemGroup.cygnus.machinery"))
		.entries((itemDisplayParameters, output) ->
		{
			List.of(
				COMMAND_CENTRE,
				TERMINAL,
				TELEPAD,
				CHRONITE_BLAST_FURNACE
			).forEach(it -> output.accept(it.get()));
		})
		.build();

	CreativeModeTab SUBTAB_ITEMS = new CreativeSubTab.Builder(TAB, Cygnus.id("items"), Component.translatable("itemGroup.cygnus.items"))
		.entries((itemDisplayParameters, output) ->
		{
			List.of(
				// Resources
				IRON_SHEET,
				IRON_ROD,
				STEEL_NUGGET,
				STEEL_INGOT,
				STEEL_SHEET,
				STEEL_ROD,
				RAW_ALUMINIUM,
				ALUMINIUM_NUGGET,
				ALUMINIUM_INGOT,
				ALUMINIUM_SHEET,
				ALUMINIUM_ROD,
				RAW_TITANIUM,
				TITANIUM_NUGGET,
				TITANIUM_INGOT,
				TITANIUM_SHEET,
				TITANIUM_ROD,
				CHRONITE_SHARD,
				// Ingredients
				ELECTRONIC_CIRCUIT,
				CHRONITE_CIRCUIT,
				// Foods
				APOLLO_CHEESE,
				// Tools
				KEY,
				HAMMER,
				OXYGEN_DRILL
			).forEach(it -> output.accept(it.get()));
		})
		.build();

	CreativeModeTab SUBTAB_BLOCKS = new CreativeSubTab.Builder(TAB, Cygnus.id("blocks"), Component.translatable("itemGroup.cygnus.blocks"))
		.entries((itemDisplayParameters, output) ->
		{
			List.of(
			// Raw Blocks
				RAW_ALUMINIUM_BLOCK,
				RAW_TITANIUM_BLOCK,
			// Decorational Blocks
				// Iron
				IRON_SHEET_METAL,
				IRON_CHISELLED,
				IRON_GRATE,
				IRON_CUT,
				IRON_CUT_STAIRS,
				IRON_CUT_SLAB,
				IRON_CUT_PRESSURE_PLATE,
				IRON_CUT_BUTTON,
				IRON_WINDOW,
				IRON_PILLAR,
				IRON_AIRTIGHT_DOOR,
				IRON_AIRTIGHT_TRAPDOOR,
				IRON_BULB,
				// Steel
				STEEL_SHEET_METAL,
				STEEL_BLOCK,
				STEEL_CHISELLED,
				STEEL_GRATE,
				STEEL_CUT,
				STEEL_CUT_STAIRS,
				STEEL_CUT_SLAB,
				STEEL_CUT_PRESSURE_PLATE,
				STEEL_CUT_BUTTON,
				STEEL_WINDOW,
				STEEL_PILLAR,
				STEEL_BARS,
				STEEL_DOOR,
				STEEL_TRAPDOOR,
				STEEL_BULB,
				// Aluminium
				ALUMINIUM_SHEET_METAL,
				ALUMINIUM_BLOCK,
				ALUMINIUM_CHISELLED,
				ALUMINIUM_GRATE,
				ALUMINIUM_CUT,
				ALUMINIUM_CUT_STAIRS,
				ALUMINIUM_CUT_SLAB,
				ALUMINIUM_CUT_PRESSURE_PLATE,
				ALUMINIUM_CUT_BUTTON,
				ALUMINIUM_WINDOW,
				ALUMINIUM_PILLAR,
				ALUMINIUM_BARS,
				ALUMINIUM_DOOR,
				ALUMINIUM_TRAPDOOR,
				ALUMINIUM_BULB,
				// Titanium
				TITANIUM_SHEET_METAL,
				TITANIUM_BLOCK,
				TITANIUM_CHISELLED,
				TITANIUM_GRATE,
				TITANIUM_CUT,
				TITANIUM_CUT_STAIRS,
				TITANIUM_CUT_SLAB,
				TITANIUM_CUT_PRESSURE_PLATE,
				TITANIUM_CUT_BUTTON,
				TITANIUM_WINDOW,
				TITANIUM_PILLAR,
				TITANIUM_BARS,
				TITANIUM_DOOR,
				TITANIUM_TRAPDOOR,
				TITANIUM_BULB,
			// Natural Blocks
				// Ores
				ALUMINIUM_ORE,
				DEEPSLATE_ALUMINIUM_ORE,
				TITANIUM_ORE,
				DEEPSLATE_TITANIUM_ORE,
				// Chronite
				CHRONITE_BLOCK,
				BUDDING_CHRONITE,
				CHRONITE_CLUSTER,
				LARGE_CHRONITE_BUD,
				MEDIUM_CHRONITE_BUD,
				SMALL_CHRONITE_BUD
			).forEach(it -> output.accept(it.get()));
		})
		.build();

	CreativeModeTab SUBTAB_MOON = new CreativeSubTab.Builder(TAB, Cygnus.id("moon"), Component.translatable("itemGroup.cygnus.moon"))
		.entries((itemDisplayParameters, output) ->
		{
			List.of(
				// Regolith
				LUNAR_REGOLITH,
				// Stone
				LUNAR_STONE,
				LUNAR_COBBLESTONE,
				POLISHED_LUNAR_STONE,
				CHISELED_LUNAR_STONE,
				LUNAR_STONE_BRICKS,
				CRACKED_LUNAR_STONE_BRICKS,
				LUNAR_STONE_PILLAR,
				// Deepslate
				LUNAR_DEEPSLATE,
				COBBLED_LUNAR_DEEPSLATE,
				POLISHED_LUNAR_DEEPSLATE,
				CHISELED_LUNAR_DEEPSLATE,
				LUNAR_DEEPSLATE_BRICKS,
				CRACKED_LUNAR_DEEPSLATE_BRICKS,
				LUNAR_DEEPSLATE_PILLAR,
				// Ores
				LUNAR_COAL_ORE,
				LUNAR_DEEPSLATE_COAL_ORE,
				LUNAR_IRON_ORE,
				LUNAR_DEEPSLATE_IRON_ORE,
				LUNAR_COPPER_ORE,
				LUNAR_DEEPSLATE_COPPER_ORE,
				LUNAR_GOLD_ORE,
				LUNAR_DEEPSLATE_GOLD_ORE,
				LUNAR_REDSTONE_ORE,
				LUNAR_DEEPSLATE_REDSTONE_ORE,
				LUNAR_EMERALD_ORE,
				LUNAR_DEEPSLATE_EMERALD_ORE,
				LUNAR_LAPIS_ORE,
				LUNAR_DEEPSLATE_LAPIS_ORE,
				LUNAR_DIAMOND_ORE,
				LUNAR_DEEPSLATE_DIAMOND_ORE,
				LUNAR_ALUMINIUM_ORE,
				LUNAR_DEEPSLATE_ALUMINIUM_ORE,
				LUNAR_TITANIUM_ORE,
				LUNAR_DEEPSLATE_TITANIUM_ORE
			).forEach(it -> output.accept(it.get()));
		})
		.hideParentTitle()
		.styled(makePlanetStyle(Cygnus.MODID, "moon"))
		.build();

	CreativeModeTab SUBTAB_MARS = new CreativeSubTab.Builder(TAB, Cygnus.id("mars"), Component.translatable("itemGroup.cygnus.mars"))
		.entries((itemDisplayParameters, output) ->
		{
//			List.of(
//				// Regolith
//				MARTIAN_REGOLITH,
//				// Stone
//				MARTIAN_STONE,
//				MARTIAN_COBBLESTONE,
//				POLISHED_MARTIAN_STONE,
//				CHISELED_MARTIAN_STONE,
//				MARTIAN_STONE_BRICKS,
//				CRACKED_MARTIAN_STONE_BRICKS,
//				MARTIAN_STONE_PILLAR,
//				// Deepslate
//				MARTIAN_DEEPSLATE,
//				COBBLED_MARTIAN_DEEPSLATE,
//				POLISHED_MARTIAN_DEEPSLATE,
//				CHISELED_MARTIAN_DEEPSLATE,
//				MARTIAN_DEEPSLATE_BRICKS,
//				CRACKED_MARTIAN_DEEPSLATE_BRICKS,
//				MARTIAN_DEEPSLATE_PILLAR,
//				// Ores
//				MARTIAN_COAL_ORE,
//				MARTIAN_DEEPSLATE_COAL_ORE,
//				MARTIAN_IRON_ORE,
//				MARTIAN_DEEPSLATE_IRON_ORE,
//				MARTIAN_COPPER_ORE,
//				MARTIAN_DEEPSLATE_COPPER_ORE,
//				MARTIAN_GOLD_ORE,
//				MARTIAN_DEEPSLATE_GOLD_ORE,
//				MARTIAN_REDSTONE_ORE,
//				MARTIAN_DEEPSLATE_REDSTONE_ORE,
//				MARTIAN_EMERALD_ORE,
//				MARTIAN_DEEPSLATE_EMERALD_ORE,
//				MARTIAN_LAPIS_ORE,
//				MARTIAN_DEEPSLATE_LAPIS_ORE,
//				MARTIAN_DIAMOND_ORE,
//				MARTIAN_DEEPSLATE_DIAMOND_ORE,
//				MARTIAN_ALUMINIUM_ORE,
//				MARTIAN_DEEPSLATE_ALUMINIUM_ORE,
//				MARTIAN_TITANIUM_ORE,
//				MARTIAN_DEEPSLATE_TITANIUM_ORE
//			).forEach(it -> output.accept(it.get()));
		})
		.hideParentTitle()
		.styled(makePlanetStyle(Cygnus.MODID, "mars"))
		.build();

	CreativeModeTab SUBTAB_MERCURY = new CreativeSubTab.Builder(TAB, Cygnus.id("mercury"), Component.translatable("itemGroup.cygnus.mercury"))
		.entries((itemDisplayParameters, output) ->
		{
//			List.of(
//				// Regolith
//				MERCURIAL_REGOLITH,
//				// Stone
//				MERCURIAL_STONE,
//				MERCURIAL_COBBLESTONE,
//				POLISHED_MERCURIAL_STONE,
//				CHISELED_MERCURIAL_STONE,
//				MERCURIAL_STONE_BRICKS,
//				CRACKED_MERCURIAL_STONE_BRICKS,
//				MERCURIAL_STONE_PILLAR,
//				// Deepslate
//				MERCURIAL_DEEPSLATE,
//				COBBLED_MERCURIAL_DEEPSLATE,
//				POLISHED_MERCURIAL_DEEPSLATE,
//				CHISELED_MERCURIAL_DEEPSLATE,
//				MERCURIAL_DEEPSLATE_BRICKS,
//				CRACKED_MERCURIAL_DEEPSLATE_BRICKS,
//				MERCURIAL_DEEPSLATE_PILLAR,
//				// Ores
//				MERCURIAL_COAL_ORE,
//				MERCURIAL_DEEPSLATE_COAL_ORE,
//				MERCURIAL_IRON_ORE,
//				MERCURIAL_DEEPSLATE_IRON_ORE,
//				MERCURIAL_COPPER_ORE,
//				MERCURIAL_DEEPSLATE_COPPER_ORE,
//				MERCURIAL_GOLD_ORE,
//				MERCURIAL_DEEPSLATE_GOLD_ORE,
//				MERCURIAL_REDSTONE_ORE,
//				MERCURIAL_DEEPSLATE_REDSTONE_ORE,
//				MERCURIAL_EMERALD_ORE,
//				MERCURIAL_DEEPSLATE_EMERALD_ORE,
//				MERCURIAL_LAPIS_ORE,
//				MERCURIAL_DEEPSLATE_LAPIS_ORE,
//				MERCURIAL_DIAMOND_ORE,
//				MERCURIAL_DEEPSLATE_DIAMOND_ORE,
//				MERCURIAL_ALUMINIUM_ORE,
//				MERCURIAL_DEEPSLATE_ALUMINIUM_ORE,
//				MERCURIAL_TITANIUM_ORE,
//				MERCURIAL_DEEPSLATE_TITANIUM_ORE
//			).forEach(it -> output.accept(it.get()));
		})
		.hideParentTitle()
		.styled(makePlanetStyle(Cygnus.MODID, "mercury"))
		.build();

	CreativeModeTab SUBTAB_VENUS = new CreativeSubTab.Builder(TAB, Cygnus.id("venus"), Component.translatable("itemGroup.cygnus.venus"))
		.entries((itemDisplayParameters, output) ->
		{
//			List.of(
//				// Regolith
//				VENUSIAN_REGOLITH,
//				// Stone
//				VENUSIAN_STONE,
//				VENUSIAN_COBBLESTONE,
//				POLISHED_VENUSIAN_STONE,
//				CHISELED_VENUSIAN_STONE,
//				VENUSIAN_STONE_BRICKS,
//				CRACKED_VENUSIAN_STONE_BRICKS,
//				VENUSIAN_STONE_PILLAR,
//				// Deepslate
//				VENUSIAN_DEEPSLATE,
//				COBBLED_VENUSIAN_DEEPSLATE,
//				POLISHED_VENUSIAN_DEEPSLATE,
//				CHISELED_VENUSIAN_DEEPSLATE,
//				VENUSIAN_DEEPSLATE_BRICKS,
//				CRACKED_VENUSIAN_DEEPSLATE_BRICKS,
//				VENUSIAN_DEEPSLATE_PILLAR,
//				// Ores
//				VENUSIAN_COAL_ORE,
//				VENUSIAN_DEEPSLATE_COAL_ORE,
//				VENUSIAN_IRON_ORE,
//				VENUSIAN_DEEPSLATE_IRON_ORE,
//				VENUSIAN_COPPER_ORE,
//				VENUSIAN_DEEPSLATE_COPPER_ORE,
//				VENUSIAN_GOLD_ORE,
//				VENUSIAN_DEEPSLATE_GOLD_ORE,
//				VENUSIAN_REDSTONE_ORE,
//				VENUSIAN_DEEPSLATE_REDSTONE_ORE,
//				VENUSIAN_EMERALD_ORE,
//				VENUSIAN_DEEPSLATE_EMERALD_ORE,
//				VENUSIAN_LAPIS_ORE,
//				VENUSIAN_DEEPSLATE_LAPIS_ORE,
//				VENUSIAN_DIAMOND_ORE,
//				VENUSIAN_DEEPSLATE_DIAMOND_ORE,
//				VENUSIAN_ALUMINIUM_ORE,
//				VENUSIAN_DEEPSLATE_ALUMINIUM_ORE,
//				VENUSIAN_TITANIUM_ORE,
//				VENUSIAN_DEEPSLATE_TITANIUM_ORE
//			).forEach(it -> output.accept(it.get()));
		})
		.hideParentTitle()
		.styled(makePlanetStyle(Cygnus.MODID, "venus"))
		.build();

	// TODO: I'm not sure if I want an "all" tab or not. I'll leave it commented out for a while and if I feel like I want it (or if someone else wants it) then I'll uncomment it.
//	CreativeModeTab SUBTAB_ALL = new CreativeSubTab.Builder(TAB, Cygnus.id("all"), Component.translatable("itemGroup.cygnus.all"))
//		.entries((itemDisplayParameters, output) ->
//		{
//			output.acceptAll(CygnusTabs.SUBTAB_MACHINERY.getDisplayItems());
//			output.acceptAll(CygnusTabs.SUBTAB_ITEMS.getDisplayItems());
//			output.acceptAll(CygnusTabs.SUBTAB_BLOCKS.getDisplayItems());
//			output.acceptAll(CygnusTabs.SUBTAB_MOON.getDisplayItems());
//			output.acceptAll(CygnusTabs.SUBTAB_MARS.getDisplayItems());
//			output.acceptAll(CygnusTabs.SUBTAB_MERCURY.getDisplayItems());
//			output.acceptAll(CygnusTabs.SUBTAB_VENUS.getDisplayItems());
//		})
//		.build();
	// @formatter:on
}
