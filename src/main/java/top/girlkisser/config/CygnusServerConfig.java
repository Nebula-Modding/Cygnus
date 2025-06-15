package top.girlkisser.config;

import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.config.ModConfigEvent;
import net.neoforged.neoforge.common.ModConfigSpec;
import top.girlkisser.cygnus.Cygnus;

@EventBusSubscriber(modid = Cygnus.MODID, bus = EventBusSubscriber.Bus.MOD)
public class CygnusServerConfig
{
	private static final ModConfigSpec.Builder BUILDER = new ModConfigSpec.Builder();

	private static final ModConfigSpec.BooleanValue ENABLE_ZERO_GRAVITY_CONTROL = BUILDER
		.comment("Enables zero-gravity controls, when on a planet where gravity = 0 (or in space), this will allow you to descend using shift and ascend using space despite there *technically* not being gravity.")
		.define("enableZeroGravityControl", true);

	public static final ModConfigSpec SPEC = BUILDER.build();

	public static boolean enableZeroGravityControl;

	@SubscribeEvent
	static void onLoad(final ModConfigEvent event)
	{
		if (event.getConfig().getSpec() != SPEC || event instanceof ModConfigEvent.Unloading)
			return;

		enableZeroGravityControl = ENABLE_ZERO_GRAVITY_CONTROL.get();
	}
}
