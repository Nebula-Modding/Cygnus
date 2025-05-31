package top.girlkisser.cygnus.client.screen.terminal;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.ImageButton;
import net.minecraft.client.gui.components.Renderable;
import net.minecraft.client.gui.components.WidgetSprites;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.narration.NarratableEntry;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;
import top.girlkisser.cygnus.content.menu.ContainerTerminal;
import top.girlkisser.cygnus.foundation.client.screen.AbstractCygnusMenuScreen;

import java.util.function.Function;

public class ScreenTerminal extends AbstractCygnusMenuScreen<ContainerTerminal>
{
	private ITerminalState state;

	public ScreenTerminal(ContainerTerminal menu, Component title)
	{
		super(menu, title);
		this.imageHeight = 218;
		this.imageWidth = 229;
		state = new TerminalStateMainMenu(this);
	}

	public void setState(ITerminalState state)
	{
		this.clearWidgets();
		this.clearFocus();
		this.state = state;
		this.init();
	}

	public void setState(Function<ScreenTerminal, ITerminalState> state)
	{
		setState(state.apply(this));
	}

	@Override
	protected void init()
	{
		super.init();
		addRenderableWidget(new ImageButton(
			this.leftPos + 17,
			this.topPos + 12,
			23,
			23,
			new WidgetSprites(TerminalButton.BACK, TerminalButton.BACK_SELECTED),
			button -> state.handleBackButton()
		));
		state.init();
	}

	@Override
	protected ResourceLocation getUI()
	{
		return state.getUI();
	}

	@Override
	protected void renderBg(@NotNull GuiGraphics graphics, float partialTick, int mouseX, int mouseY)
	{
		super.renderBg(graphics, partialTick, mouseX, mouseY);
		graphics.drawString(font, this.menu.spaceStation.name(), this.leftPos + 43, this.topPos + 24, 0xFF00FF00);
		state.renderBg(graphics, partialTick, mouseX, mouseY);
	}

	@Override
	public boolean isPauseScreen() {
		return false;
	}

	// Expose some methods as public so that ITerminalState inheritors can use them
	@Override
	public <T extends GuiEventListener & Renderable & NarratableEntry> @NotNull T addRenderableWidget(@NotNull T widget)
	{
		return super.addRenderableWidget(widget);
	}

	@Override
	public <T extends Renderable> @NotNull T addRenderableOnly(@NotNull T renderable)
	{
		return super.addRenderableOnly(renderable);
	}

	@Override
	public <T extends GuiEventListener & NarratableEntry> @NotNull T addWidget(@NotNull T listener)
	{
		return super.addWidget(listener);
	}

	@Override
	public void clearWidgets()
	{
		super.clearWidgets();
	}
}
