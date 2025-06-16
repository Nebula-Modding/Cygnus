package top.girlkisser.cygnus.content.registry;

import net.minecraft.core.registries.Registries;
import net.minecraft.sounds.SoundEvent;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import top.girlkisser.cygnus.Cygnus;

public interface CygnusSoundEvents
{
	DeferredRegister<SoundEvent> R = DeferredRegister.create(Registries.SOUND_EVENT, Cygnus.MODID);

	private static DeferredHolder<SoundEvent, SoundEvent> reg(String id)
	{
		return R.register(id, () -> SoundEvent.createVariableRangeEvent(Cygnus.id(id)));
	}

	//@formatter:off
	DeferredHolder<SoundEvent, SoundEvent>
		METAL_GRATE_BREAK = reg("block.metal_grate.break"),
		METAL_GRATE_STEP = reg("block.metal_grate.step"),
		METAL_GRATE_PLACE = reg("block.metal_grate.place"),
		METAL_GRATE_HIT = reg("block.metal_grate.hit"),
		METAL_GRATE_FALL = reg("block.metal_grate.fall"),

		METAL_BULB_BREAK = reg("block.metal_bulb.break"),
		METAL_BULB_STEP = reg("block.metal_bulb.step"),
		METAL_BULB_PLACE = reg("block.metal_bulb.place"),
		METAL_BULB_HIT = reg("block.metal_bulb.hit"),
		METAL_BULB_FALL = reg("block.metal_bulb.fall");
	//@formatter:on
}
