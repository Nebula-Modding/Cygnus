package top.girlkisser.cygnus.client.screen.terminal;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import org.apache.commons.lang3.function.TriFunction;
import org.jetbrains.annotations.NotNull;
import top.girlkisser.cygnus.api.space.Planet;

import java.util.ArrayList;
import java.util.List;

public class TerminalStateStats implements ITerminalState
{
	public static final List<TriFunction<Integer, Integer, TerminalStateStats, Component>> STATS = new ArrayList<>();

	static
	{
		STATS.add((x, y, state) -> Component.translatable("screen.cygnus.terminal.space_station_stats"));
		STATS.add((x, y, state) -> Component.translatable("screen.cygnus.terminal.orbiting", Planet.getName(state.screen.getMenu().getSpaceStation().orbiting()).getString()));
		STATS.add((x, y, state) -> Component.translatable("screen.cygnus.terminal.telepads", state.screen.getMenu().getSpaceStation().telepads().size()));
	}

	public ScreenTerminal<?> screen;

	public TerminalStateStats(ScreenTerminal<?> screen)
	{
		this.screen = screen;
	}

	@Override
	public ScreenTerminal<?> screen()
	{
		return screen;
	}

	@Override
	public void init()
	{
	}

	@Override
	public void renderBg(@NotNull GuiGraphics graphics, float partialTick, int mouseX, int mouseY)
	{
		int y = screen.getGuiTop() + 44;
		int x = screen.getGuiLeft() + 17;
		for (var func : STATS)
		{
			graphics.drawScrollingString(screen.getMinecraft().font, func.apply(x, y, this), x, x + screen.width - 18, y, 0xFF00FF00);
			y += 12;
		}
	}
}
