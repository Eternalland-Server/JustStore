package net.sakuragame.eternal.juststore.core.shop;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;

import java.util.LinkedHashMap;

@Getter
@AllArgsConstructor
public class Shop {

    private final String id;
    private final String name;
    private final boolean isFish;
    private final LinkedHashMap<Integer, GoodsShelf> goodsShelf;

    public Shop(String id, YamlConfiguration yaml) {
        this.id = id;
        this.name = yaml.getString("name");
        this.isFish = yaml.getBoolean("fish", false);
        this.goodsShelf = new LinkedHashMap<>();

        for (String key : yaml.getKeys(false)) {
            if (key.equals("name")) continue;

            String name = yaml.getString(key + ".name");
            ConfigurationSection section = yaml.getConfigurationSection(key + ".list");
            goodsShelf.put(goodsShelf.size(), new GoodsShelf(key, name, section));
        }
    }

    public GoodsShelf getGoodsShelf(int id) {
        return goodsShelf.get(id);
    }
}
