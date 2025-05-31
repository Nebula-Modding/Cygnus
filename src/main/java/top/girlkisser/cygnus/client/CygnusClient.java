package top.girlkisser.cygnus.client;

import net.minecraft.client.Minecraft;
import net.minecraft.util.RandomSource;

public class CygnusClient
{
	public static final RandomSource RANDOM = RandomSource.create();

	public static int clientTicks = 0;

	public static boolean isLeftMouseButtonDown = false;
	public static double mouseScrollX = 0, mouseScrollY = 0;

	public static boolean isGameActive()
	{
		return !(Minecraft.getInstance().level == null || Minecraft.getInstance().player == null) && !Minecraft.getInstance().isPaused();
	}
}
