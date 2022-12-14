package net.sakuragame.eternal.juststore.ui;

import net.sakuragame.eternal.dragoncore.network.PacketSender;
import net.sakuragame.eternal.juststore.JustStore;
import net.sakuragame.eternal.juststore.api.event.StoreOpenEvent;
import net.sakuragame.eternal.juststore.core.merchant.Merchant;
import net.sakuragame.eternal.juststore.core.merchant.Shelf;
import net.sakuragame.eternal.juststore.core.store.StoreType;
import net.sakuragame.eternal.juststore.file.sub.ConfigFile;
import net.sakuragame.eternal.juststore.ui.comp.CategoryComp;
import net.sakuragame.eternal.juststore.ui.comp.CommodityComp;
import net.sakuragame.eternal.juststore.ui.comp.economy.CurrencyComp;
import net.sakuragame.eternal.juststore.ui.comp.economy.EconomyComp;
import net.sakuragame.eternal.juststore.ui.comp.economy.FishComp;
import net.sakuragame.eternal.juststore.util.Utils;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ScreenManager {

    public final static String Shop_ID = "eternal_shop";
    public final static String Store_ID = "eternal_store";

    public static void openShop(Player player, String shopID) {
        openShop(player, shopID, null);
    }

    public static void openShop(Player player, String id, String shelfID) {
        Merchant merchant = JustStore.getShopManger().getMerchant(id);
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
        PacketSender.sendOpenGui(player, Shop_ID);
    }

    public static void openStore(Player player, StoreType type) {
        StoreOpenEvent event = new StoreOpenEvent(player, type);
        event.call();
        if (event.isCancelled()) return;

        List<String> commodityID = JustStore.getStoreManager().getCommodityID(event.getType());
        if (commodityID == null) return;

        CommodityComp comp = new CommodityComp();
        comp.sendStore(player, commodityID);

        PacketSender.sendSyncPlaceholder(player,
                new HashMap<String, String>() {{
                    put("eternal_store_tip", String.join("\n", ConfigFile.tip).replace("<discount>", Utils.getDiscount(player) * 10 + "???"));
                }}
        );
        PacketSender.sendRunFunction(player, "default", "global.eternal_store_category = " + type.getId() + ";", false);
        PacketSender.sendOpenGui(player, Store_ID);
    }

}
