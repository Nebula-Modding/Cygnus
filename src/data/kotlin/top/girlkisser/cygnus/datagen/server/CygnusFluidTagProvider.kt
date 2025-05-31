package top.girlkisser.cygnus.datagen.server

import martian.dapper.api.server.DapperTagProvider
import net.neoforged.neoforge.data.event.GatherDataEvent
import top.girlkisser.cygnus.Cygnus
import top.girlkisser.cygnus.content.registry.CygnusFluids.*

class CygnusFluidTagProvider(event: GatherDataEvent) : DapperTagProvider.Companion.Fluids(event, Cygnus.MODID)
{
    override fun addTags()
    {
        OXYGEN_GAS.addTo("c:oxygen", "c:gaseous")
    }
}
