package top.girlkisser.cygnus.datagen.server

import martian.dapper.api.server.DapperTagProvider
import net.minecraft.world.item.Items
import net.minecraft.world.level.ItemLike
import net.neoforged.neoforge.data.event.GatherDataEvent
import top.girlkisser.cygnus.Cygnus
import top.girlkisser.cygnus.content.registry.CygnusBlocks.ALUMINIUM_BLOCK
import top.girlkisser.cygnus.content.registry.CygnusBlocks.STEEL_BLOCK
import top.girlkisser.cygnus.content.registry.CygnusBlocks.TITANIUM_BLOCK
import top.girlkisser.cygnus.content.registry.CygnusItems.*

class CygnusItemTagProvider(event: GatherDataEvent) : DapperTagProvider.Companion.Items(event, Cygnus.MODID)
{
    override fun addTags()
    {
        "steel".apply {
			STEEL_NUGGET addNugget this
            STEEL_INGOT addIngot this
            STEEL_SHEET addPlate this
            STEEL_ROD addRod this
        }
        "aluminum".apply {
			RAW_ALUMINIUM addRaw this
			ALUMINIUM_NUGGET addNugget this
            ALUMINIUM_INGOT addIngot this
            ALUMINIUM_SHEET addPlate this
            ALUMINIUM_ROD addRod this
        }
        "titanium".apply {
			RAW_TITANIUM addRaw this
			TITANIUM_NUGGET addNugget this
			TITANIUM_INGOT addIngot this
            TITANIUM_SHEET addPlate this
            TITANIUM_ROD addRod this
        }

		HAMMER addTo "c:tools/hammers"
        OXYGEN_DRILL addTo "c:tools/drills"

		Items.LEATHER_BOOTS addTo "cygnus:can_walk_on_chronite"

		STEEL_BLOCK.asItem() addTo "c:storage_blocks/steel"
		ALUMINIUM_BLOCK.asItem() addTo "c:storage_blocks/aluminum"
		TITANIUM_BLOCK.asItem() addTo "c:storage_blocks/titanium"
    }

	infix fun ItemLike.addRaw(id: String) = (this.asItem() addTo "c:raw_materials/$id")!!
	infix fun ItemLike.addIngot(id: String) = (this.asItem() addTo "c:ingots/$id")!!
    infix fun ItemLike.addPlate(id: String) = (this.asItem() addTo "c:plates/$id")!!
    infix fun ItemLike.addRod(id: String) = (this.asItem() addTo "c:rods/$id")!!
    infix fun ItemLike.addNugget(id: String) = (this.asItem() addTo "c:nuggets/$id")!!
}
