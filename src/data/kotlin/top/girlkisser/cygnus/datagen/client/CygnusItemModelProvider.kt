package top.girlkisser.cygnus.datagen.client

import martian.dapper.api.client.DapperItemModelProvider
import net.neoforged.neoforge.data.event.GatherDataEvent
import top.girlkisser.cygnus.Cygnus
import top.girlkisser.cygnus.content.registry.CygnusItems

class CygnusItemModelProvider(event: GatherDataEvent) : DapperItemModelProvider(event, Cygnus.MODID)
{
	override fun registerModels()
	{
		CygnusItems.STEEL_INGOT.basicModel()
//		CygnusItems.STEEL_SHEET.basicModel()
//		CygnusItems.STEEL_ROD.basicModel()
//		CygnusItems.STEEL_NUGGET.basicModel()

		CygnusItems.ALUMINIUM_INGOT.basicModel()
//		CygnusItems.ALUMINIUM_SHEET.basicModel()
//		CygnusItems.ALUMINIUM_ROD.basicModel()
//		CygnusItems.ALUMINIUM_NUGGET.basicModel()

		CygnusItems.TITANIUM_INGOT.basicModel()
//		CygnusItems.TITANIUM_SHEET.basicModel()
//		CygnusItems.TITANIUM_ROD.basicModel()
//		CygnusItems.TITANIUM_NUGGET.basicModel()

//		CygnusItems.ELECTRONIC_CIRCUIT.basicModel()

		CygnusItems.OXYGEN_DRILL.basicHandheldModel()
	}
}
