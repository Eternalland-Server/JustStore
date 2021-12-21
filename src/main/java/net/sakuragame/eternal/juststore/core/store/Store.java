package net.sakuragame.eternal.juststore.core.store;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;

import java.util.LinkedHashMap;

@Getter
@AllArgsConstructor
public class Store {

    private final LinkedHashMap<String, Commodity> commodities;

    public Store(YamlConfiguration yaml) {
        commodities = new LinkedHashMap<>();

        for (String key : yaml.getKeys(false)) {
            ConfigurationSection section = yaml.getConfigurationSection(key);
            commodities.put(key, new Commodity(key, section));
        }
    }
}
