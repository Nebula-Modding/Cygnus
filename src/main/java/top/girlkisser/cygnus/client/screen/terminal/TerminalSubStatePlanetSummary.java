package top.girlkisser.cygnus.client.screen.terminal;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.NotNull;
import top.girlkisser.cygnus.api.space.Planet;

import java.util.ArrayList;
import java.util.List;

public class TerminalSubStatePlanetSummary implements ITerminalState
{
	public final ScreenTerminal<?> screen;
	public final TerminalStateNavigation previousState;
	public final List<Component> components;

	public TerminalSubStatePlanetSummary(ScreenTerminal<?> screen, TerminalStateNavigation previousState)
	{
		this.screen = screen;
		this.previousState = previousState;
		this.components = getComponents();
	}

	public Planet getPlanet()
	{
		return previousState.selectedPlanetStack.getLast();
	}

	public List<Component> getComponents()
	{
		List<Component> components = new ArrayList<>();
		components.add(Component.translatable("screen.cygnus.terminal.danger_index", Component.translatable("screen.cygnus.terminal." + getPlanet().dangerIndex().name().toLowerCase())));
		components.add(Component.literal("----------"));
		components.add(Component.translatable("screen.cygnus.terminal.atmosphere.breathable")); //TODO: When oxygen is implemented, update this to reflect accurately
		components.add(Component.translatable("screen.cygnus.terminal.temperature", getPlanet().getTemperatureAtMidnight(), getPlanet().getTemperatureAtMidday()));
		components.add(Component.translatable("screen.cygnus.terminal.gravity", getPlanet().gravity()));
		//components.add(Component.translatable("screen.cygnus.terminal.water.accessible")); //TODO
		//components.add(Component.translatable("screen.cygnus.terminal.radiation.safe")); //TODO
		//TODO: Additional notes set by resource packs
		return components;
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
		for (var it : components)
		{
			graphics.drawScrollingString(screen.getMinecraft().font, it, x, x + screen.width - 18, y, 0xFF00FF00);
			y += 12;
		}
	}

	@Override
	public void handleBackButton()
	{
		screen.setState(previousState);
		previousState.setList(previousState::listMoons);
	}
}
