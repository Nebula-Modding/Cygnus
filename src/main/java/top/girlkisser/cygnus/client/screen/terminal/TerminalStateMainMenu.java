package top.girlkisser.cygnus.client.screen.terminal;

import net.minecraft.network.chat.Component;
import org.apache.commons.lang3.function.TriFunction;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

public class TerminalStateMainMenu extends AbstractButtonTerminalState
{
	public static List<TriFunction<Integer, Integer, ITerminalState, TerminalButton>> buttons = new ArrayList<>();

	static
	{
		buttons.add((x, y, state) -> new TerminalButton(
			x,
			y,
			Component.translatable("screen.cygnus.terminal.navigation"),
			Supplier::get,
			button -> state.screen().setState(TerminalStateNavigation::new),
			TerminalButton.NAVIGATION,
			TerminalButton.NAVIGATION_SELECTED
		));
		buttons.add((x, y, state) -> new TerminalButton(
			x,
			y,
			Component.translatable("screen.cygnus.terminal.land"),
			Supplier::get,
			button ->
			{
			},
			TerminalButton.LAND,
			TerminalButton.LAND_SELECTED
		));
		buttons.add((x, y, state) -> new TerminalButton(
			x,
			y,
			Component.translatable("screen.cygnus.terminal.terminal"),
			Supplier::get,
			button -> state.screen().setState(TerminalStateConsole::new),
			TerminalButton.TERMINAL,
			TerminalButton.TERMINAL_SELECTED
		));
		buttons.add((x, y, state) -> new TerminalButton(
			x,
			y,
			Component.translatable("screen.cygnus.terminal.comms"),
			Supplier::get,
			button ->
			{
			},
			TerminalButton.COMMS,
			TerminalButton.COMMS_SELECTED
		));
		buttons.add((x, y, state) -> new TerminalButton(
			x,
			y,
			Component.translatable("screen.cygnus.terminal.stats"),
			Supplier::get,
			button -> state.screen().setState(TerminalStateStats::new),
			TerminalButton.STATS,
			TerminalButton.STATS_SELECTED
		));
	}

	public TerminalStateMainMenu(ScreenTerminal<?> screen)
	{
		super(screen);
	}

	protected List<TriFunction<Integer, Integer, ITerminalState, TerminalButton>> getButtons()
	{
		return buttons;
	}

	@Override
	public void handleBackButton()
	{
		screen.onClose();
	}
}
