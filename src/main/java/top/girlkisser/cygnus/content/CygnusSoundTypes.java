package top.girlkisser.cygnus.content;

import net.minecraft.sounds.SoundEvents;
import net.neoforged.neoforge.common.util.DeferredSoundType;
import top.girlkisser.cygnus.content.registry.CygnusSoundEvents;

public interface CygnusSoundTypes
{
	DeferredSoundType
		METAL_GRATE = new DeferredSoundType(
			1f,
			1.5f,
			CygnusSoundEvents.METAL_GRATE_BREAK,
			CygnusSoundEvents.METAL_GRATE_STEP,
			CygnusSoundEvents.METAL_GRATE_PLACE,
			CygnusSoundEvents.METAL_GRATE_HIT,
			CygnusSoundEvents.METAL_GRATE_FALL
		),
		METAL_WINDOW = new DeferredSoundType(
			1f,
			1.5f,
			CygnusSoundEvents.METAL_BULB_BREAK,
			() -> SoundEvents.STONE_STEP,
			() -> SoundEvents.STONE_PLACE,
			() -> SoundEvents.STONE_HIT,
			() -> SoundEvents.STONE_FALL
		),
		METAL_BULB = new DeferredSoundType(
			1f,
			1.5f,
			CygnusSoundEvents.METAL_BULB_BREAK,
			CygnusSoundEvents.METAL_BULB_STEP,
			CygnusSoundEvents.METAL_BULB_PLACE,
			CygnusSoundEvents.METAL_BULB_HIT,
			CygnusSoundEvents.METAL_BULB_FALL
		);
}
