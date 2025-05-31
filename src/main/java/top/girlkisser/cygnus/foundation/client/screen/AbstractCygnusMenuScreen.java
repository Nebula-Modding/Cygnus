package top.girlkisser.cygnus.foundation.client.screen;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.MenuAccess;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;
import top.girlkisser.cygnus.foundation.menu.AbstractCygnusContainer;

public abstract class AbstractCygnusMenuScreen<T extends AbstractCygnusContainer> extends Screen implements MenuAccess<T>
{
	protected T menu;
	protected int leftPos, topPos, imageWidth, imageHeight;

	protected AbstractCygnusMenuScreen(T menu, Component title)
	{
		super(title);
		this.menu = menu;
	}

	protected abstract ResourceLocation getUI();

	@Override
	public @NotNull T getMenu()
	{
		return menu;
	}

	@Override
	protected void init()
	{
		super.init();
		leftPos = this.width / 2 - this.imageWidth / 2;
		topPos = this.height / 2 - this.imageHeight / 2;
	}

	@Override
	public void renderBackground(@NotNull GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick)
	{
		super.renderBackground(guiGraphics, mouseX, mouseY, partialTick);
		renderBg(guiGraphics, partialTick, mouseX, mouseY);
	}

	protected void renderBg(@NotNull GuiGraphics graphics, float partialTick, int mouseX, int mouseY)
	{
		graphics.blit(getUI(), leftPos, topPos, 0, 0, this.imageWidth, this.imageHeight);
	}

	public int getGuiLeft()
	{
		return leftPos;
	}

	public int getGuiTop()
	{
		return topPos;
	}
}

