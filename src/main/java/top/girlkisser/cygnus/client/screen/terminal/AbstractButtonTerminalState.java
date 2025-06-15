package top.girlkisser.cygnus.client.screen.terminal;

import net.minecraft.resources.ResourceLocation;
import org.apache.commons.lang3.function.TriFunction;
import top.girlkisser.cygnus.Cygnus;

import java.util.List;

public abstract class AbstractButtonTerminalState implements ITerminalState
{
	public ScreenTerminal<?> screen;

	public AbstractButtonTerminalState(ScreenTerminal<?> screen)
	{
		this.screen = screen;
	}

	protected abstract List<TriFunction<Integer, Integer, ITerminalState, TerminalButton>> getButtons();

	@Override
	public ScreenTerminal<?> screen()
	{
		return screen;
	}

	@Override
	public void init()
	{
		int y = screen.getGuiTop() + 44;
		for (var buttonBuilder : getButtons())
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
}
