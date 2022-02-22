package net.sakuragame.eternal.juststore.core.shop;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Getter
@AllArgsConstructor
public class Shop {

    private final String id;
    private final String name;
    private final boolean isFish;
    private final Map<Integer, GoodsShelf> goodsShelf;

    private final static List<String> IGNORE = Arrays.asList("name", "fish");

    public Shop(String id, YamlConfiguration yaml) {
        this.id = id;
        this.name = yaml.getString("name");
        this.isFish = yaml.getBoolean("fish", false);
        this.goodsShelf = new HashMap<>();

        for (String key : yaml.getKeys(false)) {
            if (IGNORE.contains(key)) continue;

            String name = yaml.getString(key + ".name");
            ConfigurationSection section = yaml.getConfigurationSection(key + ".list");
            goodsShelf.put(goodsShelf.size(), new GoodsShelf(key, name, section));
        }
    }

    public GoodsShelf getGoodsShelf(int id) {
        return goodsShelf.get(id);
    }
}
