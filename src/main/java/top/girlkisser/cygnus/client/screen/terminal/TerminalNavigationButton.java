package top.girlkisser.cygnus.client.screen.terminal;

import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

public class TerminalNavigationButton extends TerminalIconButton
{
	public final ResourceLocation celestialBodyId;

	protected TerminalNavigationButton(ResourceLocation celestialBodyId, int x, int y, Component hoverMessage, CreateNarration createNarration, OnPress onPress, ResourceLocation icon, ResourceLocation hoverIcon)
	{
		super(x, y, hoverMessage, createNarration, onPress, icon, hoverIcon);
		this.celestialBodyId = celestialBodyId;
	}
}
