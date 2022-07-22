package net.sakuragame.eternal.juststore.commands.sub;

import com.taylorswiftcn.justwei.commands.sub.SubCommand;
import net.sakuragame.eternal.juststore.JustStore;
import net.sakuragame.eternal.juststore.api.JustStoreAPI;
import net.sakuragame.eternal.juststore.commands.CommandPerms;
import net.sakuragame.eternal.juststore.file.sub.ConfigFile;
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
            sender.sendMessage(ConfigFile.prefix + "玩家不在线");
            return;
        }

        if (!JustStore.getShopManger().getMerchantIDs().contains(s2)) {
            sender.sendMessage(ConfigFile.prefix + "没有该商店");
            return;
        }

        JustStoreAPI.openMerchant(player, s2);
        sender.sendMessage(ConfigFile.prefix + "已为该玩家打开商店");
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
