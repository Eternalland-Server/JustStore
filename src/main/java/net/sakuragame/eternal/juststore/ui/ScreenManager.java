package net.sakuragame.eternal.juststore.ui;

import net.sakuragame.eternal.dragoncore.network.PacketSender;
import net.sakuragame.eternal.justinventory.JustInventory;
import net.sakuragame.eternal.justinventory.ui.UIManager;
import net.sakuragame.eternal.juststore.JustStore;
import net.sakuragame.eternal.juststore.core.merchant.Merchant;
import net.sakuragame.eternal.juststore.core.merchant.Shelf;
import net.sakuragame.eternal.juststore.core.store.Store;
import net.sakuragame.eternal.juststore.core.store.StoreType;
import net.sakuragame.eternal.juststore.file.sub.ConfigFile;
import net.sakuragame.eternal.juststore.ui.comp.CategoryComp;
import net.sakuragame.eternal.juststore.ui.comp.CommodityComp;
import net.sakuragame.eternal.juststore.ui.comp.economy.CurrencyComp;
import net.sakuragame.eternal.juststore.ui.comp.economy.EconomyComp;
import net.sakuragame.eternal.juststore.ui.comp.economy.FishComp;
import net.sakuragame.eternal.juststore.ui.view.MerchantUI;
import net.sakuragame.eternal.juststore.ui.view.StoreUI;
import net.sakuragame.eternal.juststore.util.Utils;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;

public class ScreenManager {

    private final UIManager uiManager;

    public ScreenManager() {
        this.uiManager = JustInventory.getInstance().getUiManager();
    }

    public void init() {
        uiManager.registerUI(new MerchantUI());
        uiManager.registerUI(new StoreUI());
    }

    public static void openMerchant(Player player, String shopID) {
        openMerchant(player, shopID, null);
    }

    public static void openMerchant(Player player, String id, String shelfID) {
        Merchant merchant = JustStore.getMerchantManger().getMerchant(id);
        if (merchant == null) return;

        Shelf shelf = shelfID == null ? merchant.getShelf().values().stream().findFirst().get() : merchant.getShelf().get(shelfID);
        if (shelf == null) return;

        CurrencyComp currencyComp = merchant.isFishing() ? new FishComp() : new EconomyComp();
        currencyComp.send(player);

        CategoryComp categoryComp = new CategoryComp();
        categoryComp.send(player, merchant);

        CommodityComp comp = new CommodityComp();
        comp.sendMerchant(player, shelf);

        Map<String, String> map = new HashMap<>();
        map.put("eternal_shop_name", merchant.getName());

        PacketSender.sendSyncPlaceholder(player, map);
        PacketSender.sendRunFunction(player, "default", "global.eternal_shop_category = '" + shelf.getID() + "';", false);
        PacketSender.sendOpenGui(player, MerchantUI.screenID);
    }

    public static void openStore(Player player, StoreType type) {
        Store store = JustStore.getStoreManager().getStore(type);
        if (store == null) return;

        CommodityComp comp = new CommodityComp();
        comp.sendStore(player, store.getCommodities());

        PacketSender.sendSyncPlaceholder(player,
                new HashMap<String, String>() {{
                    put("eternal_store_tip", String.join("\n", ConfigFile.tip).replace("<discount>", Utils.getDiscount(player) * 10 + "折"));
                }}
        );
        PacketSender.sendRunFunction(player, "default", "global.eternal_store_category = " + type.getId() + ";", false);
        PacketSender.sendOpenGui(player, StoreUI.screenID);
    }

}
