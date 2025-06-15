package top.girlkisser.config;

import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.config.ModConfigEvent;
import net.neoforged.neoforge.common.ModConfigSpec;
import top.girlkisser.cygnus.Cygnus;

@EventBusSubscriber(modid = Cygnus.MODID, bus = EventBusSubscriber.Bus.MOD)
public class CygnusClientConfig
{
	private static final ModConfigSpec.Builder BUILDER = new ModConfigSpec.Builder();

	public static final ModConfigSpec.BooleanValue ENABLE_2D_PARTICLES = BUILDER
		.comment("Enables the 2D particle engine, used for visual effects for certain items.")
		.define("enable2dParticles", true);

	public static final ModConfigSpec SPEC = BUILDER.build();

	public static boolean enable2dParticles;

	@SubscribeEvent
	static void onLoad(final ModConfigEvent event)
	{
		if (event.getConfig().getSpec() != SPEC || event instanceof ModConfigEvent.Unloading)
			return;

		enable2dParticles = ENABLE_2D_PARTICLES.get();
	}
}
