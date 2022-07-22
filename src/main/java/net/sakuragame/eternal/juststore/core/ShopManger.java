package net.sakuragame.eternal.juststore.core;

import net.sakuragame.eternal.juststore.JustStore;
import net.sakuragame.eternal.juststore.core.merchant.*;
import net.sakuragame.eternal.juststore.core.merchant.goods.BuyGoods;
import net.sakuragame.eternal.juststore.core.merchant.goods.Goods;
import net.sakuragame.eternal.juststore.core.merchant.goods.SellGoods;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.util.*;

public class ShopManger {

    private final JustStore plugin;

    private final Map<TradeType, Class<? extends Goods>> preset;

    private final Map<String, Merchant> merchants;
    private final Map<String, Goods> goodsMap;

    private final Map<UUID, String> cache;

    public ShopManger(JustStore plugin) {
        this.plugin = plugin;
        this.preset = new HashMap<>();
        this.merchants = new HashMap<>();
        this.goodsMap = new HashMap<>();
        this.cache = new HashMap<>();
        this.registerPreset();
    }

    public void init() {
        this.loadMerchantFile();
        this.loadGoodsFile();

        plugin.getLogger().info("已加载 " + this.merchants.size() + " 名商人");
        plugin.getLogger().info("已加载 " + this.goodsMap.size() + " 件商品");
    }

    public Set<String> getMerchantIDs() {
        return this.merchants.keySet();
    }

    public Merchant getMerchant(String key) {
        return this.merchants.get(key);
    }

    public Goods getGoods(String key) {
        return this.goodsMap.get(key);
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

    public void registerPreset() {
        this.preset.put(TradeType.BUY, BuyGoods.class);
        this.preset.put(TradeType.SELL, SellGoods.class);
    }

    public void loadMerchantFile() {
        this.merchants.clear();

        File file = new File(plugin.getDataFolder(), "shop/shop");
        File[] files = file.listFiles();

        if (files == null || files.length == 0) return;
        Arrays.stream(files)
                .filter(k -> k.getName().endsWith(".yml"))
                .forEach(k -> {
                    YamlConfiguration yaml = YamlConfiguration.loadConfiguration(k);
                    String id = yaml.getString("__id__");
                    this.merchants.put(id, new Merchant(id, yaml));
                });
    }

    public void loadGoodsFile() {
        this.goodsMap.clear();

        File file = new File(plugin.getDataFolder(), "shop/goods");
        File[] files = file.listFiles();

        if (files == null || files.length == 0) return;
        Arrays.stream(files)
                .filter(k -> k.getName().endsWith(".yml"))
                .forEach(this::loadGoodsFile);
    }

    public void loadGoodsFile(File file) {
        try {
            YamlConfiguration yaml = YamlConfiguration.loadConfiguration(file);
            for (String key : yaml.getKeys(false)) {
                TradeType type = TradeType.valueOf(yaml.getString(key + ".type", "BUY").toUpperCase());
                ConfigurationSection section = yaml.getConfigurationSection(key);
                this.goodsMap.put(key, this.preset.get(type)
                        .getConstructor(String.class, ConfigurationSection.class)
                        .newInstance(key, section)
                );
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
}
