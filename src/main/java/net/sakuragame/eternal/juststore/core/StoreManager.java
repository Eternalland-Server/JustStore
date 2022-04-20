package net.sakuragame.eternal.juststore.core;

import ink.ptms.zaphkiel.ZaphkielAPI;
import net.sakuragame.eternal.gemseconomy.api.GemsEconomyAPI;
import net.sakuragame.eternal.justmessage.api.MessageAPI;
import net.sakuragame.eternal.justmessage.api.common.QuantityBox;
import net.sakuragame.eternal.juststore.JustStore;
import net.sakuragame.eternal.juststore.api.event.StorePurchaseEvent;
import net.sakuragame.eternal.juststore.api.event.StorePurchasedEvent;
import net.sakuragame.eternal.juststore.core.order.StoreOrder;
import net.sakuragame.eternal.juststore.core.store.Commodity;
import net.sakuragame.eternal.juststore.core.store.Store;
import net.sakuragame.eternal.juststore.core.store.StoreType;
import net.sakuragame.eternal.juststore.ui.Operation;
import net.sakuragame.eternal.juststore.util.Utils;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.UUID;

public class StoreManager {

    private final JustStore plugin;
    private final HashMap<StoreType, Store> stores;

    private final static HashMap<UUID, StoreOrder> storeOrder = new HashMap<>();

    public StoreManager(JustStore plugin) {
        this.plugin = plugin;
        this.stores = new HashMap<>();
    }

    public void init() {
        JustStore.getFileManager().loadStore();
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

        EnumCharge charge = commodity.getCharge();
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

    public Store getStore(StoreType type) {
        return stores.get(type);
    }

    public void clearDate(UUID uuid) {
        storeOrder.remove(uuid);
    }

    public void registerStore(StoreType type, YamlConfiguration yaml) {
        stores.put(type, new Store(yaml));
        plugin.getLogger().info(String.format("已加载 %s 商城( %s 件商品)", type.name(), stores.get(type).getCommodities().size()));
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
}
