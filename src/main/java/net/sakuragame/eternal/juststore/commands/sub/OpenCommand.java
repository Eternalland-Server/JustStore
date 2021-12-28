package net.sakuragame.eternal.juststore.commands.sub;

import com.taylorswiftcn.justwei.commands.sub.SubCommand;
import net.sakuragame.eternal.juststore.JustStore;
import net.sakuragame.eternal.juststore.commands.CommandPerms;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class OpenCommand extends SubCommand {
    @Override
    public String getIdentifier() {
        return "open";
    }

    @Override
    public void perform(CommandSender sender, String[] args) {
        if (args.length < 2) return;

        String s1 = args[0];
        String s2 = args[1];

        Player player = Bukkit.getPlayerExact(s1);
        if (player == null) {
            sender.sendMessage(" §7玩家不在线");
            return;
        }

        if (!JustStore.getMallManager().getShopsKey().contains(s2)) {
            sender.sendMessage(" §7没有该商店");
            return;
        }

        JustStore.getMallManager().openShop(player, s2);
        sender.sendMessage(" §7已为该玩家打开商店");
    }

    @Override
    public boolean playerOnly() {
        return false;
    }

    @Override
    public String getPermission() {
        return CommandPerms.ADMIN.getNode();
    }
}
