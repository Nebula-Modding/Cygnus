package top.girlkisser.cygnus.client.screen.terminal;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.network.PacketDistributor;
import org.jetbrains.annotations.NotNull;
import top.girlkisser.cygnus.Cygnus;
import top.girlkisser.cygnus.content.network.ServerboundRunTerminalCommand;

public interface ITerminalState
{
	ScreenTerminal<?> screen();

	void init();

	default void renderBg(@NotNull GuiGraphics graphics, float partialTick, int mouseX, int mouseY)
	{
	}

	default ResourceLocation getUI()
	{
		return Cygnus.id("textures/gui/terminal.png");
	}

	default void handleBackButton()
	{
		screen().setState(TerminalStateMainMenu::new);
	}

	default void reinit()
	{
		screen().clearWidgets();
		screen().init();
	}

	default void sendTerminalCommand(String command)
	{
		PacketDistributor.sendToServer(new ServerboundRunTerminalCommand(screen().getMenu().getSpaceStation().player(), command));
	}

	default String getMenuName()
	{
		return screen().getMenu().getSpaceStation().name();
	}
}
