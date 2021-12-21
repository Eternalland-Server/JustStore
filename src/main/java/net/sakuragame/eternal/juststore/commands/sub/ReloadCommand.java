package net.sakuragame.eternal.juststore.commands.sub;

import com.taylorswiftcn.justwei.commands.sub.SubCommand;
import net.sakuragame.eternal.juststore.JustStore;
import org.bukkit.command.CommandSender;

public class ReloadCommand extends SubCommand {

    @Override
    public String getIdentifier() {
        return "reload";
    }

    @Override
    public void perform(CommandSender sender, String[] args) {
        JustStore.getInstance().reload();
        sender.sendMessage("reload done!");
    }

    @Override
    public boolean playerOnly() {
        return false;
    }

    @Override
    public String getPermission() {
        return null;
    }
}
