package top.girlkisser.cygnus.content.terminal;

import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import org.jetbrains.annotations.Nullable;
import top.girlkisser.cygnus.api.space.SpaceStation;

import java.util.HashMap;
import java.util.Map;

public interface ITerminalCommand
{
	Map<String, ITerminalCommand> COMMANDS = new HashMap<>();

	int getArgCount();

	ExecuteResult execute(String[] args, ServerPlayer executor, SpaceStation spaceStation);

	record ExecuteResult(ExecuteStatus status, @Nullable Component message)
	{
	}

	enum ExecuteStatus
	{
		OK,
		ERR,
	}

	static ExecuteResult execute(String command, ServerPlayer executor, SpaceStation spaceStation)
	{
		String[] s = command.split(" ", 2);
		if (!COMMANDS.containsKey(s[0]))
			return new ExecuteResult(ExecuteStatus.ERR, Component.translatable("message.cygnus.no_such_command", s[0]));
		ITerminalCommand cmd = COMMANDS.get(s[0]);

		// Parse arguments
		String[] args = new String[cmd.getArgCount()];
		int argIndex = 0;
		StringBuilder buf = new StringBuilder();
		boolean escaped = false;
		boolean inString = false;
		for (int i = 0 ; i < s[1].length() ; i++)
		{
			char c = s[1].charAt(i);
			if (escaped)
			{
				buf.append(c);
				escaped = false;
				continue;
			}

			switch (c)
			{
				case '"':
					inString = !inString;
					break;
				case '\\':
					escaped = true;
					break;
				case ' ':
					if (!inString)
					{
						args[argIndex++] = buf.toString();
						buf = new StringBuilder();
					}
					break;
				default:
					buf.append(c);
					break;
			}
		}

		if (!buf.isEmpty())
		{
			args[argIndex] = buf.toString();
		}

		return cmd.execute(args, executor, spaceStation);
	}
}
