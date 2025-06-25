package top.girlkisser.cygnus.content.terminal;

import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import top.girlkisser.cygnus.api.space.Planet;
import top.girlkisser.cygnus.api.space.SpaceStation;

public class CommandSetDestination implements ITerminalCommand
{
	@Override
	public int getArgCount()
	{
		return 1;
	}

	@Override
	public ExecuteResult execute(String[] args, ServerPlayer executor, SpaceStation spaceStation)
	{
		var rl = ResourceLocation.parse(args[0]);
		if (Planet.getPlanetById(executor.server.registryAccess(), rl).isEmpty())
			return new ExecuteResult(ExecuteStatus.ERR, Component.translatable("message.cygnus.no_such_planet", args[1]));
		spaceStation.setOrbiting(executor.server, rl);
		return new ExecuteResult(ExecuteStatus.OK, null);
	}
}
