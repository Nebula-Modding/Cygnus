package top.girlkisser.cygnus.datagen.server

import martian.dapper.api.server.recipe.DapperRecipeProvider
import martian.dapper.api.server.recipe.DapperShapedRecipeUtil.shapedRecipeBuilder
import martian.dapper.api.server.recipe.DapperShapedRecipeUtil.unlockWith
import net.neoforged.neoforge.data.event.GatherDataEvent
import top.girlkisser.cygnus.Cygnus.id
import top.girlkisser.cygnus.content.CygnusTags
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
		// region Items/Tools
		CygnusItems.OXYGEN_DRILL.shapedRecipeBuilder().apply {
			pattern(" S ")
			pattern("SBS")
			pattern("RCR")
			define('S', CygnusTags.Items.PLATES_STEEL)
			define('B', CygnusTags.Items.STORAGE_BLOCKS_ALUMINIUM)
			define('R', CygnusTags.Items.RODS_STEEL)
			define('C', CygnusItems.ELECTRONIC_CIRCUIT)
			unlockWith(CygnusItems.ELECTRONIC_CIRCUIT)
			save(id("shaped/tools/oxygen_drill"))
		}
		// endregion Items/Tools
		// endregion Items
	}
}
