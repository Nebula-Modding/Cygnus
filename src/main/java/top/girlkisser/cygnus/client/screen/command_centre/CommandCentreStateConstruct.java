package top.girlkisser.cygnus.client.screen.command_centre;

import com.mojang.datafixers.util.Pair;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.neoforged.neoforge.network.PacketDistributor;
import org.apache.commons.lang3.function.TriFunction;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import top.girlkisser.cygnus.Cygnus;
import top.girlkisser.cygnus.client.screen.terminal.*;
import top.girlkisser.cygnus.content.crafting.RecipeSpaceStationCrafting;
import top.girlkisser.cygnus.content.network.ServerboundAttemptSpaceStationConstruction;
import top.girlkisser.cygnus.content.registry.CygnusRecipeTypes;
import top.girlkisser.cygnus.foundation.container.ContainerUtils;
import top.girlkisser.cygnus.foundation.crafting.CountedIngredient;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

public class CommandCentreStateConstruct extends AbstractButtonTerminalState
{
	protected @Nullable RecipeHolder<RecipeSpaceStationCrafting> selected;
	protected List<Pair<String, Boolean>> ingredientLabels = new ArrayList<>();

	public CommandCentreStateConstruct(ScreenTerminal<?> screen)
	{
		super(screen);
	}

	@Override
	protected List<TriFunction<Integer, Integer, ITerminalState, TerminalButton>> getButtons()
	{
		List<TriFunction<Integer, Integer, ITerminalState, TerminalButton>> buttons = new ArrayList<>();

		var level = Minecraft.getInstance().level;
		assert level != null;
		var recipeManager = level.getRecipeManager();

		var recipes = recipeManager.getAllRecipesFor(CygnusRecipeTypes.SPACE_STATION_CRAFTING.get());
		for (var recipe : recipes)
		{
			buttons.add((x, y, state) -> new TerminalButton(
				x,
				y,
				Component.translatable(recipe.value().structure().toLanguageKey("space_station")),
				Supplier::get,
				button ->
				{
					selected = recipe;
					reinit();
				},
				null,
				null
			));
		}

		return buttons;
	}

	@Override
	public void init()
	{
		if (selected != null)
		{
			ingredientLabels.clear();
			for (CountedIngredient ingredient : selected.value().ingredients())
			{
				int count = ContainerUtils.countItems(screen.getMenu().getPlayerInventory(), ingredient.ingredient());
				var item = ingredient.ingredient().getItems()[0].getDisplayName().getString();
				ingredientLabels.add(Pair.of(ingredient.count() + "x " + item, count >= ingredient.count()));
			}

			screen.addRenderableWidget(new TerminalIconButton(
				screen.getGuiLeft() + 190,
				screen.getGuiTop() + 181,
				Component.translatable("screen.cygnus.command_centre.construct"),
				Supplier::get,
				button ->
				{
					craft();
					screen.setState(CommandCentreStateMainMenu::new);
				},
				TerminalButton.CONSTRUCT,
				TerminalButton.CONSTRUCT_SELECTED,
				false
			));
		}
		else
		{
			super.init();
		}
	}

	@Override
	public void handleBackButton()
	{
		if (selected == null)
		{
			screen().setState(CommandCentreStateMainMenu::new);
		}
		else
		{
			selected = null;
			reinit();
		}
	}

	@Override
	public ResourceLocation getUI()
	{
		return selected != null ?
			Cygnus.id("textures/gui/terminal_with_right_sidebar.png") :
			Cygnus.id("textures/gui/terminal.png");
	}

	@Override
	public void renderBg(@NotNull GuiGraphics graphics, float partialTick, int mouseX, int mouseY)
	{
		super.renderBg(graphics, partialTick, mouseX, mouseY);

		int y = screen().getGuiTop() + 44;
		for (Pair<String, Boolean> label : ingredientLabels)
		{
			graphics.drawString(screen.getMinecraft().font, label.getFirst(), screen.getGuiLeft() + 17, y, label.getSecond() ? 0xFF00FF00 : 0xFFFF0000);
			y += 12;
		}
	}

	@Override
	public String getMenuName()
	{
		return screen().getMenu().getSpaceStation() == null ?
			Component.translatable("block.cygnus.command_centre").getString() :
			screen().getMenu().getSpaceStation().name();
	}

	protected void craft()
	{
		assert selected != null;
		PacketDistributor.sendToServer(new ServerboundAttemptSpaceStationConstruction(selected.id()));
	}
}
