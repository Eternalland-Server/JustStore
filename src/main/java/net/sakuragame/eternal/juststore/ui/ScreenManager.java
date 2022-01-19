package net.sakuragame.eternal.juststore.ui;

import net.sakuragame.eternal.dragoncore.network.PacketSender;
import net.sakuragame.eternal.justinventory.JustInventory;
import net.sakuragame.eternal.justinventory.ui.UIManager;
import net.sakuragame.eternal.juststore.JustStore;
import net.sakuragame.eternal.juststore.core.StoreManager;
import net.sakuragame.eternal.juststore.core.shop.GoodsShelf;
import net.sakuragame.eternal.juststore.core.shop.Shop;
import net.sakuragame.eternal.juststore.core.store.Store;
import net.sakuragame.eternal.juststore.core.store.StoreType;
import net.sakuragame.eternal.juststore.ui.comp.CategoryComp;
import net.sakuragame.eternal.juststore.ui.comp.CommodityComp;
import net.sakuragame.eternal.juststore.ui.screen.ShopScreen;
import net.sakuragame.eternal.juststore.ui.screen.StoreScreen;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class ScreenManager {

    private final UIManager uiManager;

    public ScreenManager() {
        this.uiManager = JustInventory.getInstance().getUiManager();
    }

    public void init() {
        uiManager.registerUI(new ShopScreen());
        uiManager.registerUI(new StoreScreen());
    }

    public static void openShop(Player player, String shopID) {
        openShop(player, shopID, 0);
    }

    public static void openShop(Player player, String id, int category) {
        UUID uuid = player.getUniqueId();

        Shop shop = JustStore.getStoreManager().getShop(id);
        if (shop == null) return;

        GoodsShelf shelf = shop.getGoodsShelf().get(category);
        if (shelf == null) return;

        if (StoreManager.isCurrentOpenShop(player, id)) {
            CategoryComp categoryComp = new CategoryComp();
            categoryComp.sendCategory(player, shop);
        }

        CommodityComp comp = new CommodityComp();
        comp.sendShopGoods(player, shelf.getGoods());

        Map<String, String> map = new HashMap<>();
        map.put("eternal_shop_name", shop.getName());

        PacketSender.sendSyncPlaceholder(player, map);
        PacketSender.sendRunFunction(player, "default", "global.eternal_shop_category = " + category + ";", false);
        PacketSender.sendOpenGui(player, ShopScreen.screenID);

        StoreManager.setOpenShop(player, id);
    }

    public static void openStore(Player player, StoreType type) {
        Store store = JustStore.getStoreManager().getStore(type);
        if (store == null) return;

        CommodityComp comp = new CommodityComp();
        comp.sendStoreGoods(player, store.getCommodities());

        PacketSender.sendRunFunction(player, "default", "global.eternal_store_category = " + type.getId() + ";", false);
        PacketSender.sendOpenGui(player, StoreScreen.screenID);
    }

}
