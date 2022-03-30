package net.sakuragame.eternal.juststore.core.shop;

import lombok.Getter;
import net.sakuragame.eternal.juststore.core.shop.goods.Goods;
import org.bukkit.configuration.ConfigurationSection;

import java.util.LinkedHashMap;

@Getter
public class GoodsShelf {

    private final String id;
    private final String name;
    private final LinkedHashMap<String, Goods> goods;

    public GoodsShelf(String id, String name, ConfigurationSection section) {
        this.id = id;
        this.name = name;
        this.goods = loadCategory(section);
    }

    private LinkedHashMap<String, Goods> loadCategory(ConfigurationSection section) {
        LinkedHashMap<String, Goods> map = new LinkedHashMap<>();
        if (section == null) return map;

        for (String key : section.getKeys(false)) {
            ConfigurationSection sub = section.getConfigurationSection(key);
            TradeType type = TradeType.valueOf(sub.getString("type", "BUY").toUpperCase());

            map.put(key, Goods.newInstance(key, type, sub));
        }

        return map;
    }

    public Goods getGoods(String id) {
        return goods.get(id);
    }
}
