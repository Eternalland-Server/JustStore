package net.sakuragame.eternal.juststore.core.shop;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;

import java.util.LinkedHashMap;

@Getter
@AllArgsConstructor
public class Shop {

    private final String name;
    private final LinkedHashMap<Integer, GoodsShelf> shelf;

    public Shop(YamlConfiguration yaml) {
        this.name = yaml.getString("name");
        this.shelf = new LinkedHashMap<>();

        for (String key : yaml.getKeys(false)) {
            if (key.equals("name")) continue;

            String name = yaml.getString(key + ".name");
            ConfigurationSection section = yaml.getConfigurationSection(key + ".list");
            shelf.put(shelf.size(), new GoodsShelf(key, name, section));
        }
    }
}
