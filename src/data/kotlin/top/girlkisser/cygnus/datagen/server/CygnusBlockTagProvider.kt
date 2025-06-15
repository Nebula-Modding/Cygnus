package top.girlkisser.cygnus.datagen.server

import martian.dapper.api.server.DapperTagProvider
import martian.dapper.api.server.tag.DapperTagUtil.add
import net.neoforged.neoforge.data.event.GatherDataEvent
import top.girlkisser.cygnus.Cygnus
import top.girlkisser.cygnus.content.registry.CygnusBlocks.*

class CygnusBlockTagProvider(event: GatherDataEvent) : DapperTagProvider.Companion.Blocks(event, Cygnus.MODID)
{
    override fun addTags()
    {
		val pickaxeMineable = mcTag("mineable/pickaxe")

        addToMultipleTags(
            setOf(pickaxeMineable, mcTag("needs_iron_tool")),
            STEEL_BLOCK,
            STEEL_TILES,
            STEEL_SHEET_METAL,
            COMMAND_CENTRE,
            TELEPAD,
            CHRONITE_BLAST_FURNACE
        )

		pickaxeMineable.add(
			CHRONITE_BLOCK,
			CHRONITE_CLUSTER,
			BUDDING_CHRONITE,
			SMALL_CHRONITE_BUD,
			MEDIUM_CHRONITE_BUD,
			LARGE_CHRONITE_BUD
		)
    }
}
