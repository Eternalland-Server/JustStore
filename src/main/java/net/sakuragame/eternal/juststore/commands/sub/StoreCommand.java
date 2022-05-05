package net.sakuragame.eternal.juststore.commands.sub;

import com.taylorswiftcn.justwei.commands.sub.SubCommand;
import net.sakuragame.eternal.juststore.api.JustStoreAPI;
import net.sakuragame.eternal.juststore.commands.CommandPerms;
import net.sakuragame.eternal.juststore.core.store.StoreType;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class StoreCommand extends SubCommand {
    @Override
    public String getIdentifier() {
        return "store";
    }

    @Override
    public void perform(CommandSender sender, String[] args) {
        if (args.length < 1) return;

        Player player = Bukkit.getPlayerExact(args[0]);
        JustStoreAPI.openStore(player, StoreType.Prop.getId());
    }

    @Override
    public boolean playerOnly() {
        return false;
    }

    @Override
    public String getPermission() {
        return CommandPerms.USER.getNode();
    }
}
