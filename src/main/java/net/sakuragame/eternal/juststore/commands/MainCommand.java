package net.sakuragame.eternal.juststore.commands;

import com.taylorswiftcn.justwei.commands.JustCommand;
import net.sakuragame.eternal.juststore.commands.sub.HelpCommand;
import net.sakuragame.eternal.juststore.commands.sub.OpenCommand;
import net.sakuragame.eternal.juststore.commands.sub.ReloadCommand;

public class MainCommand extends JustCommand {

    public MainCommand() {
        super(new HelpCommand());
        register(new OpenCommand());
        register(new ReloadCommand());
    }
}
