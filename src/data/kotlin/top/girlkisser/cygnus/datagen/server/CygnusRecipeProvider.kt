package top.girlkisser.cygnus.datagen.server

import martian.dapper.api.server.recipe.DapperRecipeProvider
import martian.dapper.api.server.recipe.DapperShapedRecipeUtil.pattern2x2
import martian.dapper.api.server.recipe.DapperShapedRecipeUtil.shapedRecipeBuilder
import martian.dapper.api.server.recipe.DapperShapedRecipeUtil.unlockWith
import martian.dapper.api.server.recipe.DapperShapelessRecipeUtil.shapelessRecipeBuilder
import martian.dapper.api.server.recipe.DapperShapelessRecipeUtil.unlockWith
import net.minecraft.core.registries.Registries
import net.minecraft.resources.ResourceLocation
import net.minecraft.tags.TagKey
import net.minecraft.world.item.Items
import net.minecraft.world.item.crafting.Ingredient
import net.neoforged.neoforge.data.event.GatherDataEvent
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
        CygnusItems.STEEL_SHEET.shapelessRecipeBuilder(2).apply {
            requires(CygnusItems.HAMMER)
            requires(Ingredient.of(CygnusTags.Items.INGOTS_STEEL), 2)
            unlockWith(CygnusItems.HAMMER)
            save(id("shapeless/steel_sheet"))
        }

        CygnusItems.ALUMINIUM_SHEET.shapelessRecipeBuilder(2).apply {
            requires(CygnusItems.HAMMER)
            requires(Ingredient.of(CygnusTags.Items.INGOTS_ALUMINIUM), 2)
            unlockWith(CygnusItems.HAMMER)
            save(id("shapeless/aluminium_sheet"))
        }

        CygnusItems.TITANIUM_SHEET.shapelessRecipeBuilder(2).apply {
            requires(CygnusItems.HAMMER)
            requires(Ingredient.of(CygnusTags.Items.INGOTS_TITANIUM), 2)
            unlockWith(CygnusItems.HAMMER)
            save(id("shapeless/titanium_sheet"))
        }

        CygnusItems.STEEL_ROD.shapelessRecipeBuilder(2).apply {
            requires(CygnusItems.HAMMER)
            requires(Ingredient.of(CygnusTags.Items.PLATES_STEEL), 2)
            unlockWith(CygnusItems.HAMMER)
            save(id("shapeless/steel_rod"))
        }

        CygnusItems.ALUMINIUM_ROD.shapelessRecipeBuilder(2).apply {
            requires(CygnusItems.HAMMER)
            requires(Ingredient.of(CygnusTags.Items.PLATES_ALUMINIUM), 2)
            unlockWith(CygnusItems.HAMMER)
            save(id("shapeless/aluminium_rod"))
        }

        CygnusItems.TITANIUM_ROD.shapelessRecipeBuilder(2).apply {
            requires(CygnusItems.HAMMER)
            requires(Ingredient.of(CygnusTags.Items.PLATES_TITANIUM), 2)
            unlockWith(CygnusItems.HAMMER)
            save(id("shapeless/titanium_rod"))
        }
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
            define('H', CygnusItems.CHRONITE)
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
		CygnusBlocks.STEEL_BLOCK.shapedRecipeBuilder().apply {
			pattern2x2(CygnusItems.STEEL_INGOT)
			unlockWith(CygnusItems.STEEL_INGOT)
			save(id("shaped/steel_block"))
		}

        CygnusBlocks.STEEL_TILES.shapedRecipeBuilder(count = 4).apply {
            pattern2x2(CygnusTags.Items.PLATES_STEEL)
            unlockWith(CygnusItems.STEEL_SHEET)
            save(id("shaped/steel_tiles"))
        }

        CygnusBlocks.STEEL_SHEET_METAL.shapedRecipeBuilder(count = 4).apply {
            pattern(" # ")
            pattern("# #")
            pattern(" # ")
            define('#', CygnusTags.Items.PLATES_STEEL)
            unlockWith(CygnusItems.STEEL_INGOT)
            save(id("shaped/steel_sheet_metal"))
        }
        // endregion Blocks/Decorational

		// region Blocks/Environmental
		// endregion Blocks/Environmental

        // region Blocks/Functional
        CygnusBlocks.TELEPAD.shapedRecipeBuilder().apply {
            pattern("SGS")
            pattern("SCS")
            pattern("STS")
            define('S', CygnusTags.Items.PLATES_STEEL)
            define('G', cTag("glass_blocks/colorless"))
            define('C', CygnusItems.CHRONITE_CIRCUIT)
            define('T', CygnusBlocks.STEEL_TILES)
            unlockWith(CygnusItems.CHRONITE_CIRCUIT)
            save(id("shaped/telepad"))
        }

        CygnusBlocks.CHRONITE_BLAST_FURNACE.shapedRecipeBuilder().apply {
            pattern(" A ")
            pattern("AFA")
            pattern("ACA")
            define('A', CygnusTags.Items.INGOTS_ALUMINIUM)
            define('F', Items.BLAST_FURNACE)
            define('C', CygnusItems.CHRONITE)
            unlockWith(CygnusItems.CHRONITE)
            save(id("shaped/chronite_blast_furnace"))
        }
        // endregion Blocks/Functional
        // endregion Blocks
	}

    private fun cTag(path: String) = TagKey.create(Registries.ITEM, ResourceLocation.fromNamespaceAndPath("c", path))
}
