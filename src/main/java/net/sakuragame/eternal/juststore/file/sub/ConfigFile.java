package net.sakuragame.eternal.juststore.file.sub;

import com.taylorswiftcn.justwei.util.MegumiUtil;
import net.sakuragame.eternal.juststore.JustStore;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ConfigFile {
    private static YamlConfiguration config;

    public static String prefix;

    public static List<String> tip;
    public static Map<String, Double> discount;
    public static Map<String, Integer> purchaseLimit;

    public static void init() {
        config = JustStore.getFileManager().getConfig();

        prefix = getString("Prefix");
        tip = getStringList("tip");
        loadDiscount();
        loadPurchaseLimit();
    }

    private static String getString(String path) {
        return MegumiUtil.onReplace(config.getString(path));
    }

    private static List<String> getStringList(String path) {
        return MegumiUtil.onReplace(config.getStringList(path));
    }

    private static void loadDiscount() {
        discount = new HashMap<>();

        ConfigurationSection section = config.getConfigurationSection("discount");
        if (section == null) return;

        for (String key : section.getKeys(false)) {
            double percent = section.getDouble(key);
            discount.put(key, percent);
        }
    }

    private static void loadPurchaseLimit() {
        purchaseLimit = new HashMap<>();

        ConfigurationSection section = config.getConfigurationSection("purchase-limit");
        if (section == null) return;

        for (String key : section.getKeys(false)) {
            int amount = section.getInt(key);
            purchaseLimit.put(key, amount);
        }
    }
}
