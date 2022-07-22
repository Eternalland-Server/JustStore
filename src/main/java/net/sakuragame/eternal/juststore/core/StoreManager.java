package net.sakuragame.eternal.juststore.core;

import ink.ptms.zaphkiel.ZaphkielAPI;
import net.sakuragame.eternal.gemseconomy.api.GemsEconomyAPI;
import net.sakuragame.eternal.gemseconomy.currency.EternalCurrency;
import net.sakuragame.eternal.justmessage.api.MessageAPI;
import net.sakuragame.eternal.justmessage.api.common.QuantityBox;
import net.sakuragame.eternal.juststore.JustStore;
import net.sakuragame.eternal.juststore.api.event.StoreTradeEvent;
import net.sakuragame.eternal.juststore.core.store.Commodity;
import net.sakuragame.eternal.juststore.core.store.StoreType;
import net.sakuragame.eternal.juststore.ui.Operation;
import net.sakuragame.eternal.juststore.util.Utils;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.io.File;
import java.util.*;

public class StoreManager {

    private final JustStore plugin;

    private final Map<StoreType, List<String>> stores;
    private final Map<String, Commodity> commodities;

    private final Map<UUID, String> cache;

    public StoreManager(JustStore plugin) {
        this.plugin = plugin;
        this.stores = new HashMap<>();
        this.commodities = new HashMap<>();
        this.cache = new HashMap<>();
    }

    public void init() {
        this.loadStoreFile();
        this.loadCommodityFile();

        plugin.getLogger().info("已加载 " + stores.size() + " 个商城(" + commodities.size() + "件商品)");
    }

    public List<String> getCommodityID(StoreType key) {
        return this.stores.get(key);
    }

    public Commodity getCommodity(String key) {
        return this.commodities.get(key);
    }

    public void trade(Player player, String commodityID) {
        this.trade(player, commodityID, null);
    }

    public void trade(Player player, String commodityID, Integer quantity) {
        UUID uuid = player.getUniqueId();

        Commodity commodity = this.commodities.get(commodityID);
        if (commodity == null) return;

        if (Utils.getEmptySlotCount(player) == 0) {
            MessageAPI.sendActionTip(player, "&a&l交易前请确保背包内有空余的槽位");
            return;
        }

        if (!commodity.isSingle() && quantity == null) {
            QuantityBox box = new QuantityBox(Operation.StoreOrder.name(), "&6&l购买数量", "&f&l" + commodity.getName());
            box.open(player, true);
            this.addCache(uuid, commodityID);
            return;
        }

        quantity = quantity == null ? 1 : quantity;

        StoreTradeEvent.Pre preEvent = new StoreTradeEvent.Pre(player, commodity, quantity);
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

        if (charge.getCurrency() == EternalCurrency.Points) {
            charge.withdraw(player, price, "购买 " + commodityID + " x" + quantity);
        }
        else {
            charge.withdraw(player, price);
        }

        boughtGoods.setAmount(quantity * commodity.getAmount());
        player.getInventory().addItem(boughtGoods);

        MessageAPI.sendActionTip(player, "&a&l购买成功！");

        StoreTradeEvent.Post postEvent = new StoreTradeEvent.Post(player, commodity, quantity);
        postEvent.call();
    }

    public void addCache(UUID uuid, String id) {
        this.cache.put(uuid, id);
    }

    public String getCache(UUID uuid) {
        return this.cache.remove(uuid);
    }

    public void delCache(UUID uuid) {
        this.cache.remove(uuid);
    }

    public void loadStoreFile() {
        for (StoreType type : StoreType.values()) {
            File file = new File(plugin.getDataFolder(), "mall/store/" + type.getFile());
            if (!file.exists()) continue;

            YamlConfiguration yaml = YamlConfiguration.loadConfiguration(file);
            this.stores.put(type, yaml.getStringList("list"));
        }
    }

    public void loadCommodityFile() {
        File file = new File(plugin.getDataFolder(), "mall/commodity");
        File[] files = file.listFiles();

        if (files == null || files.length == 0) return;
        Arrays.stream(files)
                .filter(k -> k.getName().endsWith(".yml"))
                .forEach(k -> {
                    YamlConfiguration yaml = YamlConfiguration.loadConfiguration(k);
                    for (String key : yaml.getKeys(false)) {
                        ConfigurationSection section = yaml.getConfigurationSection(key);
                        this.commodities.put(key, new Commodity(key, section));
                    }
                });
    }
}
