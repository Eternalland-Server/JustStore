package net.sakuragame.eternal.juststore.api;

import net.sakuragame.eternal.juststore.JustStore;
import net.sakuragame.eternal.juststore.core.shop.ShopCategory;
import org.bukkit.entity.Player;

public class JustStoreAPI {

    public static void openShop(Player player, String shopID) {
        openShop(player, shopID, ShopCategory.Weapon);
    }

    public static void openShop(Player player, String shopID, ShopCategory category) {
        JustStore.getMallManager().openShop(player, shopID, category);
    }
}
