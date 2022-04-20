package net.sakuragame.eternal.juststore.core.merchant;

import lombok.Getter;
import org.bukkit.configuration.file.YamlConfiguration;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Getter
public class Merchant {

    private final String ID;
    private final String name;
    private final Map<String, Shelf> shelf;

    private final boolean fishing;

    public Merchant(String ID, YamlConfiguration yaml) {
        this.ID = ID;
        this.name = yaml.getString("__name__");
        this.shelf = new LinkedHashMap<>();
        this.fishing = yaml.getBoolean("__fish__", false);

        for (String key : yaml.getKeys(false)) {
            if (key.startsWith("__")) continue;

            String name = yaml.getString(key + ".name");
            List<String> list = yaml.getStringList(key + ".list");
            this.shelf.put(key, new Shelf(key, name, list));
        }
    }
}
