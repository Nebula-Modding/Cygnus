package top.girlkisser.cygnus.client.screen;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Inventory;
import org.jetbrains.annotations.NotNull;
import top.girlkisser.cygnus.content.menu.ContainerChroniteBlastFurnace;
import top.girlkisser.cygnus.foundation.client.screen.AbstractCygnusContainerScreen;
import top.girlkisser.cygnus.foundation.mathematics.Mathematics;

import static top.girlkisser.cygnus.Cygnus.id;

public class ScreenChroniteBlastFurnace extends AbstractCygnusContainerScreen<ContainerChroniteBlastFurnace>
{
	public static final ResourceLocation UI = id("textures/gui/chronite_blast_furnace.png");
	public static final ResourceLocation LIT_PROGRESS_SPRITE = id("minecraft", "container/furnace/lit_progress");
	private static final ResourceLocation BURN_PROGRESS_SPRITE = id("minecraft", "container/furnace/burn_progress");

	public static final int
		LIT_ICON_X = 47, LIT_ICON_Y = 37,
		PROGRESS_SPRITE_X = 88, PROGRESS_SPRITE_Y = 34;

	public ScreenChroniteBlastFurnace(ContainerChroniteBlastFurnace menu, Inventory playerInventory, Component title)
	{
		super(menu, playerInventory, title);
	}

	@Override
	protected ResourceLocation getUI()
	{
		return UI;
	}

	@Override
	public void renderBg(@NotNull GuiGraphics graphics, float partialTick, int mouseX, int mouseY)
	{
		super.renderBg(graphics, partialTick, mouseX, mouseY);

		if (this.menu.getLitTicksRemaining() > 0)
		{
			int spriteHeight = Mth.ceil((float) this.menu.getLitTicksRemaining() / this.menu.getTotalLitTicksForFuel() * 13.0F) + 1;
			graphics.blitSprite(
				LIT_PROGRESS_SPRITE,
				14,
				14,
				0,
				14 - spriteHeight,
				this.leftPos + LIT_ICON_X,
				this.topPos + LIT_ICON_Y + 14 - spriteHeight,
				14,
				spriteHeight
			);
		}

		int l = Mth.ceil((float) this.menu.getCraftProgress() / this.menu.getCraftDuration() * 24.0F);
		graphics.blitSprite(BURN_PROGRESS_SPRITE, 24, 16, 0, 0, leftPos + PROGRESS_SPRITE_X, topPos + PROGRESS_SPRITE_Y, l, 16);
	}

	@Override
	public void render(@NotNull GuiGraphics graphics, int mouseX, int mouseY, float partialTick)
	{
		super.render(graphics, mouseX, mouseY, partialTick);

		if (this.menu.getLitTicksRemaining() > 0 && Mathematics.pointWithinRectangle(mouseX, mouseY, leftPos + LIT_ICON_X, topPos + LIT_ICON_Y, 14, 14))
		{
			graphics.renderTooltip(
				this.font,
				Component.literal("Burn Ticks: " + this.menu.getLitTicksRemaining() + "/" + menu.getTotalLitTicksForFuel()),
				mouseX,
				mouseY
			);
		}
	}
}
