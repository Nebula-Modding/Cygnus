# 1.0.0

> ![NOTE]
> 
> This changelog is in progress, expect changes with new commits!

Yippie the first release :3

**Additions:**

*Generic Additions:*

- Data-driven galaxies, solar systems, and planets (+moons)
- Rudimentary gravity system (actively working on a better one that's more configurable)
- Space stations, which are per-player (todo: per-team)
- Planet renderers, see the [planet renderers documentation](doc/addon-development/resources/planet_renderers.md)
  for more information.
- `cygnus:space` dimension effects for planet renderers and other fancy effects.

*Celestial Body Additions:*

- Milky Way
  - The Sun
    - Earth
      - The Moon

*Blocks Additions:*

- Chronite Blast Furnace: Smelt alloys using Chronite (or other items if added to the `cygnus:chronite_blast_furnace_fuels` data map)
- Command Centre: Create and manage your space station
- Terminal: Manage your space station whilst on-board
- Telepad: Teleport yourself between your space station and a planet
- Steel block set
- Lunar stone and deepslate block set
- Chronite block set

*Items Additions:*

- Oxygen Drill: A simple mining drill tool that uses oxygen gas as fuel
- A variety of different resources and ingredients:
  - Steel/Aluminium/Titanium Ingot/Sheet/Rod
  - Chronite
  - Electronic/Chronite Circuit

*Worldgen Additions:*

- Chronite Geodes, which spawn on Earth and on the Moon

**Nerd Notes:**

- Space stations are per-player and currently do not support teams. I will look into supporting a teams mod (or multiple) in the near future,
- Chronite Blast Furnace fuels and burn times can be tweaked using the `cygnus:chronite_blast_furnace_fuels` data map,
- All celestial bodies (galaxies, stars, planets, and moons) are data-driven. See `src/main/resources/data/cygnus/cygnus/{galaxies,planets,stars}/`.
  - Moons are planets with a different name, just add the planet as a moon to the planet it orbits.
  - Moons can have moons.
  - All planets need a dimension that they associate with.
  - I recommend not using dimensions with exposed void.
    - If the landing beam ends up over void, then the player will be left with a telepad at Y=(dimension's minimum build limit), which will be annoying to get out of.
- Custom space stations can be made by adding a `cygnus:space_station_crafting` recipe:

  ```jsonc
  {
    "type": "cygnus:space_station_crafting",
    // A list of ingredients with amounts
    "ingredients": [
      {
        // A plain ol' ingredient, the same as you would see in a minecraft:shaped_crafting recipe
        "ingredient": { "tag": "c:plates/steel" },
        // How much of this item to need to craft the space station
        "count": 128
      },
      ...
    ],
    // The ID of the structure to build. This one can be found at `src/main/resources/data/cygnus/structure/space_station.nbt`
    "structure": "cygnus:space_station"
  }
  ```

  - You should also add a language entry for `space_station.mod_id.structure_id`, which appears in the Command Centre.
- By default, Cygnus has "zero-gravity controls" to aid in moving around in zero-gravity environments.
  - This can be toggled on or off using the `enableZeroGravityControl` server config entry.
  - I do not recommend disabling this since it will make it possible for players to get stuck in 0G environments and potentially softlock themselves if they don't have a `/home`, `/kill @s`, or some other way of getting back.
- Space stations are not managed by the Terminal block, nor is any space station data stored there. If you accidentally break a Terminal, just replace it, nothing will break!
- The Terminal and Command Centre UIs are really modular, and you can make custom ones too!
  - For the screen, instantiate `ScreenTerminal` and pass a constructor for a class that implements `ITerminalState` as a parameter to the screen.
  - `ITerminalState` implementors should always have a `ClassName(ScreenTerminal)` constructor.
  - For your `ITerminalState`, you will need to implement:
    - `ScreenTerminal screen()`, a getter for the ScreenTerminal.
    - `void init()`, which you can use to add renderable widgets.
    - If your menu is not a terminal menu: `void handleBackButton()` and call `setState(YourMainMenuState::new)`.
    - If your menu does not associate with a space station: `String getMenuName()` and return the name of your menu.
  - For the menu, you can extend+implement `AbstractTerminalContainer<YourBlockEntity>`
  - For references, you can check `src/main/java/top/girlkisser/cygnus/client/screen/{terminal,command_centre}/`
- Space stations **are not synced to clients automatically**. *Your* space station will get sent to the client when:
  - A Terminal or Command Centre are opened
  - You run a terminal command. A clientbound packet will be sent to sync the packet. It's handler will update the current open screen if it's a `ScreenTerminal` and set the screen's space station to the packet's.
  - If you need the space station on the client for another reason, you will need to manually send a clientbound packet. You can get a space station using a `SpaceStationManager`.
  - In the future the client will also be sent a space station packet to render skyboxes while orbiting a planet.
- Chronite has a 2D particle that gets emitted when held in a screen. You can utilize this engine too by adding particles to `top.girlkisser.cygnus.foundation.client.particl2d.ScreenParticleEngine2D.INSTANCE`.
  - I create the particles in a `ScreenEvent.Render.Post` event. See `src/main/java/top/girlkisser/cygnus/CygnusClientListeners$GameEventListeners#postScreenRender`,
  - The visual effect can be disabled using the `enable2dParticles` config option in the client config file.
- You can create *completely* custom dimension effects for your planets by simply changing the `effects` field in the dimension type.
  - You can also extend `SpaceSpecialEffects` to build on top of the existing custom dimension effects.
