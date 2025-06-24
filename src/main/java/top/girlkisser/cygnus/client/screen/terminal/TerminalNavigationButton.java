package top.girlkisser.cygnus.client.screen.terminal;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

public class TerminalNavigationButton extends TerminalIconButton
{
	public final ResourceLocation celestialBodyId;
	public final TerminalStateNavigation state;

	protected TerminalNavigationButton(ResourceLocation celestialBodyId, int x, int y, Component hoverMessage, CreateNarration createNarration, OnPress onPress, ResourceLocation icon, ResourceLocation hoverIcon, TerminalStateNavigation state)
	{
		super(x, y, hoverMessage, createNarration, onPress, icon, hoverIcon);
		this.celestialBodyId = celestialBodyId;
		this.state = state;
	}

	@Override
	protected void renderWidget(@NotNull GuiGraphics graphics, int mouseX, int mouseY, float partialTick)
	{
		if (this.isProperlyHoveredOrFocused(mouseX, mouseY))
		{
			if (animationProgress < 1)
				animationProgress += animationSpeed;

			graphics.enableScissor(
				state.screen.getGuiLeft() + TerminalStateNavigation.BUTTONS_MIN_X,
				state.screen.getGuiTop() + TerminalStateNavigation.BUTTONS_MIN_Y,
				state.screen.getGuiLeft() + TerminalStateNavigation.BUTTONS_MAX_X,
				state.screen.getGuiTop() + TerminalStateNavigation.BUTTONS_MAX_Y
			);
			graphics.blitSprite(hoverIcon, this.getX(), this.getY(), 23, 23);
			graphics.disableScissor();

			if (animationProgress > 0)
			{
				int width = (int) (TOOLTIP_ANIMATION_CURVE.sample(animationProgress).y * TOOLTIP_WIDTH);
				int x = renderMessageOnRight ? this.getX() + 24 : this.getX() - 1 - width;
				graphics.blitSprite(TerminalButton.BUTTON_SELECTED, x, this.getY(), width, 23);
				int y = (this.getY() + (this.getY() + this.getHeight()) - 9) / 2 + 1;
				graphics.drawScrollingString(Minecraft.getInstance().font, this.getMessage(), x + 4, x - 4 + width, y, 0xFFFFFFFF);
			}
		}
		else
		{
			if (animationProgress > 0)
				animationProgress -= animationSpeed;

			graphics.enableScissor(
				state.screen.getGuiLeft() + TerminalStateNavigation.BUTTONS_MIN_X,
				state.screen.getGuiTop() + TerminalStateNavigation.BUTTONS_MIN_Y,
				state.screen.getGuiLeft() + TerminalStateNavigation.BUTTONS_MAX_X,
				state.screen.getGuiTop() + TerminalStateNavigation.BUTTONS_MAX_Y
			);
			graphics.blitSprite(icon, this.getX(), this.getY(), 23, 23);
			graphics.disableScissor();

			if (animationProgress > 0)
			{
				int width = (int) (TOOLTIP_ANIMATION_CURVE.sample(animationProgress).y * TOOLTIP_WIDTH);
				int x = renderMessageOnRight ? this.getX() + 24 : this.getX() - 1 - width;
				graphics.blitSprite(TerminalButton.BUTTON, x, this.getY(), width, 23);
				int y = (this.getY() + (this.getY() + this.getHeight()) - 9) / 2 + 1;
				graphics.drawScrollingString(Minecraft.getInstance().font, this.getMessage(), x + 4, x - 4 + width, y, 0xFF00FF00);
			}
		}
	}

	@Override
	public void onClick(double mouseX, double mouseY)
	{
		if (this.isProperlyHoveredOrFocused((int) mouseX, (int) mouseY))
			this.onPress();
	}

	public boolean isProperlyHoveredOrFocused(int mouseX, int mouseY)
	{
		return
			mouseY >= this.state.screen().getGuiTop() + TerminalStateNavigation.BUTTONS_MIN_Y &&
				mouseY <= this.state.screen().getGuiTop() + TerminalStateNavigation.BUTTONS_MAX_Y &&
				mouseX >= this.state.screen().getGuiLeft() + TerminalStateNavigation.BUTTONS_MIN_X &&
				mouseX <= this.state.screen().getGuiLeft() + TerminalStateNavigation.BUTTONS_MAX_X &&
				this.isHoveredOrFocused();
	}
}
