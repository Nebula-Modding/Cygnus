# Planet Renderers

Planet renderers in Cygnus are inspired by the ones from Ad Astra, however with
a variety of tweaks to allow for significantly more customization.

## Spec

Any colour values can be provided in three different formats:

```jsonc
{"r": 255, "g": 150, "b": 100, "a": 255} // RGB(A) object (alpha defaults to 255)
[100, 150, 250, 255] // RGB(A) list (alpha defaults to 255)
"#abcdefff" // RGB(A) hex code (alpha defaults to 255)
14180147 // Packed integers (ARGB)
```

```jsonc
{
  // If a sunset should be rendered for this planet. If you set this to
  // `false`, any skybox objects with a sunset will not render their sunsets
  // (optional, default: true)
  "render_sunset": true,

  // Textures for the environment. Any of these can be set to `null` to disable
  // its respective field. The values provided here are the defaults.
  // If this field is not provided, then everything will use its default value.
  // (optional, default: as seen below)
  "textures": {
    "clouds": "minecraft:environment/clouds",
    "skybox": "minecraft:environment/end_sky",
    "rain": "minecraft:environment/rain",
    "snow": "minecraft:environment/snow"
  },
  
  // Skybox objects. These are used to add planets, stars, or other celestial
  // bodies to the skybox (optional, default: [])
  "skybox_objects": [
    {
      // Skybox objects have layers, which are a list of textures that get
      // rendered on top of each other (required and at least one element
      // inside)
      "layers": [
        {
          // The object's texture (required)
          // Cygnus provides a handful of pre-made ones in environment/star/
          "texture": "cygnus:environment/star/8x8/g_class",

          // The size of the object in the sky (optional, defaults to 30)
          // Note: The sun's scale on Earth is 30.0, it's texture is 32x32
          // (including its backlight)
          "size": 30.0,

          // Local transformations for this layer (optional, defaults to [])
          "transforms": []

          // The texture rotation *relative* to the object's texture rotation
          // (optional, defaults to [0,0,0])
          "texture_rotation": [0,0,0]
        }
      ],

      // A scale to multiple each layer's texture and the backlight texture's
      // size by (optional, defaults to 1)
      "scale": 1.0,

      // A sequence of quaternion rotations/transformations to apply to the
      // object (optional, defaults to [])
      // Note that there is an implicit transform on the Y axis of -90 degrees,
      // this orients the axes so that rotating the X axis can be used for
      // sunrise/sunset.
      "transforms": [
        { "axis": "y", "value": 45 }, // Rotate the object by 45 degrees on the **local** Y axis
        { "axis": "x", "value": 45 }, // Rotate the object by 45 degrees on the local X axis
        // Rotate the object based on the time
        {
          "axis": "x",
          "value": "cygnus:time",
          "args": {
            "multiplier": 2.0 // Make this celestial body go at 2x speed, i.e: it'll orbit the planet twice per day
          }
        }
      ],

      // A rotation for the texture in the sky (optional, defaults to [0,0,0])
      "texture_rotation": [
        0.0, // X rotation
        0.0, // Y rotation
        0.0 // Z rotation
      ],

      // The colour for the sunset of this object, if omitted there will be no
      // sunset for it. This can be applied to any celestial body *and* you can
      // have multiple objects with a sunset! The colours will blend together
      // (optional)
      "sunset_color": 14180147,

      // The backlight for the object, which is a light rendered behind the
      // object. If you don't provide this, the object will have no backlight.
      // (optional, default: null)
      "backlight": {
        // A texture to use for the backlight (optional, default: "cygnus:environment/backlight")
        "texture": "cygnus:environment/backlight",

        // The size of the backlight (required)
        "size": 30.0,

        // The colour of the backlight (required)
        "color" : 16379820
      },

      // If `true`, this skybox object will only be rendered when you're
      // orbiting the planet this renderer is for.
      "is_for_orbit": false,
    }
  ],
  
  // Information regarding stars, if omitted, no stars will be rendered
  "stars": {
    // When the stars are visible. Possible values are ["always", "day",
    // "night", "never"] (optional, defaults to "night")
    "visibility": "always",

    // A curve to determine star brightness. X=0.0=0t and X=1.0=24000t (t is
    // ticks). (optional, defaults to the vanilla star brightness function)
    // The vanilla function is
    // ClientLevel#getStarBrightness(partialTick) * Level#getRainLevel(partialTick)
    "brightness_curve": [
			{"x": 0, "y": 0.50},
			{"x": 0, "y": 1.00},
			{"x": 1, "y": 1.00},
			{"x": 1, "y": 0.50}
    ]

    // How many stars to try to render (optional, defaults to 1500)
    "count": 1500,

    // How much to vary star sizes by (optional, default is 0.1)
    "size_variance": 0.1,

    // A "palette" of different stars that may be rendered (optional, defaults
    // to [{"color":16777215,"weight":1}])
    "palette": [
      {
        // The colour of this star (required)
        "color": 16777215,

        // The size of this star. Note that this is not the final size, for
        // example, the actual size of this star would be `(0.15F + rand.nextFloat() * 0.1F) * 2`
        // (optional, default: 0.15)
        "size": 0.15,

        // The chance that this star is used (as a fraction: `weight/(sum of weights from palette)`)
        // This star would have a 2/3 chance of appearing, since the total
        // weight of this palette is 3 (required)
        "weight": 2
      },
      { "color": 16379820, "weight": 1 }
    ]
  }
}
```
