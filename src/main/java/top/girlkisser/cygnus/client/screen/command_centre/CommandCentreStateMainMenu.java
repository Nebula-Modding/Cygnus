package top.girlkisser.cygnus.client.screen.command_centre;

import net.minecraft.network.chat.Component;
import org.apache.commons.lang3.function.TriFunction;
import top.girlkisser.cygnus.client.screen.terminal.AbstractButtonTerminalState;
import top.girlkisser.cygnus.client.screen.terminal.ITerminalState;
import top.girlkisser.cygnus.client.screen.terminal.ScreenTerminal;
import top.girlkisser.cygnus.client.screen.terminal.TerminalButton;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

public class CommandCentreStateMainMenu extends AbstractButtonTerminalState
{
	public static List<TriFunction<Integer, Integer, ITerminalState, TerminalButton>>
		createNewButtons = new ArrayList<>(),
		buttons = new ArrayList<>();

	static
	{
		createNewButtons.add((x, y, state) -> new TerminalButton(
			x,
			y,
			Component.translatable("screen.cygnus.command_center.construct"),
			Supplier::get,
			button -> state.screen().setState(CommandCentreStateConstruct::new),
			TerminalButton.CONSTRUCT,
			TerminalButton.CONSTRUCT_SELECTED
		));

		buttons.add((x, y, state) -> new TerminalButton(
			x,
			y,
			Component.translatable("screen.cygnus.command_center.stats"),
			Supplier::get,
			button -> state.screen().setState(CommandCentreStateStats::new),
			TerminalButton.STATS,
			TerminalButton.STATS_SELECTED
		));
		buttons.add((x, y, state) -> new TerminalButton(
			x,
			y,
			Component.translatable("screen.cygnus.command_center.retrieve"),
			Supplier::get,
			button ->
			{
				state.sendTerminalCommand("setdest cygnus:earth");
				state.screen().setState(CommandCentreStateMainMenu::new);
			},
			TerminalButton.LAUNCH,
			TerminalButton.LAUNCH_SELECTED
		));
	}

	public CommandCentreStateMainMenu(ScreenTerminal<?> screen)
	{
		super(screen);
	}

	@Override
	public List<TriFunction<Integer, Integer, ITerminalState, TerminalButton>> getButtons()
	{
		return screen.getMenu().getSpaceStation() == null ? createNewButtons : buttons;
	}

	@Override
	public void handleBackButton()
	{
		screen().onClose();
	}

	@Override
	public String getMenuName()
	{
		return screen().getMenu().getSpaceStation() == null ?
			Component.translatable("block.cygnus.command_center").getString() :
			screen().getMenu().getSpaceStation().name();
	}
}
