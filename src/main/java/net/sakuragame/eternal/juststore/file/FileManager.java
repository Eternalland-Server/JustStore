package net.sakuragame.eternal.juststore.file;

import com.taylorswiftcn.justwei.file.JustConfiguration;
import com.taylorswiftcn.justwei.util.MegumiUtil;
import net.sakuragame.eternal.juststore.JustStore;
import net.sakuragame.eternal.juststore.core.store.StoreType;
import net.sakuragame.eternal.juststore.file.sub.ConfigFile;
import net.sakuragame.eternal.juststore.file.sub.MessageFile;
import lombok.Getter;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.util.Arrays;


public class FileManager extends JustConfiguration {

    private final JustStore plugin;

    @Getter private YamlConfiguration config;
    @Getter private YamlConfiguration message;

    public FileManager(JustStore plugin) {
        super(plugin);
        this.plugin = plugin;
    }

    public void init() {
        config = initFile("config.yml");
        message = initFile("message.yml");

        ConfigFile.init();
        MessageFile.init();

        initShops();
        initStore();
    }

    private void initShops() {
        File file = new File(plugin.getDataFolder(), "shops");
        if (file.exists()) return;

        File template = new File(file, "shop.yml");
        template.getParentFile().mkdirs();
        MegumiUtil.copyFile(plugin.getResource("shop.yml"), template);
    }

    private void initStore() {
        File file = new File(plugin.getDataFolder(), "stores");
        if (file.exists()) return;

        for (StoreType type : StoreType.values()) {
            File sub = new File(file, type.getFile());
            sub.getParentFile().mkdirs();
            if (type.getFile().equals("prop.yml")) {
                MegumiUtil.copyFile(plugin.getResource("store.yml"), sub);
                continue;
            }

            try {
                sub.createNewFile();
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void loadShops() {
        File file = new File(plugin.getDataFolder(), "shops");

        File[] child = file.listFiles();
        if (child == null || child.length == 0) return;

        Arrays.stream(child).filter(sub -> sub.getName().endsWith(".yml")).forEach(sub -> {
            String shopID = sub.getName().replace(".yml", "");
            YamlConfiguration yaml = YamlConfiguration.loadConfiguration(sub);
            JustStore.getMallManager().registerShop(shopID, yaml);
        });
    }

    public void loadStore() {
        File file = new File(plugin.getDataFolder(), "stores");

        for (StoreType type : StoreType.values()) {
            File sub = new File(file, type.getFile());
            if (!sub.exists()) continue;

            YamlConfiguration yaml = YamlConfiguration.loadConfiguration(sub);
            JustStore.getMallManager().registerStore(type, yaml);
        }
    }
}
