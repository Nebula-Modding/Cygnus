package top.girlkisser.cygnus;

import com.mojang.logging.LogUtils;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.neoforge.common.NeoForge;
import org.slf4j.Logger;
import top.girlkisser.cygnus.config.CygnusClientConfig;
import top.girlkisser.cygnus.config.CygnusServerConfig;
import top.girlkisser.cygnus.client.CygnusClientListeners;
import top.girlkisser.cygnus.content.registry.*;
import top.girlkisser.cygnus.content.terminal.CommandSetDestination;
import top.girlkisser.cygnus.content.terminal.ITerminalCommand;

@Mod(Cygnus.MODID)
public class Cygnus
{
	public static final String MODID = "cygnus";
	public static final Logger LOGGER = LogUtils.getLogger();

	public Cygnus(IEventBus bus, ModContainer mod, Dist dist)
	{
		// Event listeners
		bus.register(CygnusListeners.ModEventListeners.class);
		NeoForge.EVENT_BUS.register(CygnusListeners.GameEventListeners.class);
		if (dist.isClient())
		{
			bus.register(CygnusClientListeners.ModEventListeners.class);
			NeoForge.EVENT_BUS.register(CygnusClientListeners.GameEventListeners.class);
		}

		// Registration
		CygnusBlocks.R.register(bus);
		CygnusBlockEntityTypes.R.register(bus);
		CygnusDataComponents.R.register(bus);
		CygnusEntityTypes.R.register(bus);
		CygnusFluids.R.register(bus);
		CygnusFluidTypes.R.register(bus);
		CygnusItems.R.register(bus);
		CygnusMenuTypes.R.register(bus);
		CygnusParticleTypes.R.register(bus);
		CygnusRecipeSerializers.R.register(bus);
		CygnusRecipeTypes.R.register(bus);
		CygnusSoundEvents.R.register(bus);
		CygnusTabs.R.register(bus);

		// Terminal commands
		ITerminalCommand.COMMANDS.put("setdest", new CommandSetDestination());

		// Register configs
		mod.registerConfig(ModConfig.Type.CLIENT, CygnusClientConfig.SPEC);
		mod.registerConfig(ModConfig.Type.SERVER, CygnusServerConfig.SPEC);
	}

	public static ResourceLocation id(String path)
	{
		return ResourceLocation.fromNamespaceAndPath(MODID, path);
	}

	public static ResourceLocation id(String namespace, String path)
	{
		return ResourceLocation.fromNamespaceAndPath(namespace, path);
	}
}
