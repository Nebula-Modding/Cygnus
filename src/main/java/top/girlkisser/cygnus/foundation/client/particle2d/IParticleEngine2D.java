package top.girlkisser.cygnus.foundation.client.particle2d;

import net.minecraft.client.gui.GuiGraphics;

public interface IParticleEngine2D
{
	void addParticle(IParticle2D particle);

	void render(GuiGraphics graphics, float partialTick);

	void clear();
}
