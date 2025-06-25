package top.girlkisser.cygnus.client;

import net.minecraft.util.RandomSource;
import org.jetbrains.annotations.Nullable;
import top.girlkisser.cygnus.api.space.SpaceStation;

public final class CygnusClient
{
	private CygnusClient()
	{
	}

	public static final RandomSource RANDOM = RandomSource.create();

	public static boolean isLeftMouseButtonDown = false;
	public static double mouseScrollX = 0, mouseScrollY = 0;

	// The space station that belongs to the client player, if there is one.
	// This gets synced when:
	// - The player runs a terminal command
	// - The player enters cygnus:space
	// - The player joins the world
	public static @Nullable SpaceStation mySpaceStation;
}
