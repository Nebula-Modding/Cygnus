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
		CygnusItems.STEEL_INGOT.basicModel()
		CygnusItems.STEEL_SHEET.basicModel()
		CygnusItems.STEEL_ROD.basicHandheldModel()
		CygnusItems.STEEL_NUGGET.basicModel()

		CygnusItems.RAW_ALUMINIUM.basicModel()
		CygnusItems.ALUMINIUM_INGOT.basicModel()
		CygnusItems.ALUMINIUM_SHEET.basicModel()
		CygnusItems.ALUMINIUM_ROD.basicHandheldModel()
		CygnusItems.ALUMINIUM_NUGGET.basicModel()

		CygnusItems.RAW_TITANIUM.basicModel()
		CygnusItems.TITANIUM_INGOT.basicModel()
		CygnusItems.TITANIUM_SHEET.basicModel()
		CygnusItems.TITANIUM_ROD.basicHandheldModel()
		CygnusItems.TITANIUM_NUGGET.basicModel()

		CygnusItems.CHRONITE_SHARD.basicModel()

		CygnusItems.ELECTRONIC_CIRCUIT.basicModel()
		CygnusItems.CHRONITE_CIRCUIT.basicModel()

		CygnusItems.HAMMER.basicHandheldModel()
		CygnusItems.KEY.basicHandheldModel()
        CygnusItems.OXYGEN_DRILL.basicHandheldModel()

		setOf(
			CygnusBlocks.CHRONITE_CLUSTER,
			CygnusBlocks.LARGE_CHRONITE_BUD,
			CygnusBlocks.MEDIUM_CHRONITE_BUD,
			CygnusBlocks.SMALL_CHRONITE_BUD,
			CygnusBlocks.STEEL_BARS,
		).forEach { it.asItem().addModel(it.id.withPrefix("block/")) }

		setOf(
			CygnusBlocks.STEEL_DOOR,
		).forEach { it.asItem().basicModel() }
	}
}
