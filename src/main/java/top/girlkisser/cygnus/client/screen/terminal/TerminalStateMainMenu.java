package top.girlkisser.cygnus.client.screen.terminal;

import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import org.apache.commons.lang3.function.TriFunction;
import top.girlkisser.cygnus.Cygnus;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

public record TerminalStateMainMenu(ScreenTerminal screen) implements ITerminalState
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

	@Override
	public void init()
	{
		int y = screen.getGuiTop() + 44;
		for (var buttonBuilder : buttons)
		{
			var button = buttonBuilder.apply(screen.getGuiLeft() + 17, y, this);
			screen.addRenderableWidget(button);
			y += 27;
		}
	}

	@Override
	public ResourceLocation getUI()
	{
		return Cygnus.id("textures/gui/terminal_with_right_sidebar.png");
	}

	@Override
	public void handleBackButton()
	{
		screen.onClose();
	}
}
