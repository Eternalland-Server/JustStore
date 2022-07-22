package net.sakuragame.eternal.juststore.api;

import net.sakuragame.eternal.juststore.core.store.StoreType;
import net.sakuragame.eternal.juststore.ui.ScreenManager;
import org.bukkit.entity.Player;

public class JustStoreAPI {

    public static void openMerchant(Player player, String shopID) {
        openMerchant(player, shopID, null);
    }

    public static void openMerchant(Player player, String shopID, String shelfID) {
        ScreenManager.openShop(player, shopID, shelfID);
    }

    public static void openStore(Player player, int id) {
        StoreType type = StoreType.match(id);
        if (type == null) return;

        ScreenManager.openStore(player, type);
    }
}
