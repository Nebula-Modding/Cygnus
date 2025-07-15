package top.girlkisser.cygnus.datagen

import martian.dapper.api.add
import martian.dapper.api.addProviders
import net.neoforged.bus.api.SubscribeEvent
import net.neoforged.fml.common.EventBusSubscriber
import net.neoforged.neoforge.data.event.GatherDataEvent
import top.girlkisser.cygnus.Cygnus
import top.girlkisser.cygnus.datagen.client.*
import top.girlkisser.cygnus.datagen.server.*

@EventBusSubscriber(modid = Cygnus.MODID, bus = EventBusSubscriber.Bus.MOD)
object CygnusDataGen {
	@JvmStatic
	@SubscribeEvent
	fun onGatherData(event: GatherDataEvent) = event.addProviders(
		client =
			{
				it.add(CygnusBlockStateProvider(event))
				it.add(CygnusItemModelProvider(event))
				it.add(CygnusSoundProvider(event))
			},
		server =
			{
				it.add(CygnusBlockDropsProvider(event))
				it.add(CygnusBlockTagProvider(event))
				it.add(CygnusFluidTagProvider(event))
				it.add(CygnusItemTagProvider(event))
				it.add(CygnusRecipeProvider(event))
			}
	)
}

