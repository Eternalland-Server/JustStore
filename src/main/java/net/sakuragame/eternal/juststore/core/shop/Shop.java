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
    private final LinkedHashMap<String, Goods> weapon;
    private final LinkedHashMap<String, Goods> equip;
    private final LinkedHashMap<String, Goods> accessory;
    private final LinkedHashMap<String, Goods> material;

    public Shop(YamlConfiguration yaml) {
        this.name = yaml.getString("name");
        this.weapon = loadCategory(yaml.getConfigurationSection("weapon"));
        this.equip = loadCategory(yaml.getConfigurationSection("equip"));
        this.accessory = loadCategory(yaml.getConfigurationSection("accessory"));
        this.material = loadCategory(yaml.getConfigurationSection("material"));
    }

    private LinkedHashMap<String, Goods> loadCategory(ConfigurationSection section) {
        LinkedHashMap<String, Goods> map = new LinkedHashMap<>();
        if (section == null) return map;

        for (String key : section.getKeys(false)) {
            ConfigurationSection sub = section.getConfigurationSection(key);
            map.put(key, new Goods(key, sub));
        }

        return map;
    }
}
