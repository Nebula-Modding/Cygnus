package top.girlkisser.cygnus.client.skybox;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import top.girlkisser.cygnus.foundation.codec.LimitedStringCodec;
import top.girlkisser.cygnus.foundation.collections.WeightedRandomList;
import top.girlkisser.cygnus.foundation.colours.UnpackedColour;
import top.girlkisser.cygnus.foundation.mathematics.CubicBezier;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public record PlanetRenderer(
	boolean renderSunset,
	PlanetRendererTextures textures,
	List<SkyboxObject> objects,
	Optional<StarInfo> stars
)
{
	public static final Codec<PlanetRenderer> CODEC = RecordCodecBuilder.create(it -> it.group(
		Codec.BOOL.optionalFieldOf("render_sunset", true).forGetter(PlanetRenderer::renderSunset),
		PlanetRendererTextures.CODEC.optionalFieldOf("textures", PlanetRendererTextures.DEFAULT).forGetter(PlanetRenderer::textures),
		SkyboxObject.CODEC.listOf().fieldOf("skybox_objects").forGetter(PlanetRenderer::objects),
		StarInfo.CODEC.optionalFieldOf("stars").forGetter(PlanetRenderer::stars)
	).apply(it, PlanetRenderer::new));

	public static final StreamCodec<ByteBuf, PlanetRenderer> STREAM_CODEC = ByteBufCodecs.fromCodec(CODEC);

	public static final PlanetRenderer DEFAULT = new PlanetRenderer(
		true,
		PlanetRendererTextures.DEFAULT,
		new ArrayList<>(),
		Optional.of(StarInfo.DEFAULT)
	);

	public record PlanetRendererTextures(
		Optional<ResourceLocation> clouds,
		Optional<ResourceLocation> skybox,
		Optional<ResourceLocation> rain,
		Optional<ResourceLocation> snow
	)
	{
		public static final Codec<PlanetRendererTextures> CODEC = RecordCodecBuilder.create(it -> it.group(
			ResourceLocation.CODEC.optionalFieldOf("clouds").forGetter(PlanetRendererTextures::clouds),
			ResourceLocation.CODEC.optionalFieldOf("skybox").forGetter(PlanetRendererTextures::skybox),
			ResourceLocation.CODEC.optionalFieldOf("rain").forGetter(PlanetRendererTextures::rain),
			ResourceLocation.CODEC.optionalFieldOf("snow").forGetter(PlanetRendererTextures::snow)
		).apply(it, PlanetRendererTextures::new));

		public static final StreamCodec<ByteBuf, PlanetRendererTextures> STREAM_CODEC = ByteBufCodecs.fromCodec(CODEC);

		public static final PlanetRendererTextures DEFAULT = new PlanetRendererTextures(
			Optional.of(ResourceLocation.withDefaultNamespace("textures/environment/clouds.png")),
			Optional.of(ResourceLocation.withDefaultNamespace("textures/environment/end_sky.png")),
			Optional.of(ResourceLocation.withDefaultNamespace("textures/environment/rain.png")),
			Optional.of(ResourceLocation.withDefaultNamespace("textures/environment/snow.png"))
		);
	}

	public record StarInfo(
		StarVisibility visibility,
		Optional<CubicBezier> brightnessCurve,
		int count,
		WeightedRandomList<StarVariant> palette
	)
	{
		public static final Codec<StarInfo> CODEC = RecordCodecBuilder.create(it -> it.group(
			LimitedStringCodec.enumStringCodec(StarVisibility.values(), StarVisibility::valueOf)
				.optionalFieldOf("visibility", StarVisibility.NIGHT)
				.forGetter(StarInfo::visibility),
			CubicBezier.LIST_CODEC.optionalFieldOf("brightness_curve").forGetter(StarInfo::brightnessCurve),
			Codec.INT.optionalFieldOf("count", 1500).forGetter(StarInfo::count),
			StarVariant.CODEC.listOf()
				.optionalFieldOf("palette", List.of(new StarVariant(new UnpackedColour(16777215), 1)))
				.xmap(
					WeightedRandomList::new,
					to -> to.getEntries().stream().map(WeightedRandomList.Entry::it).toList()
				)
				.forGetter(StarInfo::palette)
		).apply(it, StarInfo::new));

		public static final StreamCodec<ByteBuf, StarInfo> STREAM_CODEC = ByteBufCodecs.fromCodec(CODEC);

		public static final StarInfo DEFAULT = new StarInfo(
			StarVisibility.NIGHT,
			Optional.empty(),
			1500,
			new WeightedRandomList<>(List.of(new StarVariant(new UnpackedColour(16777215), 1)))
		);

		public StarVariant pickVariant(RandomSource random)
		{
			return this.palette.pick(random);
		}

		public enum StarVisibility
		{
			ALWAYS,
			DAY,
			NIGHT,
			NEVER,
		}

		public record StarVariant(UnpackedColour color, int weight) implements WeightedRandomList.IWeighted
		{
			public static final Codec<StarVariant> CODEC = RecordCodecBuilder.create(it -> it.group(
				UnpackedColour.FLEXIBLE_CODEC.fieldOf("color").forGetter(StarVariant::color),
				Codec.INT.fieldOf("weight").forGetter(StarVariant::weight)
			).apply(it, StarVariant::new));

			public static final StreamCodec<ByteBuf, StarVariant> STREAM_CODEC = ByteBufCodecs.fromCodec(CODEC);
		}
	}
}
