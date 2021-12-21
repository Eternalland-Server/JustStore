package net.sakuragame.eternal.juststore;

import net.sakuragame.eternal.juststore.commands.MainCommand;
import net.sakuragame.eternal.juststore.core.MallManager;
import net.sakuragame.eternal.juststore.file.FileManager;
import lombok.Getter;
import net.sakuragame.eternal.juststore.listener.PlayerListener;
import net.sakuragame.eternal.juststore.listener.ShopListener;
import net.sakuragame.eternal.juststore.listener.StoreListener;
import net.sakuragame.eternal.juststore.ui.ScreenManager;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public class JustStore extends JavaPlugin {
    @Getter private static JustStore instance;

    @Getter private FileManager fileManager;
    private MallManager mallManager;
    private ScreenManager screenManager;

    @Override
    public void onEnable() {
        long start = System.currentTimeMillis();

        instance = this;

        fileManager = new FileManager(this);
        mallManager = new MallManager(this);
        screenManager = new ScreenManager();
        fileManager.init();
        mallManager.init();
        screenManager.init();

        Bukkit.getPluginManager().registerEvents(new PlayerListener(), this);
        Bukkit.getPluginManager().registerEvents(new StoreListener(), this);
        Bukkit.getPluginManager().registerEvents(new ShopListener(), this);
        getCommand("jstore").setExecutor(new MainCommand());

        long end = System.currentTimeMillis();

        getLogger().info("加载成功! 用时 %time% ms".replace("%time%", String.valueOf(end - start)));
    }

    @Override
    public void onDisable() {
        getLogger().info("卸载成功!");
    }

    public String getVersion() {
        String packet = Bukkit.getServer().getClass().getPackage().getName();
        return packet.substring(packet.lastIndexOf('.') + 1);
    }

    public void reload() {
        fileManager.init();
        mallManager.init();
    }

    public static MallManager getMallManager() {
        return instance.mallManager;
    }

    public static ScreenManager getScreenManager() {
        return instance.screenManager;
    }
}
