package top.girlkisser.cygnus.foundation.client.particle2d;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;
import org.joml.Vector2f;
import org.joml.Vector2i;

public interface IParticle2D
{
	ResourceLocation texture();

	Vector2f position();

	Vector2i size();

	int lifetime();

	default void render(InstancedParticle2D instance, GuiGraphics graphics, float partialTick)
	{
		graphics.blitSprite(
			texture(),
			(int) position().x,
			(int) position().y,
			size().x,
			size().y
		);
		instance.lifetimeLeft--;
	}
}
