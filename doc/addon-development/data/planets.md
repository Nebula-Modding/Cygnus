# Planet Files

Any celestial body that orbits another celestial body should be registered as a planet in Cygnus. This includes but is not limited to planets, dwarf planets, moons, and asteroid moons.

```

```jsonc
{
  {
	"dimension": "minecraft:overworld", // The dimension that the planet leads to. This is optional, and without it you won't be able to land on this body.
	"gravity": 9.807, // The gravity of the body in meters per second squared. (m/s²)
	"has_oxygen": true, // Whether or not there is oxygen on this planet. This controls oxidization and breathability. At some point, there will be a field that determines if the air is safe to breathe independent from oxygen.

  // The day and night length in ticks. Assuming the planet is not tidally locked, use the formula below and use the result as both the day AND night lengths:
  // (L*24000)/2
  // In this formula, L is equal to the solar day length in Earth days. Every Earth day in minecraft is 24000 ticks, so this gets multiplied by it. It is then divided by two, which splits it between day and night.
  // It is recommended to round the length to the nearest whole number.
	"day_length": 12000,
	"night_length": 12000,

	"year_length": 365.2422, // The orbital period in earth days. For moons, this would be the month length.
	"ambient_temperature_curve": [ // The temperature curve in celsius.
		{ "x": 0.00, "y": 15.0 }, // dawn
		{ "x": 0.25, "y": 15.4 }, // midday
		{ "x": 0.50, "y": 14.6 }, // midnight
		{ "x": 1.00, "y": 15.0 } // dawn
	],
	"ambient_radiation": 0, // TBD
	"moons": [ // The moons of the body. You can have as many moons as you please. You can also have moons of moons.
		"cygnus:the_moon",
    "cygnus:deimos",
    "cygnus:phobos",
	],
	"atmospheric_pressure": 1, // The atmosphere's pressure at the surface in Earth atmospheres. A value of 2 would mean it is 2x the pressure of Earth's.
	"danger_index": "habitable" // The danger index, which tells the player what to expect. The danger index values and meanings are listed in the documentation.
}

}
```
