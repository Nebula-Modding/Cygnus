package top.girlkisser.cygnus.client.screen.terminal;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;
import top.girlkisser.lazuli.api.mathematics.CubicBezier;

public class TerminalIconButton extends Button
{
	public static final CubicBezier TOOLTIP_ANIMATION_CURVE = new CubicBezier(.4f, 0f, .6f, 1f); // ease-in-out
	public static final int SIZE = 23;
	public static final int TOOLTIP_WIDTH = 119;
	protected final ResourceLocation icon, hoverIcon;

	public boolean renderMessageOnRight = true;
	protected float animationProgress = 0f;
	protected float animationSpeed = 0.05f;

	public TerminalIconButton(int x, int y, Component hoverMessage, CreateNarration createNarration, OnPress onPress, ResourceLocation icon, ResourceLocation hoverIcon)
	{
		super(x, y, SIZE, SIZE, hoverMessage, onPress, createNarration);
		this.icon = icon;
		this.hoverIcon = hoverIcon;
	}

	public TerminalIconButton(int x, int y, Component hoverMessage, CreateNarration createNarration, OnPress onPress, ResourceLocation icon, ResourceLocation hoverIcon, boolean renderMessageOnRight)
	{
		this(x, y, hoverMessage, createNarration, onPress, icon, hoverIcon);
		this.renderMessageOnRight = renderMessageOnRight;
	}

	@Override
	protected void renderWidget(@NotNull GuiGraphics graphics, int mouseX, int mouseY, float partialTick)
	{
		if (this.isHoveredOrFocused())
		{
			if (animationProgress < 1)
				animationProgress += animationSpeed;

			graphics.blitSprite(hoverIcon, this.getX(), this.getY(), 23, 23);
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

			graphics.blitSprite(icon, this.getX(), this.getY(), 23, 23);
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
}
