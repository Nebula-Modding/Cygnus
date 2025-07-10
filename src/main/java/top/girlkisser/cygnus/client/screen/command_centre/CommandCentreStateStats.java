package top.girlkisser.cygnus.client.screen.command_centre;

import net.minecraft.network.chat.Component;
import top.girlkisser.cygnus.client.screen.terminal.ScreenTerminal;
import top.girlkisser.cygnus.client.screen.terminal.TerminalStateStats;

public class CommandCentreStateStats extends TerminalStateStats
{
	public CommandCentreStateStats(ScreenTerminal<?> screen)
	{
		super(screen);
	}

	@Override
	public void handleBackButton()
	{
		screen().setState(CommandCentreStateMainMenu::new);
	}

	@Override
	public String getMenuName()
	{
		return screen().getMenu().getSpaceStation() == null ?
			Component.translatable("block.cygnus.command_center").getString() :
			screen().getMenu().getSpaceStation().name();
	}
}
