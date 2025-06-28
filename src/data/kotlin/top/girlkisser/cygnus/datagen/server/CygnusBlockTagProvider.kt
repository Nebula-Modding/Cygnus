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
		val shovelMineable = mcTag("mineable/shovel")

		// Pickaxe mineable with any tier
		pickaxeMineable.add(
			LUNAR_STONE,
			LUNAR_COBBLESTONE,
			SMOOTH_LUNAR_STONE,
			CHISELED_LUNAR_STONE,
			LUNAR_STONE_BRICKS,
			CRACKED_LUNAR_STONE_BRICKS,
			LUNAR_STONE_PILLAR,
			LUNAR_DEEPSLATE,
			COBBLED_LUNAR_DEEPSLATE,
			SMOOTH_LUNAR_DEEPSLATE,
			CHISELED_LUNAR_DEEPSLATE,
			LUNAR_DEEPSLATE_BRICKS,
			CRACKED_LUNAR_DEEPSLATE_BRICKS,
			LUNAR_DEEPSLATE_PILLAR,
			LUNAR_COAL_ORE,
			LUNAR_DEEPSLATE_COAL_ORE,
			CHRONITE_BLOCK,
			CHRONITE_CLUSTER,
			BUDDING_CHRONITE,
			SMALL_CHRONITE_BUD,
			MEDIUM_CHRONITE_BUD,
			LARGE_CHRONITE_BUD
		)

		// Pickaxe mineable with at least a stone tool
		addToMultipleTags(
			setOf(pickaxeMineable, mcTag("needs_stone_tool")),
			ALUMINIUM_BLOCK,
			ALUMINIUM_SHEET_METAL,
			ALUMINIUM_TILES,
			LUNAR_IRON_ORE,
			LUNAR_DEEPSLATE_IRON_ORE,
			LUNAR_LAPIS_ORE,
			LUNAR_DEEPSLATE_LAPIS_ORE,
			ALUMINIUM_ORE,
			DEEPSLATE_ALUMINIUM_ORE,
			LUNAR_ALUMINIUM_ORE,
			LUNAR_DEEPSLATE_ALUMINIUM_ORE,
		)

		// Pickaxe mineable with at least an iron tool
        addToMultipleTags(
            setOf(pickaxeMineable, mcTag("needs_iron_tool")),
            STEEL_BLOCK,
            STEEL_SHEET_METAL,
            STEEL_PLATING,
			STEEL_GRATE,
			STEEL_WINDOW,
			STEEL_PILLAR,
			STEEL_BARS,
			STEEL_DOOR,
			STEEL_TRAPDOOR,
			STEEL_BULB,

			TITANIUM_BLOCK,
			TITANIUM_TILES,
			TITANIUM_SHEET_METAL,
			TITANIUM_PLATING,
			TITANIUM_WINDOW,

			LUNAR_GOLD_ORE,
			LUNAR_DEEPSLATE_GOLD_ORE,
			LUNAR_REDSTONE_ORE,
			LUNAR_DEEPSLATE_REDSTONE_ORE,
			LUNAR_EMERALD_ORE,
			LUNAR_DEEPSLATE_EMERALD_ORE,
			LUNAR_DIAMOND_ORE,
			LUNAR_DEEPSLATE_DIAMOND_ORE,
			TITANIUM_ORE,
			DEEPSLATE_TITANIUM_ORE,
			LUNAR_TITANIUM_ORE,
			LUNAR_DEEPSLATE_TITANIUM_ORE,

            COMMAND_CENTRE,
            TELEPAD,
            CHRONITE_BLAST_FURNACE
        )

		// Pickaxe mineable with any tier
		shovelMineable.add(LUNAR_REGOLITH)

		STEEL_BLOCK addTo "c:storage_blocks/steel"
		ALUMINIUM_BLOCK addTo "c:storage_blocks/aluminum"
		TITANIUM_BLOCK addTo "c:storage_blocks/titanium"
    }
}
