package net.sakuragame.eternal.juststore.api;

import net.sakuragame.eternal.juststore.ui.ScreenManager;
import org.bukkit.entity.Player;

public class JustStoreAPI {

    public static void openMerchant(Player player, String shopID) {
        openMerchant(player, shopID, null);
    }

    public static void openMerchant(Player player, String shopID, String shelfID) {
        ScreenManager.openMerchant(player, shopID, shelfID);
    }
}
