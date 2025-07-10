package top.girlkisser.cygnus.datagen.server

import martian.dapper.api.server.recipe.DapperRecipeProvider
import martian.dapper.api.server.recipe.DapperShapedRecipeUtil.pattern2x2
import martian.dapper.api.server.recipe.DapperShapedRecipeUtil.shapedRecipeBuilder
import martian.dapper.api.server.recipe.DapperShapedRecipeUtil.unlockWith
import martian.dapper.api.server.recipe.DapperShapelessRecipeUtil.shapelessRecipeBuilder
import martian.dapper.api.server.recipe.DapperShapelessRecipeUtil.unlockWith
import martian.dapper.api.server.recipe.DapperSmeltingRecipeUtil.smeltsTo
import martian.dapper.api.server.recipe.DapperSmeltingRecipeUtil.unlockWith
import net.minecraft.core.registries.Registries
import net.minecraft.resources.ResourceLocation
import net.minecraft.tags.TagKey
import net.minecraft.world.item.Item
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
			"steel",
			CygnusItems.STEEL_INGOT,
			CygnusBlocks.STEEL_BLOCK,
			CygnusItems.STEEL_NUGGET,
			CygnusItems.STEEL_SHEET,
			CygnusItems.STEEL_ROD,
			null, // Steel does not have a raw variant... yet >:3
			setOf(),
		)
		basicResource(
			"aluminium",
			CygnusItems.ALUMINIUM_INGOT,
			CygnusBlocks.ALUMINIUM_BLOCK,
			CygnusItems.ALUMINIUM_NUGGET,
			CygnusItems.ALUMINIUM_SHEET,
			CygnusItems.ALUMINIUM_ROD,
			CygnusItems.RAW_ALUMINIUM,
			setOf(
				CygnusBlocks.ALUMINIUM_ORE,
				CygnusBlocks.DEEPSLATE_ALUMINIUM_ORE,
				CygnusBlocks.LUNAR_ALUMINIUM_ORE,
				CygnusBlocks.LUNAR_DEEPSLATE_ALUMINIUM_ORE,
			),
			tagId = "aluminum", // I use `aluminium` (british) in IDs but the tag by convention is `aluminum` (american)
		)
		basicResource(
			"titanium",
			CygnusItems.TITANIUM_INGOT,
			CygnusBlocks.TITANIUM_BLOCK,
			CygnusItems.TITANIUM_NUGGET,
			CygnusItems.TITANIUM_SHEET,
			CygnusItems.TITANIUM_ROD,
			CygnusItems.RAW_TITANIUM,
			setOf(
				CygnusBlocks.TITANIUM_ORE,
				CygnusBlocks.DEEPSLATE_TITANIUM_ORE,
				CygnusBlocks.LUNAR_TITANIUM_ORE,
				CygnusBlocks.LUNAR_DEEPSLATE_TITANIUM_ORE,
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
            pattern(" T ")
            pattern("THT")
            pattern(" C ")
            define('T', CygnusTags.Items.PLATES_TITANIUM)
            define('H', CygnusItems.CHRONITE_SHARD)
            define('C', CygnusItems.ELECTRONIC_CIRCUIT)
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
			"steel",
			CygnusItems.STEEL_INGOT.get(),
			CygnusItems.STEEL_SHEET.get(),
			CygnusTags.Items.INGOTS_STEEL,
			CygnusTags.Items.PLATES_STEEL,
			CygnusBlocks.STEEL_SHEET_METAL,
			CygnusBlocks.STEEL_CUT,
			CygnusBlocks.STEEL_WINDOW,
			CygnusBlocks.STEEL_DOOR,
			CygnusBlocks.STEEL_TRAPDOOR,
			CygnusBlocks.STEEL_GRATE,
			CygnusBlocks.STEEL_PILLAR,
			CygnusBlocks.STEEL_BULB,
			CygnusBlocks.STEEL_BARS,
		)
		metalDecorationSet(
			"aluminium",
			CygnusItems.ALUMINIUM_INGOT.get(),
			CygnusItems.ALUMINIUM_SHEET.get(),
			CygnusTags.Items.INGOTS_ALUMINIUM,
			CygnusTags.Items.PLATES_ALUMINIUM,
			CygnusBlocks.ALUMINIUM_SHEET_METAL,
			CygnusBlocks.ALUMINIUM_CUT,
			null, // CygnusBlocks.ALUMINIUM_WINDOW,
			null, // CygnusBlocks.ALUMINIUM_DOOR,
			null, // CygnusBlocks.ALUMINIUM_TRAPDOOR,
			null, // CygnusBlocks.ALUMINIUM_VENT,
			null, // CygnusBlocks.ALUMINIUM_PILLAR,
			null, // CygnusBlocks.ALUMINIUM_BULB,
			null, // CygnusBlocks.ALUMINIUM_BARS,
		)
		metalDecorationSet(
			"titanium",
			CygnusItems.TITANIUM_INGOT.get(),
			CygnusItems.TITANIUM_SHEET.get(),
			CygnusTags.Items.INGOTS_TITANIUM,
			CygnusTags.Items.PLATES_TITANIUM,
			CygnusBlocks.TITANIUM_SHEET_METAL,
			CygnusBlocks.TITANIUM_CUT,
			CygnusBlocks.TITANIUM_WINDOW,
			null, // CygnusBlocks.TITANIUM_DOOR,
			null, // CygnusBlocks.TITANIUM_TRAPDOOR,
			null, // CygnusBlocks.TITANIUM_VENT,
			null, // CygnusBlocks.TITANIUM_PILLAR,
			null, // CygnusBlocks.TITANIUM_BULB,
			null, // CygnusBlocks.TITANIUM_BARS,
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
			save(id("shaped/command_centre"))
		}

        CygnusBlocks.TELEPAD.shapedRecipeBuilder().apply {
            pattern(" G ")
            pattern("SCS")
            pattern("STS")
            define('S', CygnusTags.Items.PLATES_STEEL)
            define('G', cTag("glass_blocks/colorless"))
            define('C', CygnusItems.CHRONITE_CIRCUIT)
            define('T', CygnusBlocks.STEEL_CUT)
            unlockWith(CygnusItems.CHRONITE_CIRCUIT)
            save(id("shaped/telepad"))
        }

        CygnusBlocks.CHRONITE_BLAST_FURNACE.shapedRecipeBuilder().apply {
            pattern(" A ")
            pattern("AFA")
            pattern("ACA")
            define('A', CygnusTags.Items.INGOTS_ALUMINIUM)
            define('F', Items.BLAST_FURNACE)
            define('C', CygnusItems.CHRONITE_SHARD)
            unlockWith(CygnusItems.CHRONITE_SHARD)
            save(id("shaped/chronite_blast_furnace"))
        }
        // endregion Blocks/Functional
        // endregion Blocks
	}

	fun basicResource(
		id: String,
		ingot: ItemLike,
		block: ItemLike,
		nugget: ItemLike,
		sheet: ItemLike?,
		rod: ItemLike?,
		raw: ItemLike?,
		ores: Set<DeferredBlock<*>>,
		tagId: String? = null
	)
	{
		val ingotTag = CygnusTags.Items.c("ingots/${tagId ?: id}")
		val plateTag = CygnusTags.Items.c("plates/${tagId ?: id}")

		raw?.smeltsTo(ingot)?.apply {
			unlockWith(raw)
			save(id("smelting/${id}_ingot"))
		}

		ingot.shapelessRecipeBuilder(1).apply {
			requires(nugget, 9)
			unlockWith(nugget)
			save(id("shapeless/${id}_ingot_from_nuggets"))
		}

		nugget.shapelessRecipeBuilder(9).apply {
			requires(ingot)
			unlockWith(ingot)
			save(id("shapeless/${id}_nuggets_from_ingot"))
		}

		sheet?.shapelessRecipeBuilder(2)?.apply {
			requires(CygnusItems.HAMMER)
			requires(Ingredient.of(ingotTag), 2)
			unlockWith(CygnusItems.HAMMER)
			save(id("shapeless/${id}_sheet"))
		}

		rod?.shapelessRecipeBuilder(2)?.apply {
			requires(CygnusItems.HAMMER)
			requires(Ingredient.of(plateTag), 2)
			unlockWith(CygnusItems.HAMMER)
			save(id("shapeless/${id}_rod"))
		}

		block.shapelessRecipeBuilder().apply {
			requires(ingot, 9)
			unlockWith(ingot)
			save(id("shapeless/${id}_block"))
		}

		ingot.shapelessRecipeBuilder(9).apply {
			requires(block)
			unlockWith(block)
			save(id("shapeless/${id}_ingot_from_block"))
		}

		ores.forEach {
			(it smeltsTo ingot).apply {
				unlockWith(it)
				save(id("smelting/${id}_ingot_from_${it.id.path}"))
			}
		}
	}

	fun metalDecorationSet(
		id: String,
		ingot: ItemLike,
		sheet: ItemLike,
		ingotTag: TagKey<Item>,
		sheetTag: TagKey<Item>,
		sheetMetal: ItemLike?,
		tiles: ItemLike?,
		window: ItemLike?,
		door: ItemLike?,
		trapDoor: ItemLike?,
		grate: ItemLike?,
		pillar: ItemLike?,
		bulb: ItemLike?,
		bars: ItemLike?,
	)
	{
		sheetMetal?.shapedRecipeBuilder(2)?.apply {
			pattern2x2(sheetTag)
			unlockWith(sheet)
			save(id("shaped/${id}_sheet_metal"))
		}

		tiles?.shapedRecipeBuilder(4)?.apply {
			pattern2x2(sheetMetal ?: sheet)
			unlockWith(sheetMetal ?: sheet)
			save(id("shaped/${id}_tiles"))
		}

		pillar?.shapedRecipeBuilder(2)?.apply {
			if (sheetMetal == null)
				throw IllegalStateException("Pillars need sheet metal blocks to be generated using CygnusRecipeProvider#metalDecorationSet")
			pattern("#")
			pattern("#")
			define('#', sheetMetal)
			unlockWith(sheetMetal)
			save(id("shaped/${id}_pillar"))
		}

		grate?.shapedRecipeBuilder(4)?.apply {
			pattern(" # ")
			pattern("# #")
			pattern(" # ")
			define('#', sheetTag)
			unlockWith(sheet)
			save(id("shaped/${id}_grate"))
		}

		bulb?.shapedRecipeBuilder(4)?.apply {
			pattern(" # ")
			pattern("#B#")
			pattern(" R ")
			define('#', sheetTag)
			define('B', Items.BLAZE_ROD)
			define('R', cTag("dusts/redstone"))
			unlockWith(sheet)
			save(id("shaped/${id}_bulb"))
		}

		window?.shapedRecipeBuilder(4)?.apply {
			pattern(" G ")
			pattern("G#G")
			pattern(" G ")
			define('#', sheetTag)
			define('G', cTag("glass_blocks/colorless"))
			unlockWith(sheet)
			save(id("shaped/${id}_window"))
		}

		door?.shapedRecipeBuilder(3)?.apply {
			pattern("##")
			pattern("##")
			pattern("##")
			define('#', ingotTag)
			unlockWith(ingot)
			save(id("shaped/${id}_door"))
		}

		trapDoor?.shapedRecipeBuilder(2)?.apply {
			pattern2x2(ingotTag)
			unlockWith(ingot)
			save(id("shaped/${id}_trap_door"))
		}

		bars?.shapedRecipeBuilder(16)?.apply {
			pattern("###")
			pattern("###")
			define('#', ingotTag)
			unlockWith(ingot)
			save(id("shaped/${id}_bars"))
		}
	}

    private fun cTag(path: String) = TagKey.create(Registries.ITEM, ResourceLocation.fromNamespaceAndPath("c", path))
}
