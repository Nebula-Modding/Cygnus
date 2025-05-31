package top.girlkisser.cygnus.client.screen.terminal;

import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.network.chat.Component;
import net.neoforged.neoforge.network.PacketDistributor;
import top.girlkisser.cygnus.content.network.ServerboundRunTerminalCommand;

import java.util.function.Supplier;

public class TerminalStateConsole implements ITerminalState
{
	private final ScreenTerminal screen;
	private EditBox editBox;

	public TerminalStateConsole(ScreenTerminal screen)
	{
		this.screen = screen;
	}

	@Override
	public ScreenTerminal screen()
	{
		return screen;
	}

	@Override
	public void init()
	{
		editBox = new EditBox(this.screen.getMinecraft().font, screen.getGuiLeft() + 17, screen.getGuiTop() + 44, 196, 23, Component.translatable("screen.cygnus.terminal.execute_terminal_command"));
		editBox.setCanLoseFocus(true);
		editBox.setTextColor(0xFF00FF00);
		editBox.setTextColorUneditable(0xFF999999);
		editBox.setBordered(true);
		editBox.setMaxLength(255);
		editBox.setValue("");
		screen.addRenderableWidget(editBox);

		screen.addRenderableWidget(new TerminalButton(
			screen.getGuiLeft() + 17,
			screen.getGuiTop() + 71,
			Component.translatable("screen.cygnus.terminal.execute"),
			Supplier::get,
			this::executeCommandCallback,
			TerminalButton.EXECUTE,
			TerminalButton.EXECUTE_SELECTED
		));
	}

	private void executeCommandCallback(Button button)
	{
		PacketDistributor.sendToServer(new ServerboundRunTerminalCommand(this.screen.getMenu().pos, editBox.getValue()));
	}
}
