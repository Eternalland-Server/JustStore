package net.sakuragame.eternal.juststore.core;

import ink.ptms.zaphkiel.ZaphkielAPI;
import net.sakuragame.eternal.gemseconomy.api.GemsEconomyAPI;
import net.sakuragame.eternal.gemseconomy.currency.EternalCurrency;
import net.sakuragame.eternal.justmessage.api.MessageAPI;
import net.sakuragame.eternal.justmessage.api.common.QuantityBox;
import net.sakuragame.eternal.juststore.JustStore;
import net.sakuragame.eternal.juststore.api.event.*;
import net.sakuragame.eternal.juststore.core.shop.Goods;
import net.sakuragame.eternal.juststore.core.shop.GoodsShelf;
import net.sakuragame.eternal.juststore.core.shop.Shop;
import net.sakuragame.eternal.juststore.core.shop.ShopOrder;
import net.sakuragame.eternal.juststore.core.store.Commodity;
import net.sakuragame.eternal.juststore.core.store.Store;
import net.sakuragame.eternal.juststore.core.store.StoreOrder;
import net.sakuragame.eternal.juststore.core.store.StoreType;
import net.sakuragame.eternal.juststore.file.sub.ConfigFile;
import net.sakuragame.eternal.juststore.ui.Operation;
import net.sakuragame.eternal.juststore.util.Utils;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.*;

public class StoreManager {

    private final JustStore plugin;
    private final HashMap<StoreType, Store> stores;
    private final HashMap<String, Shop> shops;

    private final static HashMap<UUID, String> openMap = new HashMap<>();
    private final static HashMap<UUID, StoreOrder> storeOrder = new HashMap<>();
    private final static HashMap<UUID, ShopOrder> shopOrder = new HashMap<>();

    public StoreManager(JustStore plugin) {
        this.plugin = plugin;
        this.stores = new HashMap<>();
        this.shops = new HashMap<>();
    }

    public void init() {
        JustStore.getFileManager().loadStore();
        JustStore.getFileManager().loadShops();
        plugin.getLogger().info(String.format("已载入 %s 个商店", shops.size()));
    }

    public List<String> getShopsKey() {
        return new ArrayList<>(shops.keySet());
    }

    public void storeBuying(Player player, StoreType type, String commodityID) {
        storeBuying(player, type, commodityID, null);
    }

    public void storeBuying(Player player, StoreType type, String commodityID, Integer quantity) {
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

        if (!commodity.isSingle() && quantity == null) {
            QuantityBox box = new QuantityBox(Operation.StoreOrder.name(), "&6&l购买数量", "&f&l" + commodity.getName());
            box.open(player, true);
            addStoreOrder(uuid, new StoreOrder(type, commodityID));
            return;
        }

        quantity = quantity == null ? 1 : quantity;

        StorePurchaseEvent preEvent = new StorePurchaseEvent(player, type, commodityID, quantity);
        preEvent.call();
        if (preEvent.isCancelled()) return;

        Charge charge = commodity.getCharge();
        double price = commodity.getPrice() * quantity * Utils.getDiscount(player);
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

        boughtGoods.setAmount(quantity * commodity.getAmount());
        player.getInventory().addItem(boughtGoods);

        StorePurchasedEvent postEvent = new StorePurchasedEvent(player, type, commodityID, quantity);
        postEvent.call();

        MessageAPI.sendActionTip(player, "&a&l购买成功！");
    }

    public void shopBuying(Player player, int category, String goodsID) {
        shopBuying(player, category, goodsID, null);
    }

    public void shopBuying(Player player, int categoryID, String goodsID, Integer quantity) {
        UUID uuid = player.getUniqueId();
        String currentShop = getOpenShop(uuid);
        if (currentShop == null) {
            player.closeInventory();
            return;
        }

        Shop shop = getShop(currentShop);
        Goods goods = getGoods(shop, categoryID, goodsID);
        if (goods == null) {
            player.closeInventory();
            return;
        }

        String category = shop.getGoodsShelf(categoryID).getId();

        if (!goods.isSingle() && quantity == null) {
            QuantityBox box = new QuantityBox(Operation.ShopOrder.name(), "&6&l购买数量", "&7" + goods.getName());
            box.open(player, true);
            addShopOrder(player.getUniqueId(), new ShopOrder(currentShop, categoryID, goods.getId()));
            return;
        }

        quantity = quantity == null ? 1 : quantity;

        ShopPurchaseEvent preEvent = new ShopPurchaseEvent(player, currentShop, category, goodsID, quantity);
        preEvent.call();
        if (preEvent.isCancelled()) return;

        Charge charge = goods.getCharge();
        double price = goods.getPrice() * quantity;
        double balance = GemsEconomyAPI.getBalance(uuid, charge.getCurrency());
        if (balance < price) {
            MessageAPI.sendActionTip(player, "&c&l你没有足够的" + charge.getCurrency().getDisplay());
            return;
        }

        if (goods.getConsume().size() != 0) {
            if (!Utils.checkItem(player, goods.getConsume(quantity))) {
                MessageAPI.sendActionTip(player, "&c&l购买失败，背包内材料不足");
                return;
            }

            Utils.consumeItem(player, goods.getConsume(quantity));
        }

        ItemStack boughtGoods = ZaphkielAPI.INSTANCE.getItemStack(goods.getItem(), player);
        if (boughtGoods == null) {
            player.sendMessage(" §c§l购买失败，请联系管理员");
            return;
        }
        charge.withdraw(player, price);

        boughtGoods.setAmount(quantity * goods.getAmount());
        player.getInventory().addItem(boughtGoods);

        ShopPurchasedEvent postEvent = new ShopPurchasedEvent(player, currentShop, category, goodsID, quantity);
        postEvent.call();

        MessageAPI.sendActionTip(player, "&a&l购买成功！");
        player.sendMessage(ConfigFile.prefix + "你购买了 " + goods.getName());
    }

    public void shopSelling(Player player, int categoryID, String goodsID, int quantity) {
        UUID uuid = player.getUniqueId();
        String currentShop = getOpenShop(uuid);
        if (currentShop == null) {
            player.closeInventory();
            return;
        }

        Shop shop = getShop(currentShop);
        Goods goods = getGoods(shop, categoryID, goodsID);
        if (goods == null) {
            player.closeInventory();
            return;
        }

        String category = shop.getGoodsShelf(categoryID).getId();

        ShopSellEvent sellEvent = new ShopSellEvent(player, currentShop, category, goodsID, quantity);
        sellEvent.call();
        if (sellEvent.isCancelled()) return;

        Charge charge = goods.getCharge();
        double price = goods.getPrice() * quantity;

        Map<String, Integer> sellItem = new HashMap<String, Integer>() {{ put(goods.getItem(), goods.getAmount()); }};
        if (!Utils.checkItem(player, new HashMap<>(sellItem))) {
            MessageAPI.sendActionTip(player, "&c&l出售失败，你背包内没有足够的 " + goods.getName());
            return;
        }

        EternalCurrency currency = charge.getCurrency();
        Utils.consumeItem(player, sellItem);
        GemsEconomyAPI.deposit(uuid, price, charge.getCurrency());
        MessageAPI.sendActionTip(player, "&a&l出售成功！");
        player.sendMessage(ConfigFile.prefix + "§7你出售了 " + goods.getName() + " §7获得了 §f" + Utils.unitFormatting(price) + " §7" + currency.getDisplay());
    }

    public Store getStore(StoreType type) {
        return stores.get(type);
    }

    public Shop getShop(String id) {
        return shops.get(id);
    }

    public Goods getGoods(Shop shop, int category, String goodsID) {
        GoodsShelf shelf = shop.getGoodsShelf().get(category);
        if (shelf != null) {
            return shelf.getGoods().get(goodsID);
        }

        return null;
    }

    public void clearDate(UUID uuid) {
        openMap.remove(uuid);
        storeOrder.remove(uuid);
        shopOrder.remove(uuid);
    }

    public void registerStore(StoreType type, YamlConfiguration yaml) {
        stores.put(type, new Store(yaml));
        plugin.getLogger().info(String.format("已加载 %s 商城( %s 件商品)", type.name(), stores.get(type).getCommodities().size()));
    }

    public void registerShop(String id, YamlConfiguration yaml) {
        shops.put(id, new Shop(id, yaml));
    }

    public static boolean isCurrentOpenShop(Player player, String id) {
        UUID uuid = player.getUniqueId();
        if (!openMap.containsKey(uuid)) return false;
        return openMap.get(uuid).equals(id);
    }

    public static String getOpenShop(UUID uuid) {
        return openMap.get(uuid);
    }

    public static void setOpenShop(Player player, String id) {
        openMap.put(player.getUniqueId(), id);
    }

    public static void addStoreOrder(UUID uuid, StoreOrder order) {
        storeOrder.put(uuid, order);
    }

    public static void delStoreOrder(UUID uuid) {
        storeOrder.remove(uuid);
    }

    public static StoreOrder getStoreOrder(UUID uuid) {
        return storeOrder.get(uuid);
    }

    public static void addShopOrder(UUID uuid, ShopOrder order) {
        shopOrder.put(uuid, order);
    }

    public static void delShopOrder(UUID uuid) {
        shopOrder.remove(uuid);
    }

    public static ShopOrder getShopOrder(UUID uuid) {
        return shopOrder.get(uuid);
    }
}
