package top.girlkisser.cygnus.datagen.server

import martian.dapper.api.server.DapperTagProvider
import net.neoforged.neoforge.data.event.GatherDataEvent
import top.girlkisser.cygnus.Cygnus
import top.girlkisser.cygnus.content.registry.CygnusBlocks.*

class CygnusBlockTagProvider(event: GatherDataEvent) : DapperTagProvider.Companion.Blocks(event, Cygnus.MODID)
{
    override fun addTags()
    {
        // Mineable with an iron pickaxe or above
        val pickaxeMineable = mcTag("mineable/pickaxe")
        val needsIronTool = mcTag("needs_iron_tool")
        addToMultipleTags(
            setOf(pickaxeMineable, needsIronTool),
            STEEL_BLOCK,
            STEEL_TILES,
            STEEL_SHEET_METAL,
            COMMAND_CENTRE,
            TELEPAD
        )
    }
}
