package top.girlkisser.cygnus.client.screen.terminal;

import net.minecraft.client.Minecraft;
import net.minecraft.client.MouseHandler;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.Rect2i;
import net.minecraft.core.RegistryAccess;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import top.girlkisser.cygnus.Cygnus;
import top.girlkisser.cygnus.api.space.Galaxy;
import top.girlkisser.cygnus.api.space.Planet;
import top.girlkisser.cygnus.api.space.PlanetaryDangerIndex;
import top.girlkisser.cygnus.api.space.Star;
import top.girlkisser.cygnus.client.CygnusClient;
import top.girlkisser.cygnus.client.starmap.StarmapGalaxyConfigLoader;
import top.girlkisser.cygnus.client.starmap.StarmapRenderer;
import top.girlkisser.cygnus.client.starmap.StarmapStarConfigLoader;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Supplier;

public class TerminalStateNavigation implements ITerminalState
{
	public static final int MAP_MIN_X = 45, MAP_MIN_Y = 40, MAP_MAX_X = 216, MAP_MAX_Y = 207;
	public static final int BUTTONS_MIN_X = 13, BUTTONS_MIN_Y = 40, BUTTONS_MAX_X = 43, BUTTONS_MAX_Y = 207;

	public ScreenTerminal<?> screen;
	protected RegistryAccess registryAccess;
	protected List<BiFunction<Integer, Integer, TerminalIconButton>> buttons = new ArrayList<>();
	protected boolean isInitialized = false;

	protected @Nullable ResourceLocation selectedGalaxyId = null;
	protected @Nullable Galaxy selectedGalaxy = null;
	protected @Nullable ResourceLocation selectedStarId = null;
	protected @Nullable Star selectedStar = null;
	// Internally, moons are planets too, so we use a list here to support moons of moons.
	protected List<ResourceLocation> selectedPlanetIdStack = new ArrayList<>();
	protected List<Planet> selectedPlanetStack = new ArrayList<>();

	private int previousMouseX, previousMouseY;
	protected int mapPanX = 0, mapPanY = 0;
	protected float mapZoom = 1f;
	protected float buttonScrollY = 0;

	public TerminalStateNavigation(ScreenTerminal<?> screen)
	{
		this.screen = screen;

		assert Minecraft.getInstance().level != null;
		this.registryAccess = Minecraft.getInstance().level.registryAccess();
	}

	@Override
	public ScreenTerminal<?> screen()
	{
		return screen;
	}

	@Override
	public void init()
	{
		if (!isInitialized)
		{
			listGalaxies();
			isInitialized = true;
		}

		buildButtons();
	}

	public void resetPanAndZoom()
	{
		mapPanX = 0;
		mapPanY = 0;
		mapZoom = 1f;
		buttonScrollY = 0;
	}

	@Override
	public void renderBg(@NotNull GuiGraphics graphics, float partialTick, int mouseX, int mouseY)
	{
		if (isMouseInMap(mouseX, mouseY))
		{
			if (CygnusClient.mouseScrollY != 0)
			{
				mapZoom = Math.clamp(mapZoom + CygnusClient.mouseScrollY > 0 ? 0.05f : -0.05f, 0.1f, 3f);
			}

			if (CygnusClient.isLeftMouseButtonDown)
			{
				if (previousMouseX != mouseX)
					mapPanX -= previousMouseX - mouseX;

				if (previousMouseY != mouseY)
					mapPanY -= previousMouseY - mouseY;
			}
		}
		else if (isMouseInButtonList(mouseX, mouseY) && CygnusClient.mouseScrollY != 0)
		{
			buttonScrollY += (float) ((CygnusClient.mouseScrollY > 0 ? 5 : -5) * screen.getMinecraft().options.mouseWheelSensitivity().get());
			// We can fit 6 buttons on the screen before scrolling, so we can subtract that from the buttons.size()*27
			buttonScrollY = Math.clamp(buttonScrollY, -((this.buttons.size() - 6) * 27) + 3, 0);
			int y = (int)(screen.getGuiTop() + 44 + buttonScrollY);
			for (var renderable : this.screen.renderables)
			{
				if (renderable instanceof TerminalNavigationButton button)
				{
					button.setY(y);
					y += 27;
				}
			}
		}

		StarmapRenderer starmapRenderer = new StarmapRenderer(
			graphics,
			new Rect2i(
				screen.getGuiLeft() + MAP_MIN_X + mapPanX,
				screen.getGuiTop() + MAP_MIN_Y + mapPanY,
				MAP_MAX_X,
				MAP_MAX_Y
			),
			mapZoom
		);

		graphics.enableScissor(
			screen.getGuiLeft() + MAP_MIN_X,
			screen.getGuiTop() + MAP_MIN_Y,
			screen.getGuiLeft() + MAP_MAX_X,
			screen.getGuiTop() + MAP_MAX_Y
		);
		if (!selectedPlanetStack.isEmpty())
		{
			starmapRenderer.highlighted = selectedPlanetIdStack.getLast();
			starmapRenderer.renderStar(selectedStarId, selectedStar, StarmapStarConfigLoader.getRenderConfigOrThrow(selectedStarId), true);
		}
		else if (selectedStar != null)
		{
			starmapRenderer.renderStar(selectedStarId, selectedStar, StarmapStarConfigLoader.getRenderConfigOrThrow(selectedStarId), true);
		}
		else if (selectedGalaxy != null)
		{
			starmapRenderer.renderGalaxy(StarmapGalaxyConfigLoader.getRenderConfigOrThrow(selectedGalaxyId));
		}
		graphics.disableScissor();

		graphics.drawCenteredString(
			Minecraft.getInstance().font,
			String.format("%.2fx", mapZoom),
			screen.getGuiLeft() + MAP_MIN_X + ((MAP_MAX_X - MAP_MIN_Y) / 2),
			screen.getGuiTop() + MAP_MIN_Y + 4,
			0xFF00FF00
		);

		previousMouseX = mouseX;
		previousMouseY = mouseY;
	}

	@Override
	public ResourceLocation getUI()
	{
		return Cygnus.id("textures/gui/terminal_with_left_sidebar.png");
	}

	@Override
	public void handleBackButton()
	{
		resetPanAndZoom();

		if (!selectedPlanetStack.isEmpty())
		{
			selectedPlanetStack.removeLast();
			selectedPlanetIdStack.removeLast();
			if (selectedPlanetStack.isEmpty())
				setList(this::listPlanets);
			else
				setList(this::listMoons);
		}
		else if (selectedStar != null)
		{
			selectedStar = null;
			selectedStarId = null;
			// If the galaxy only has one star, we can skip the star selection menu.
			if (selectedGalaxy != null && selectedGalaxy.stars().size() == 1)
			{
				selectedGalaxy = null;
				selectedGalaxyId = null;
				setList(this::listGalaxies);
			}
			else
			{
				setList(this::listStars);
			}
		}
		else if (selectedGalaxy != null)
		{
			selectedGalaxy = null;
			selectedGalaxyId = null;
			setList(this::listGalaxies);
		}
		else
		{
			screen.setState(TerminalStateMainMenu::new);
		}
	}

	protected boolean isMouseInMap(int mouseX, int mouseY)
	{
		return (
			mouseX >= screen.getGuiLeft() + MAP_MIN_X &&
				mouseX <= screen.getGuiLeft() + MAP_MAX_X &&
				mouseY >= screen.getGuiTop() + MAP_MIN_Y &&
				mouseY <= screen.getGuiTop() + MAP_MAX_Y
		);
	}

	protected boolean isMouseInButtonList(int mouseX, int mouseY)
	{
		return (
			mouseX >= screen.getGuiLeft() + BUTTONS_MIN_X &&
				mouseX <= screen.getGuiLeft() + BUTTONS_MAX_X &&
				mouseY >= screen.getGuiTop() + BUTTONS_MIN_Y &&
				mouseY <= screen.getGuiTop() + BUTTONS_MAX_Y
		);
	}

	protected void buildButtons()
	{
		int y = screen.getGuiTop() + 44;
		for (var buttonBuilder : buttons)
		{
			var button = buttonBuilder.apply(screen.getGuiLeft() + 17, y);
			screen.addRenderableWidget(button);
			y += 27;
		}
	}

	protected void setList(Runnable list)
	{
		buttons.clear();
		this.reinit();

		if (!selectedPlanetStack.isEmpty())
		{
			PlanetaryDangerIndex dangerIndex = selectedPlanetStack.getLast().dangerIndex();
			ResourceLocation icon = switch (dangerIndex) {
				case HABITABLE, SAFE -> TerminalButton.INFO;
				case EUCLID -> TerminalButton.INFO_YELLOW;
				case LETHAL -> TerminalButton.EXCLAIM_RED;
				case NON_LANDABLE -> TerminalButton.INFO_GREY;
			};
			ResourceLocation selected = switch (dangerIndex) {
				case HABITABLE, SAFE, EUCLID, NON_LANDABLE -> TerminalButton.INFO_SELECTED;
				case LETHAL -> TerminalButton.EXCLAIM_SELECTED;
			};
			screen.addRenderableWidget(new TerminalIconButton(
				screen.getGuiLeft() + 190,
				screen.getGuiTop() + 154,
				Component.translatable("screen.cygnus.terminal.info"),
				Supplier::get,
				button -> screen.setState(new TerminalSubStatePlanetSummary(screen, this)),
				icon,
				selected,
				false
			));

			screen.addRenderableWidget(new TerminalIconButton(
				screen.getGuiLeft() + 190,
				screen.getGuiTop() + 181,
				Component.translatable("screen.cygnus.terminal.launch_to", Planet.getName(selectedPlanetIdStack.getLast())),
				Supplier::get,
				button ->
				{
					this.sendTerminalCommand("setdest " + selectedPlanetIdStack.getLast());
					screen.setState(TerminalStateMainMenu::new);
				},
				TerminalButton.LAUNCH,
				TerminalButton.LAUNCH_SELECTED,
				false
			));
		}

		list.run();
		buildButtons();
	}

	protected void listGalaxies()
	{
		for (ResourceLocation id : Galaxy.getGalaxyIds(registryAccess))
			buttons.add((x, y) ->
			{
				Galaxy galaxy = Galaxy.getGalaxyById(registryAccess, id);
				return new TerminalNavigationButton(
					id,
					x,
					y,
					Galaxy.getName(id),
					Supplier::get,
					button ->
					{
						selectedGalaxy = galaxy;
						selectedGalaxyId = id;
						// If the galaxy only has one solar system, we'll pick that one automatically
						if (galaxy.stars().size() == 1)
						{
							selectedStarId = galaxy.stars().getFirst();
							selectedStar = Star.getStarById(registryAccess, selectedStarId);
							resetPanAndZoom();
							this.setList(this::listPlanets);
						}
						else
						{
							this.setList(this::listStars);
						}
					},
					galaxy.terminalIcon(),
					galaxy.terminalIconHover(),
					this
				);
			});
	}

	protected void listStars()
	{
		assert selectedGalaxy != null;
		for (ResourceLocation id : selectedGalaxy.stars())
			buttons.add((x, y) ->
			{
				Star star = Star.getStarById(registryAccess, id);
				return new TerminalNavigationButton(
					id,
					x,
					y,
					Star.getName(id),
					Supplier::get,
					button ->
					{
						selectedStar = star;
						selectedStarId = id;
						resetPanAndZoom();
						this.setList(this::listPlanets);
					},
					star.terminalIcon(),
					star.terminalIconHover(),
					this
				);
			});
	}

	protected void listPlanets()
	{
		assert selectedStar != null;
		for (ResourceLocation id : selectedStar.planets())
			buttons.add((x, y) ->
			{
				Planet planet = Planet.getPlanetByIdOrThrow(registryAccess, id);
				return new TerminalNavigationButton(
					id,
					x,
					y,
					Planet.getName(id),
					Supplier::get,
					button ->
					{
						selectedPlanetStack.add(planet);
						selectedPlanetIdStack.add(id);
						this.setList(this::listMoons);
					},
					planet.terminalIcon(),
					planet.terminalIconHover(),
					this
				);
			});
	}

	protected void listMoons()
	{
		for (ResourceLocation id : selectedPlanetStack.getLast().moons())
			buttons.add((x, y) ->
			{
				Planet moon = Planet.getPlanetByIdOrThrow(registryAccess, id);
				return new TerminalNavigationButton(
					id,
					x,
					y,
					Planet.getName(id),
					Supplier::get,
					button ->
					{
						selectedPlanetStack.add(moon);
						selectedPlanetIdStack.add(id);
						this.setList(this::listMoons);
					},
					moon.terminalIcon(),
					moon.terminalIconHover(),
					this
				);
			});
	}
}
