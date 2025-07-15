package top.girlkisser.cygnus.datagen.client

import martian.dapper.api.client.DapperItemModelProvider
import net.neoforged.neoforge.data.event.GatherDataEvent
import top.girlkisser.cygnus.Cygnus
import top.girlkisser.cygnus.content.registry.CygnusBlocks
import top.girlkisser.cygnus.content.registry.CygnusItems

class CygnusItemModelProvider(event: GatherDataEvent) : DapperItemModelProvider(event, Cygnus.MODID)
{
	override fun registerModels()
	{
		CygnusItems.IRON_SHEET.basicModel()
		CygnusItems.IRON_ROD.basicHandheldModel()

		CygnusItems.STEEL_INGOT.basicModel()
		CygnusItems.STEEL_NUGGET.basicModel()
		CygnusItems.STEEL_SHEET.basicModel()
		CygnusItems.STEEL_ROD.basicHandheldModel()

		CygnusItems.RAW_ALUMINIUM.basicModel()
		CygnusItems.ALUMINIUM_NUGGET.basicModel()
		CygnusItems.ALUMINIUM_INGOT.basicModel()
		CygnusItems.ALUMINIUM_SHEET.basicModel()
		CygnusItems.ALUMINIUM_ROD.basicHandheldModel()

		CygnusItems.RAW_TITANIUM.basicModel()
		CygnusItems.TITANIUM_NUGGET.basicModel()
		CygnusItems.TITANIUM_INGOT.basicModel()
		CygnusItems.TITANIUM_SHEET.basicModel()
		CygnusItems.TITANIUM_ROD.basicHandheldModel()

		CygnusItems.CHRONITE_SHARD.basicModel()

		CygnusItems.ELECTRONIC_CIRCUIT.basicModel()
		CygnusItems.CHRONITE_CIRCUIT.basicModel()

		CygnusItems.KEY.basicModel()
		CygnusItems.HAMMER.basicHandheldModel()
        CygnusItems.OXYGEN_DRILL.basicHandheldModel()

		setOf(
			CygnusBlocks.STEEL_BARS,
			CygnusBlocks.ALUMINIUM_BARS,
			CygnusBlocks.TITANIUM_BARS,

			CygnusBlocks.CHRONITE_CLUSTER,
			CygnusBlocks.LARGE_CHRONITE_BUD,
			CygnusBlocks.MEDIUM_CHRONITE_BUD,
			CygnusBlocks.SMALL_CHRONITE_BUD,
		).forEach { it.asItem().addModel(it.id.withPrefix("block/")) }

		setOf(
			CygnusBlocks.IRON_AIRTIGHT_DOOR,
			CygnusBlocks.STEEL_DOOR,
			CygnusBlocks.ALUMINIUM_DOOR,
			CygnusBlocks.TITANIUM_DOOR,
		).forEach { it.asItem().basicModel() }
	}
}
