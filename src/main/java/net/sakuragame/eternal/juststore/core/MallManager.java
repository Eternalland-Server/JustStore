package net.sakuragame.eternal.juststore.core;

import ink.ptms.zaphkiel.ZaphkielAPI;
import net.sakuragame.eternal.dragoncore.network.PacketSender;
import net.sakuragame.eternal.gemseconomy.api.GemsEconomyAPI;
import net.sakuragame.eternal.justmessage.api.MessageAPI;
import net.sakuragame.eternal.justmessage.api.common.QuantityBox;
import net.sakuragame.eternal.juststore.JustStore;
import net.sakuragame.eternal.juststore.api.event.ShopPurchasedEvent;
import net.sakuragame.eternal.juststore.api.event.StorePurchasedEvent;
import net.sakuragame.eternal.juststore.core.common.Charge;
import net.sakuragame.eternal.juststore.core.shop.Goods;
import net.sakuragame.eternal.juststore.core.shop.ShopCategory;
import net.sakuragame.eternal.juststore.core.shop.ShopOrder;
import net.sakuragame.eternal.juststore.core.shop.Shop;
import net.sakuragame.eternal.juststore.core.store.Commodity;
import net.sakuragame.eternal.juststore.core.store.Store;
import net.sakuragame.eternal.juststore.core.store.StoreOrder;
import net.sakuragame.eternal.juststore.core.store.StoreType;
import net.sakuragame.eternal.juststore.ui.Operation;
import net.sakuragame.eternal.juststore.ui.comp.GoodsShelfComp;
import net.sakuragame.eternal.juststore.ui.screen.ShopScreen;
import net.sakuragame.eternal.juststore.ui.screen.StoreScreen;
import net.sakuragame.eternal.juststore.util.Utils;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.*;

public class MallManager {

    private final JustStore plugin;
    private final HashMap<StoreType, Store> stores;
    private final HashMap<String, Shop> shops;

    private final HashMap<UUID, String> openMap;
    private final HashMap<UUID, StoreOrder> storeOrder;
    private final HashMap<UUID, ShopOrder> shopOrder;

    public MallManager(JustStore plugin) {
        this.plugin = plugin;
        this.stores = new HashMap<>();
        this.shops = new HashMap<>();
        this.openMap = new HashMap<>();
        this.storeOrder = new HashMap<>();
        this.shopOrder = new HashMap<>();
    }

    public void init() {
        plugin.getFileManager().loadStore();

        plugin.getFileManager().loadShops();
        plugin.getLogger().info(String.format("已载入 %s 个商店", shops.size()));
    }

    public void openShop(Player player, String shopID, ShopCategory category) {
        Shop shop = shops.get(shopID);
        if (shop == null) return;

        GoodsShelfComp comp = new GoodsShelfComp();
        switch (category) {
            case Weapon:
                comp.sendShopGoods(player, shop.getWeapon());
                break;
            case Equip:
                comp.sendShopGoods(player, shop.getEquip());
                break;
            case Accessory:
                comp.sendShopGoods(player, shop.getAccessory());
                break;
            case Material:
                comp.sendShopGoods(player, shop.getMaterial());
                break;
        }

        PacketSender.sendRunFunction(player, "default", "global.eternal_shop_category = " + category.getId() + ";", false);
        PacketSender.sendOpenGui(player, ShopScreen.screenID);
        openMap.put(player.getUniqueId(), shopID);
    }

    public void openStore(Player player, StoreType type) {
        Store store = stores.get(type);
        if (store == null) return;

        GoodsShelfComp comp = new GoodsShelfComp();
        comp.sendStoreGoods(player, store.getCommodities());

        PacketSender.sendRunFunction(player, "default", "global.eternal_store_category = " + type.getId() + ";", false);
        PacketSender.sendOpenGui(player, StoreScreen.screenID);
    }

    public Goods getGoods(String shopID, ShopCategory category, String goodsID) {
        Shop shop = shops.get(shopID);

        switch (category) {
            case Weapon:
                return shop.getWeapon().get(goodsID);
            case Equip:
                return shop.getEquip().get(goodsID);
            case Accessory:
                return shop.getAccessory().get(goodsID);
            case Material:
                return shop.getMaterial().get(goodsID);
        }

        return null;
    }

    public void storeBuying(Player player, StoreType type, String commodityID) {
        storeBuying(player, type, commodityID, null);
    }

    public void storeBuying(Player player, StoreType type, String commodityID, Integer amount) {
        UUID uuid = player.getUniqueId();

        Store store = stores.get(type);
        if (store == null) {
            player.closeInventory();
            return;
        }

        Commodity commodity = store.getCommodities().get(commodityID);
        if (commodity == null) {
            player.closeInventory();
            return;
        }

        if (!commodity.isSingle() && amount == null) {
            QuantityBox box = new QuantityBox(Operation.StoreOrder.name(), "&6&l购买数量", "&f&l" + commodity.getName());
            box.open(player, true);
            addStoreOrder(uuid, new StoreOrder(type, commodityID));
            return;
        }

        Charge charge = commodity.getCharge();
        double price = amount == null ? commodity.getPrice() : commodity.getPrice() * amount;
        double balance = GemsEconomyAPI.getBalance(uuid, charge.getCurrency());
        if (balance < price) {
            MessageAPI.sendActionTip(player, "&c&l你没有足够的" + charge.getCurrency().getDisplay());
            return;
        }

        ItemStack boughtGoods = ZaphkielAPI.INSTANCE.getItemStack(commodity.getItem(), player);
        if (boughtGoods == null) {
            player.sendMessage(" §c§l购买失败，请联系管理员");
            return;
        }
        charge.withdraw(player, price);

        amount = (amount == null) ? 1 : amount;
        boughtGoods.setAmount(amount);
        player.getInventory().addItem(boughtGoods);

        StorePurchasedEvent event = new StorePurchasedEvent(player, type, commodityID, amount);
        event.call();

        MessageAPI.sendActionTip(player, "&a&l购买成功！");
    }

    public void shopBuying(Player player, ShopCategory category, String goodsID) {
        shopBuying(player, category, goodsID, null);
    }

    public void shopBuying(Player player, ShopCategory category, String goodsID, Integer amount) {
        UUID uuid = player.getUniqueId();
        String openShop = getOpenShop(uuid);
        if (openShop == null) {
            player.closeInventory();
            return;
        }

        Goods goods = getGoods(openShop, category, goodsID);
        if (goods == null) {
            player.closeInventory();
            return;
        }

        if (category == ShopCategory.Material && amount == null) {
            QuantityBox box = new QuantityBox(Operation.ShopOrder.name(), "&6&l购买数量", "&7" + goods.getName());
            box.open(player, true);
            addShopOrder(player.getUniqueId(), new ShopOrder(openShop, goods.getId()));
            return;
        }

        Charge charge = goods.getCharge();
        double price = amount == null ? goods.getPrice() : goods.getPrice() * amount;
        double balance = GemsEconomyAPI.getBalance(uuid, charge.getCurrency());
        if (balance < price) {
            MessageAPI.sendActionTip(player, "&c&l你没有足够的" + charge.getCurrency().getDisplay());
            return;
        }

        if (goods.getConsume().size() != 0) {
            if (!Utils.checkItem(player, new HashMap<>(goods.getConsume()))) {
                MessageAPI.sendActionTip(player, "&c&l购买失败，背包内材料不足");
                return;
            }

            Utils.consumeItem(player, new HashMap<>(goods.getConsume()));
        }

        ItemStack boughtGoods = ZaphkielAPI.INSTANCE.getItemStack(goods.getItem(), player);
        if (boughtGoods == null) {
            player.sendMessage(" §c§l购买失败，请联系管理员");
            return;
        }
        charge.withdraw(player, price);

        amount = (amount == null) ? 1 : amount;
        boughtGoods.setAmount(amount);
        player.getInventory().addItem(boughtGoods);

        ShopPurchasedEvent event = new ShopPurchasedEvent(player, openShop, category, goodsID, amount);
        event.call();

        MessageAPI.sendActionTip(player, "&a&l购买成功！");
    }

    public void registerStore(StoreType type, YamlConfiguration yaml) {
        stores.put(type, new Store(yaml));
        plugin.getLogger().info(String.format("已加载 %s 商城( %s 件商品)", type.name(), stores.get(type).getCommodities().size()));
    }

    public void clearDate(UUID uuid) {
        openMap.remove(uuid);
        storeOrder.remove(uuid);
        shopOrder.remove(uuid);
    }

    public void registerShop(String shopID, YamlConfiguration yaml) {
        shops.put(shopID, new Shop(yaml));
    }

    public void addStoreOrder(UUID uuid, StoreOrder order) {
        storeOrder.put(uuid, order);
    }

    public void delStoreOrder(UUID uuid) {
        storeOrder.remove(uuid);
    }

    public StoreOrder getStoreOrder(UUID uuid) {
        return storeOrder.get(uuid);
    }

    public List<String> getShopsKey() {
        return new ArrayList<>(shops.keySet());
    }

    public String getOpenShop(UUID uuid) {
        return openMap.get(uuid);
    }

    public void addShopOrder(UUID uuid, ShopOrder order) {
        shopOrder.put(uuid, order);
    }

    public void delShopOrder(UUID uuid) {
        shopOrder.remove(uuid);
    }

    public ShopOrder getShopOrder(UUID uuid) {
        return shopOrder.get(uuid);
    }
}
