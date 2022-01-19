package net.sakuragame.eternal.juststore.api;

import net.sakuragame.eternal.juststore.JustStore;
import net.sakuragame.eternal.juststore.ui.ScreenManager;
import org.bukkit.entity.Player;

public class JustStoreAPI {

    public static void openShop(Player player, String shopID) {
        openShop(player, shopID, 0);
    }

    public static void openShop(Player player, String shopID, int category) {
        ScreenManager.openShop(player, shopID, category);
    }
}
