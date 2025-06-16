package top.girlkisser.cygnus.datagen.client

import net.minecraft.sounds.SoundEvent
import net.neoforged.neoforge.common.data.SoundDefinition
import net.neoforged.neoforge.common.data.SoundDefinitionsProvider
import net.neoforged.neoforge.data.event.GatherDataEvent
import top.girlkisser.cygnus.Cygnus
import top.girlkisser.cygnus.content.registry.CygnusSoundEvents
import java.util.Arrays

class CygnusSoundProvider(event: GatherDataEvent) :
	SoundDefinitionsProvider(event.generator.packOutput, Cygnus.MODID, event.existingFileHelper)
{
	override fun registerSounds()
	{
		addWithVariants(CygnusSoundEvents.METAL_GRATE_BREAK.get(), "block.generic.break", "block/metal_grate/break", 4)
		addWithVariants(CygnusSoundEvents.METAL_GRATE_STEP.get(), "block.generic.step", "block/metal_grate/step", 6)
		addWithVariants(CygnusSoundEvents.METAL_GRATE_PLACE.get(), "block.generic.place", "block/metal_grate/break", 4)
		addWithVariants(CygnusSoundEvents.METAL_GRATE_HIT.get(), "block.generic.hit", "block/metal_grate/step", 6)
		addWithVariants(CygnusSoundEvents.METAL_GRATE_FALL.get(), "block.generic.fall", "block/metal_grate/step", 6)

		addWithVariants(CygnusSoundEvents.METAL_BULB_BREAK.get(), "block.generic.break", "block/metal_bulb/break", 4)
		addWithVariants(CygnusSoundEvents.METAL_BULB_STEP.get(), "block.generic.step", "block/metal_bulb/step", 6)
		addWithVariants(CygnusSoundEvents.METAL_BULB_PLACE.get(), "block.generic.place", "block/metal_bulb/place", 4)
		addWithVariants(CygnusSoundEvents.METAL_BULB_HIT.get(), "block.generic.hit", "block/metal_bulb/step", 6)
		addWithVariants(CygnusSoundEvents.METAL_BULB_FALL.get(), "block.generic.fall", "block/metal_bulb/step", 6)
	}

	fun addWithVariants(se: SoundEvent, subtitleId: String, idPrefix: String, variantCount: Int)
	{
		add(se, subtitled(subtitleId).apply {
			for (i in 1 .. variantCount)
				with(modSound("$idPrefix$i"))
		})
	}

	fun subtitled(id: String): SoundDefinition = definition().subtitle("subtitles.$id")

	fun modSound(id: String): SoundDefinition.Sound = sound(Cygnus.id(id))
	fun mcSound(id: String): SoundDefinition.Sound = sound(Cygnus.id(id))
}
