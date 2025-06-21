package top.girlkisser.cygnus.client.screen.terminal;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import top.girlkisser.cygnus.Cygnus;

public class TerminalButton extends Button
{
	public static final ResourceLocation
		// Terminal
		NAVIGATION = Cygnus.id("terminal/navigation"),
		NAVIGATION_SELECTED = Cygnus.id("terminal/navigation_selected"),
		LAND = Cygnus.id("terminal/land"),
		LAND_SELECTED = Cygnus.id("terminal/land_selected"),
		TERMINAL = Cygnus.id("terminal/terminal"),
		TERMINAL_SELECTED = Cygnus.id("terminal/terminal_selected"),
		COMMS = Cygnus.id("terminal/comms"),
		COMMS_SELECTED = Cygnus.id("terminal/comms_selected"),
		STATS = Cygnus.id("terminal/stats"),
		STATS_SELECTED = Cygnus.id("terminal/stats_selected"),
		EXECUTE = Cygnus.id("terminal/execute"),
		EXECUTE_SELECTED = Cygnus.id("terminal/execute_selected"),
		GALAXY = Cygnus.id("terminal/planet"),
		GALAXY_SELECTED = Cygnus.id("terminal/galaxy_selected"),
		STAR = Cygnus.id("terminal/star"),
		STAR_SELECTED = Cygnus.id("terminal/star_selected"),
		PLANET = Cygnus.id("terminal/planet"),
		PLANET_SELECTED = Cygnus.id("terminal/planet_selected"),
		LAUNCH = Cygnus.id("terminal/launch"),
		LAUNCH_SELECTED = Cygnus.id("terminal/launch_selected"),
		EXCLAIM_GREEN = Cygnus.id("terminal/exclaim_green"),
		EXCLAIM_RED = Cygnus.id("terminal/exclaim_red"),
		EXCLAIM_SELECTED = Cygnus.id("terminal/exclaim_selected"),
		INFO = Cygnus.id("terminal/info"),
		INFO_GREY = Cygnus.id("terminal/info_grey"),
		INFO_YELLOW = Cygnus.id("terminal/info_yellow"),
		INFO_RED = Cygnus.id("terminal/info_red"),
		INFO_SELECTED = Cygnus.id("terminal/info_selected"),
		// Command Centre
		CONSTRUCT = Cygnus.id("terminal/construct"),
		CONSTRUCT_SELECTED = Cygnus.id("terminal/construct_selected"),
		// Generic
		BACK = Cygnus.id("terminal/back"),
		BACK_SELECTED = Cygnus.id("terminal/back_selected"),
		BUTTON = Cygnus.id("terminal/button"),
		BUTTON_SELECTED = Cygnus.id("terminal/button_selected"),
		BUTTON_WIDE = Cygnus.id("terminal/button_wide"),
		BUTTON_WIDE_SELECTED = Cygnus.id("terminal/button_wide_selected");

	private static final int width = 143, height = 23;
	private final @Nullable ResourceLocation icon, hoverIcon;

	public TerminalButton(int x, int y, Component message, CreateNarration createNarration, OnPress onPress, @Nullable ResourceLocation icon, @Nullable ResourceLocation hoverIcon)
	{
		super(x, y, width, height, message, onPress, createNarration);
		this.icon = icon;
		this.hoverIcon = hoverIcon;
	}

	@Override
	protected void renderWidget(@NotNull GuiGraphics graphics, int mouseX, int mouseY, float partialTick)
	{
		if (this.isHoveredOrFocused())
		{
			if (hoverIcon == null)
			{
				graphics.blitSprite(BUTTON_WIDE_SELECTED, this.getX(), this.getY(), 119, 23);
				int y = (this.getY() + (this.getY() + this.getHeight()) - 9) / 2 + 1;
				graphics.drawString(Minecraft.getInstance().font, this.getMessage(), this.getX() + 4, y, 0xFFFFFFFF);
			}
			else
			{
				graphics.blitSprite(hoverIcon, this.getX(), this.getY(), 23, 23);
				graphics.blitSprite(BUTTON_SELECTED, this.getX() + 24, this.getY(), 119, 23);
				int y = (this.getY() + (this.getY() + this.getHeight()) - 9) / 2 + 1;
				graphics.drawString(Minecraft.getInstance().font, this.getMessage(), this.getX() + 28, y, 0xFFFFFFFF);
			}
		}
		else
		{
			if (icon == null)
			{
				graphics.blitSprite(BUTTON_WIDE, this.getX(), this.getY(), 119, 23);
				int y = (this.getY() + (this.getY() + this.getHeight()) - 9) / 2 + 1;
				graphics.drawString(Minecraft.getInstance().font, this.getMessage(), this.getX() + 4, y, 0xFF00FF00);
			}
			else
			{
				graphics.blitSprite(icon, this.getX(), this.getY(), 23, 23);
				graphics.blitSprite(BUTTON, this.getX() + 24, this.getY(), 119, 23);
				int y = (this.getY() + (this.getY() + this.getHeight()) - 9) / 2 + 1;
				graphics.drawString(Minecraft.getInstance().font, this.getMessage(), this.getX() + 28, y, 0xFF00FF00);
			}
		}
	}

	@Override
	public void renderString(@NotNull GuiGraphics graphics, @NotNull Font font, int color)
	{
		int y = (this.getY() + (this.getY() + this.getHeight()) - 9) / 2 + 1;
		graphics.drawString(font, this.getMessage(), this.getX() + 28, y, color);
	}
}
