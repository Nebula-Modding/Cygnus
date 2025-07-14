package top.girlkisser.cygnus.datagen.server

import martian.dapper.api.server.recipe.DapperRecipeProvider
import martian.dapper.api.server.recipe.DapperShapedRecipeUtil.shapedRecipeBuilder
import martian.dapper.api.server.recipe.DapperShapedRecipeUtil.unlockWith
import martian.dapper.api.server.recipe.DapperShapelessRecipeUtil.shapelessRecipeBuilder
import martian.dapper.api.server.recipe.DapperShapelessRecipeUtil.unlockWith
import martian.dapper.api.server.recipe.DapperSmeltingRecipeUtil.smeltsTo
import martian.dapper.api.server.recipe.DapperSmeltingRecipeUtil.unlockWith
import net.minecraft.core.registries.Registries
import net.minecraft.resources.ResourceLocation
import net.minecraft.tags.TagKey
import net.minecraft.world.item.Items
import net.minecraft.world.item.crafting.Ingredient
import net.minecraft.world.level.ItemLike
import net.neoforged.neoforge.data.event.GatherDataEvent
import net.neoforged.neoforge.registries.DeferredBlock
import top.girlkisser.cygnus.Cygnus.id
import top.girlkisser.cygnus.content.CygnusTags
import top.girlkisser.cygnus.content.registry.CygnusBlocks
import top.girlkisser.cygnus.content.registry.CygnusItems

class CygnusRecipeProvider(event: GatherDataEvent) : DapperRecipeProvider(event)
{
	override fun buildRecipes()
	{
		/**
		 * Hey, you! Yes, you! If you are editing this file:
		 * USE REGIONS!!! Please help me keep this code clean!
		 * Data generation is never pretty, Dapper does help, but
		 * obviously it is not going to fix the sheer amount of
		 * repetition going on here. For that reason, **please** use
		 * region blocks to allow code folding and faster visual
		 * grepping.
		 * Kthxbai!!
		 *
		 * (for those who do not know, this is a region block)
		 * // region [some name]
		 * // endregion [that same name]
		 * Within that area is collapsable by Intellij and also will
		 * allow future readers to find where they are in a long file.
		 *
		 * Another note: name your regions using this format, for
		 * example:
		 * // region Items
		 * // region Items/Tools
		 * // endregion Items/Tools
		 * // endregion Items
		 *
		 * Okay rant over
		 */

		// region Items
        // region Items/Resources
		basicResource(
			"iron",
			null,
			null,
			null,
			CygnusItems.IRON_SHEET,
			CygnusItems.IRON_ROD,
			null,
			null,
			setOf(),
		)
		basicResource(
			"steel",
			null, // Steel does not have a raw variant... yet >:3
			CygnusItems.STEEL_NUGGET,
			CygnusItems.STEEL_INGOT,
			CygnusItems.STEEL_SHEET,
			CygnusItems.STEEL_ROD,
			CygnusBlocks.STEEL_BLOCK,
			null,
			setOf(),
		)
		basicResource(
			"aluminum",
			CygnusItems.RAW_ALUMINIUM,
			CygnusItems.ALUMINIUM_NUGGET,
			CygnusItems.ALUMINIUM_INGOT,
			CygnusItems.ALUMINIUM_SHEET,
			CygnusItems.ALUMINIUM_ROD,
			CygnusBlocks.ALUMINIUM_BLOCK,
			CygnusBlocks.RAW_ALUMINIUM_BLOCK,
			setOf(
				CygnusBlocks.ALUMINIUM_ORE,
				CygnusBlocks.DEEPSLATE_ALUMINIUM_ORE,
				CygnusBlocks.LUNAR_ALUMINIUM_ORE,
				CygnusBlocks.LUNAR_DEEPSLATE_ALUMINIUM_ORE,
//				CygnusBlocks.MARTIAN_ALUMINIUM_ORE,
//				CygnusBlocks.MARTIAN_DEEPSLATE_ALUMINIUM_ORE,
//				CygnusBlocks.MERCURIAL_ALUMINIUM_ORE,
//				CygnusBlocks.MERCURIAL_DEEPSLATE_ALUMINIUM_ORE,
//				CygnusBlocks.VENUSIAN_ALUMINIUM_ORE,
//				CygnusBlocks.VENUSIAN_DEEPSLATE_ALUMINIUM_ORE,
			),
		)
		basicResource(
			"titanium",
			CygnusItems.RAW_TITANIUM,
			CygnusItems.TITANIUM_NUGGET,
			CygnusItems.TITANIUM_INGOT,
			CygnusItems.TITANIUM_SHEET,
			CygnusItems.TITANIUM_ROD,
			CygnusBlocks.TITANIUM_BLOCK,
			CygnusBlocks.RAW_TITANIUM_BLOCK,
			setOf(
				CygnusBlocks.TITANIUM_ORE,
				CygnusBlocks.DEEPSLATE_TITANIUM_ORE,
				CygnusBlocks.LUNAR_TITANIUM_ORE,
				CygnusBlocks.LUNAR_DEEPSLATE_TITANIUM_ORE,
//				CygnusBlocks.MARTIAN_TITANIUM_ORE,
//				CygnusBlocks.MARTIAN_DEEPSLATE_TITANIUM_ORE,
//				CygnusBlocks.MERCURIAL_TITANIUM_ORE,
//				CygnusBlocks.MERCURIAL_DEEPSLATE_TITANIUM_ORE,
//				CygnusBlocks.VENUSIAN_TITANIUM_ORE,
//				CygnusBlocks.VENUSIAN_DEEPSLATE_TITANIUM_ORE,
			),
		)
        // endregion Items/Resources

        // region Items/Ingredients
        CygnusItems.ELECTRONIC_CIRCUIT.shapedRecipeBuilder().apply {
            pattern(" Q ")
            pattern("CCC")
            pattern(" S ")
            define('Q', cTag("gems/quartz"))
            define('C', cTag("ingots/copper"))
            define('S', CygnusTags.Items.PLATES_STEEL)
            unlockWith(Items.QUARTZ)
            save(id("shaped/electronic_circuit"))
        }

        CygnusItems.CHRONITE_CIRCUIT.shapedRecipeBuilder().apply {
            pattern(" C ")
            pattern("TTT")
            pattern(" E ")
            define('T', CygnusTags.Items.PLATES_TITANIUM)
            define('C', CygnusItems.CHRONITE_SHARD)
            define('E', CygnusItems.ELECTRONIC_CIRCUIT)
            unlockWith(Items.QUARTZ)
            save(id("shaped/chronite_circuit"))
        }
        // endregion Items/Ingredients

		// region Items/Tools
		CygnusItems.HAMMER.shapedRecipeBuilder().apply {
			pattern("SSS")
			pattern("SSS")
			pattern(" T ")
			define('S', CygnusTags.Items.INGOTS_STEEL)
			define('T', Items.STICK)
			unlockWith(CygnusItems.STEEL_INGOT)
			save(id("shaped/hammer"))
		}

		CygnusItems.KEY.shapedRecipeBuilder().apply {
			pattern("I")
			pattern("N")
			define('I', cTag("ingots/gold"))
			define('N', cTag("nuggets/gold"))
			unlockWith(Items.GOLD_INGOT)
			save(id("shaped/key"))
		}

		CygnusItems.OXYGEN_DRILL.shapedRecipeBuilder().apply {
			pattern(" S ")
			pattern("SBS")
			pattern("RCR")
			define('S', CygnusTags.Items.PLATES_STEEL)
			define('B', CygnusTags.Items.STORAGE_BLOCKS_ALUMINIUM)
			define('R', CygnusTags.Items.RODS_STEEL)
			define('C', CygnusItems.ELECTRONIC_CIRCUIT)
			unlockWith(CygnusItems.ELECTRONIC_CIRCUIT)
			save(id("shaped/oxygen_drill"))
		}
		// endregion Items/Tools
		// endregion Items

        // region Blocks
        // region Blocks/Decorational
		metalDecorationSet(
			"iron",
			null,
			CygnusItems.IRON_SHEET,
			CygnusBlocks.IRON_SHEET_METAL,
			CygnusBlocks.IRON_CHISELLED,
			CygnusBlocks.IRON_GRATE,
			CygnusBlocks.IRON_CUT,
			CygnusBlocks.IRON_CUT_STAIRS,
			CygnusBlocks.IRON_CUT_SLAB,
			CygnusBlocks.IRON_CUT_PRESSURE_PLATE,
			CygnusBlocks.IRON_CUT_BUTTON,
			CygnusBlocks.IRON_WINDOW,
			CygnusBlocks.IRON_PILLAR,
			null,
			null,
			null,
			CygnusBlocks.IRON_BULB,
		)
		metalDecorationSet(
			"steel",
			CygnusItems.STEEL_INGOT,
			CygnusItems.STEEL_SHEET,
			CygnusBlocks.STEEL_SHEET_METAL,
			CygnusBlocks.STEEL_CHISELLED,
			CygnusBlocks.STEEL_GRATE,
			CygnusBlocks.STEEL_CUT,
			CygnusBlocks.STEEL_CUT_STAIRS,
			CygnusBlocks.STEEL_CUT_SLAB,
			CygnusBlocks.STEEL_CUT_PRESSURE_PLATE,
			CygnusBlocks.STEEL_CUT_BUTTON,
			CygnusBlocks.STEEL_WINDOW,
			CygnusBlocks.STEEL_PILLAR,
			CygnusBlocks.STEEL_BARS,
			CygnusBlocks.STEEL_DOOR,
			CygnusBlocks.STEEL_TRAPDOOR,
			CygnusBlocks.STEEL_BULB,
		)
		metalDecorationSet(
			"aluminum",
			CygnusItems.ALUMINIUM_INGOT,
			CygnusItems.ALUMINIUM_SHEET,
			CygnusBlocks.ALUMINIUM_SHEET_METAL,
			CygnusBlocks.ALUMINIUM_CHISELLED,
			CygnusBlocks.ALUMINIUM_GRATE,
			CygnusBlocks.ALUMINIUM_CUT,
			CygnusBlocks.ALUMINIUM_CUT_STAIRS,
			CygnusBlocks.ALUMINIUM_CUT_SLAB,
			CygnusBlocks.ALUMINIUM_CUT_PRESSURE_PLATE,
			CygnusBlocks.ALUMINIUM_CUT_BUTTON,
			CygnusBlocks.ALUMINIUM_WINDOW,
			CygnusBlocks.ALUMINIUM_PILLAR,
			CygnusBlocks.ALUMINIUM_BARS,
			CygnusBlocks.ALUMINIUM_DOOR,
			CygnusBlocks.ALUMINIUM_TRAPDOOR,
			CygnusBlocks.ALUMINIUM_BULB,
		)
		metalDecorationSet(
			"titanium",
			CygnusItems.TITANIUM_INGOT,
			CygnusItems.TITANIUM_SHEET,
			CygnusBlocks.TITANIUM_SHEET_METAL,
			CygnusBlocks.TITANIUM_CHISELLED,
			CygnusBlocks.TITANIUM_GRATE,
			CygnusBlocks.TITANIUM_CUT,
			CygnusBlocks.TITANIUM_CUT_STAIRS,
			CygnusBlocks.TITANIUM_CUT_SLAB,
			CygnusBlocks.TITANIUM_CUT_PRESSURE_PLATE,
			CygnusBlocks.TITANIUM_CUT_BUTTON,
			CygnusBlocks.TITANIUM_WINDOW,
			CygnusBlocks.TITANIUM_PILLAR,
			CygnusBlocks.TITANIUM_BARS,
			CygnusBlocks.TITANIUM_DOOR,
			CygnusBlocks.TITANIUM_TRAPDOOR,
			CygnusBlocks.TITANIUM_BULB,
		)
        // endregion Blocks/Decorational

		// region Blocks/Environmental
		// endregion Blocks/Environmental

        // region Blocks/Functional
		CygnusBlocks.COMMAND_CENTRE.shapedRecipeBuilder().apply {
			pattern(" S ")
			pattern("SCS")
			pattern("STS")
			define('S', CygnusTags.Items.PLATES_STEEL)
			define('C', CygnusItems.CHRONITE_CIRCUIT)
			define('T', CygnusBlocks.STEEL_CUT)
			unlockWith(CygnusItems.CHRONITE_CIRCUIT)
			save(id("shaped/command_center"))
		}

        CygnusBlocks.TELEPAD.shapedRecipeBuilder().apply {
            pattern(" G ")
            pattern("SCS")
            pattern("STS")
			define('G', cTag("glass_panes/colorless"))
            define('S', CygnusTags.Items.PLATES_STEEL)
            define('C', CygnusItems.CHRONITE_CIRCUIT)
            define('T', CygnusBlocks.STEEL_CUT_SLAB)
            unlockWith(CygnusItems.CHRONITE_CIRCUIT)
            save(id("shaped/telepad"))
        }

        CygnusBlocks.CHRONITE_BLAST_FURNACE.shapedRecipeBuilder().apply {
            pattern("TTT")
            pattern("TBT")
            pattern("DCD")
			define('T', CygnusTags.Items.INGOTS_TITANIUM)
			define('B', Items.BLAST_FURNACE)
            define('D', Items.POLISHED_DEEPSLATE)
            define('C', CygnusItems.CHRONITE_SHARD)
            unlockWith(CygnusItems.CHRONITE_SHARD)
            save(id("shaped/chronite_blast_furnace"))
        }
        // endregion Blocks/Functional
        // endregion Blocks
	}

	fun basicResource(
		id: String,
		raw: ItemLike?,
		nugget: ItemLike?,
		ingot: ItemLike?,
		sheet: ItemLike?,
		rod: ItemLike?,
		block: ItemLike?,
		rawBlock: ItemLike?,
		ores: Set<DeferredBlock<*>>,
	)
	{
		val rawTag = CygnusTags.Items.c("raw_materials/${id}")
		val nuggetTag = CygnusTags.Items.c("nuggets/${id}")
		val ingotTag = CygnusTags.Items.c("ingots/${id}")
		val sheetTag = CygnusTags.Items.c("plates/${id}")

		if (ingot != null) {
			raw?.smeltsTo(ingot)?.apply {
				unlockWith(raw)
				save(id("smelting/${id}_ingot"))
			}
		}

		if (raw != null && rawBlock != null) {
			raw.shapelessRecipeBuilder(9).apply {
				requires(rawBlock)
				unlockWith(rawBlock)
				save(id("shapeless/raw_${id}"))
			}

			rawBlock.shapedRecipeBuilder().apply {
				pattern("TTT")
				pattern("TRT")
				pattern("TTT")
				define('T', rawTag)
				define('R', raw)
				unlockWith(raw)
				save(id("shaped/raw_${id}_block"))
			}
		}

		nugget?.shapelessRecipeBuilder(9)?.apply {
			if (ingot == null)
				throw IllegalStateException("${id}_nuggets_from_ingot needs ingots to be generated using CygnusRecipeProvider#basicResource")
			requires(ingot)
			unlockWith(ingot)
			save(id("shapeless/${id}_nuggets_from_ingot"))
		}

		ingot?.shapedRecipeBuilder()?.apply {
			if (nugget == null)
				throw IllegalStateException("${id}_ingot_from_nuggets needs nuggets to be generated using CygnusRecipeProvider#basicResource")
			pattern("TTT")
			pattern("TNT")
			pattern("TTT")
			define('T', nuggetTag)
			define('N', nugget)
			unlockWith(nugget)
			save(id("shaped/${id}_ingot_from_nuggets"))
		}

		if (block != null && ingot != null) {
			ingot.shapelessRecipeBuilder(9).apply {
				requires(block)
				unlockWith(block)
				save(id("shapeless/${id}_ingot_from_block"))
			}
		}

		sheet?.shapelessRecipeBuilder(2)?.apply {
			requires(CygnusItems.HAMMER)
			requires(Ingredient.of(ingotTag), 2)
			unlockWith(CygnusItems.HAMMER)
			save(id("shapeless/${id}_sheet"))
		}

		rod?.shapelessRecipeBuilder(2)?.apply {
			requires(CygnusItems.HAMMER)
			requires(Ingredient.of(sheetTag), 2)
			unlockWith(CygnusItems.HAMMER)
			save(id("shapeless/${id}_rod"))
		}

		block?.shapedRecipeBuilder()?.apply {
			if (ingot == null)
				throw IllegalStateException("${id}_block needs ingots to be generated using CygnusRecipeProvider#basicResource")
			pattern("TTT")
			pattern("TIT")
			pattern("TTT")
			define('T', ingotTag)
			define('I', ingot)
			unlockWith(ingot)
			save(id("shaped/${id}_block"))
		}

		ores.forEach {
			if (ingot == null)
				throw IllegalStateException("${id}_ingot_from_${it.id.path} needs ingots to be generated using CygnusRecipeProvider#basicResource")
			(it smeltsTo ingot).apply {
				unlockWith(it)
				save(id("smelting/${id}_ingot_from_${it.id.path}"))
			}
		}
	}

	fun metalDecorationSet(
		id: String,
		ingot: ItemLike?,
		sheet: ItemLike,
		sheetMetal: ItemLike?,
		chiseled: ItemLike?,
		grate: ItemLike?,
		cut: ItemLike?,
		cutStairs: ItemLike?,
		cutSlab: ItemLike?,
		cutPressurePlate: ItemLike?,
		cutButton: ItemLike?,
		window: ItemLike?,
		pillar: ItemLike?,
		bars: ItemLike?,
		door: ItemLike?,
		trapDoor: ItemLike?,
		bulb: ItemLike?,
	)
	{
		val ingotTag = CygnusTags.Items.c("ingots/${id}")
		val sheetTag = CygnusTags.Items.c("plates/${id}")

		sheet.shapelessRecipeBuilder(3).apply {
			if (sheetMetal == null)
				throw IllegalStateException("${id}_sheets_from_sheet_metal needs sheet metal blocks to be generated using CygnusRecipeProvider#metalDecorationSet")
			requires(sheetMetal)
			unlockWith(sheetMetal)
			save(id("shapeless/${id}_sheets_from_sheet_metal"))
		}

		sheetMetal?.shapedRecipeBuilder(3)?.apply {
			pattern("TTT")
			pattern("TST")
			pattern("TTT")
			define('T', sheetTag)
			define('S', sheet)
			unlockWith(sheet)
			save(id("shaped/${id}_sheet_metal"))
		}

		chiseled?.shapedRecipeBuilder()?.apply {
			if (cutSlab == null)
				throw IllegalStateException("chiseled_${id} needs a cut slab to be generated using CygnusRecipeProvider#metalDecorationSet")
			pattern("#")
			pattern("#")
			define('#', cutSlab)
			unlockWith(cutSlab)
			save(id("shaped/chiseled_${id}"))
		}

		grate?.shapedRecipeBuilder(4)?.apply {
			pattern("SSS")
			pattern("S S")
			pattern("SSS")
			define('S', sheetTag)
			unlockWith(sheet)
			save(id("shaped/${id}_grate"))
		}

		if(cut != null) {
			cut.shapedRecipeBuilder(2).apply {
				pattern("SS")
				pattern("SS")
				define('S', sheetTag)
				unlockWith(sheet)
				save(id("shaped/cut_${id}"))
			}

			cutStairs?.shapedRecipeBuilder(4)?.apply {
				pattern("C  ")
				pattern("CC ")
				pattern("CCC")
				define('C', cut)
				unlockWith(cut)
				save(id("shaped/cut_${id}_stairs"))
			}

			cutSlab?.shapedRecipeBuilder(6)?.apply {
				pattern("CCC")
				define('C', cut)
				unlockWith(cut)
				save(id("shaped/cut_${id}_slab"))
			}

			cutPressurePlate?.shapedRecipeBuilder(4)?.apply {
				pattern("CC")
				define('C', cut)
				unlockWith(cut)
				save(id("shaped/cut_${id}_pressure_plate"))
			}

			cutButton?.shapelessRecipeBuilder(4)?.apply {
				requires(cut)
				unlockWith(cut)
				save(id("shapeless/cut_${id}_button"))
			}
		}

		window?.shapedRecipeBuilder(2)?.apply {
			pattern("SG")
			pattern("GS")
			define('G', cTag("glass_blocks/colorless"))
			define('S', sheetTag)
			unlockWith(sheet)
			save(id("shaped/${id}_window"))
		}

		pillar?.shapedRecipeBuilder(2)?.apply {
			if (cut == null)
				throw IllegalStateException("${id}_pillar needs a cut block to be generated using CygnusRecipeProvider#metalDecorationSet")
			pattern("#")
			pattern("#")
			define('#', cut)
			unlockWith(cut)
			save(id("shaped/${id}_pillar"))
		}

		bars?.shapedRecipeBuilder(16)?.apply {
			if (ingot == null)
				throw IllegalStateException("${id}_bars needs ingots to be generated using CygnusRecipeProvider#basicResource")
			pattern("TTT")
			pattern("TIT")
			define('T', ingotTag)
			define('I', ingot)
			unlockWith(ingot)
			save(id("shaped/${id}_bars"))
		}

		door?.shapedRecipeBuilder(3)?.apply {
			if (ingot == null)
				throw IllegalStateException("${id}_door needs ingots to be generated using CygnusRecipeProvider#basicResource")
			pattern("TT")
			pattern("TI")
			pattern("TT")
			define('T', ingotTag)
			define('I', ingot)
			unlockWith(ingot)
			save(id("shaped/${id}_door"))
		}

		trapDoor?.shapedRecipeBuilder()?.apply {
			if (ingot == null)
				throw IllegalStateException("${id}_trapdoor needs ingots to be generated using CygnusRecipeProvider#basicResource")
			pattern("TT")
			pattern("TI")
			define('T', ingotTag)
			define('I', ingot)
			unlockWith(ingot)
			save(id("shaped/${id}_trapdoor"))
		}

		bulb?.shapedRecipeBuilder(4)?.apply {
			pattern("SSS")
			pattern("SBS")
			pattern("SRS")
			define('S', sheetTag)
			define('B', Items.BLAZE_ROD)
			define('R', cTag("dusts/redstone"))
			unlockWith(sheet)
			save(id("shaped/${id}_bulb"))
		}
	}

    private fun cTag(path: String) = TagKey.create(Registries.ITEM, ResourceLocation.fromNamespaceAndPath("c", path))
}
