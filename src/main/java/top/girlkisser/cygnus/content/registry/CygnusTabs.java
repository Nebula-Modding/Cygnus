package top.girlkisser.cygnus.content.registry;

import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Items;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import org.jetbrains.annotations.ApiStatus;
import top.girlkisser.cygnus.Cygnus;

@SuppressWarnings("unused")
@ApiStatus.NonExtendable
public interface CygnusTabs
{
	DeferredRegister<CreativeModeTab> R = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, Cygnus.MODID);

	DeferredHolder<CreativeModeTab, CreativeModeTab> MOD_TAB = R.register("tab", () -> CreativeModeTab.builder()
		.icon(() -> CygnusBlocks.TELEPAD.asItem().getDefaultInstance())
		.title(Component.translatable("itemGroup.cygnus"))
		.displayItems((params, output) ->
			CygnusItems.R.getEntries().forEach(item -> output.accept(item.get())))
		.build());
}
