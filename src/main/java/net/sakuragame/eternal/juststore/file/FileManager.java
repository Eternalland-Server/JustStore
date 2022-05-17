package net.sakuragame.eternal.juststore.file;

import com.taylorswiftcn.justwei.file.JustConfiguration;
import com.taylorswiftcn.justwei.util.MegumiUtil;
import lombok.Getter;
import net.sakuragame.eternal.juststore.JustStore;
import net.sakuragame.eternal.juststore.core.store.StoreType;
import net.sakuragame.eternal.juststore.file.sub.ConfigFile;
import net.sakuragame.eternal.juststore.file.sub.MessageFile;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;


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

        initMerchant();
        initStore();
    }

    private void initMerchant() {
        File file = new File(plugin.getDataFolder(), "merchant");
        if (!file.mkdirs()) return;

        File shop = new File(file, "shop/example.yml");
        File goods = new File(file, "goods/example.yml");

        if (shop.getParentFile().mkdirs()) {
            MegumiUtil.copyFile(plugin.getResource("shop.yml"), shop);
        }
        if (goods.getParentFile().mkdirs()) {
            MegumiUtil.copyFile(plugin.getResource("goods.yml"), goods);
        }
    }

    private void initStore() {
        File file = new File(plugin.getDataFolder(), "mall");
        if (!file.mkdirs()) return;

        File store = new File(file, "store/prop.yml");
        File commodity = new File(file, "commodity/prop.yml");

        if (store.getParentFile().mkdirs()) {
            MegumiUtil.copyFile(plugin.getResource("store.yml"), store);
        }
        if (commodity.getParentFile().mkdirs()) {
            MegumiUtil.copyFile(plugin.getResource("commodity.yml"), commodity);
        }

        for (StoreType type : StoreType.values()) {
            File sub = new File(file, "store/" + type.getFile());
            if (type.getFile().equals("prop.yml")) continue;

            try {
                sub.createNewFile();
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
