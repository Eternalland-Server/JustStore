package net.sakuragame.eternal.juststore;

import lombok.Getter;
import net.sakuragame.eternal.juststore.commands.MainCommand;
import net.sakuragame.eternal.juststore.core.ShopManger;
import net.sakuragame.eternal.juststore.core.StoreManager;
import net.sakuragame.eternal.juststore.core.UserManager;
import net.sakuragame.eternal.juststore.file.FileManager;
import net.sakuragame.eternal.juststore.listener.ShopListener;
import net.sakuragame.eternal.juststore.listener.PlayerListener;
import net.sakuragame.eternal.juststore.listener.StoreListener;
import net.sakuragame.eternal.juststore.storge.StorageManager;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public class JustStore extends JavaPlugin {
    @Getter private static JustStore instance;

    @Getter private static FileManager fileManager;
    @Getter private static StoreManager storeManager;
    @Getter private static ShopManger shopManger;
    @Getter private static StorageManager storageManager;
    @Getter private static UserManager userManager;

    @Override
    public void onEnable() {
        long start = System.currentTimeMillis();

        instance = this;

        fileManager = new FileManager(this);
        fileManager.init();

        storeManager = new StoreManager(this);
        storeManager.init();

        shopManger = new ShopManger(this);
        shopManger.init();

        storageManager = new StorageManager();

        userManager = new UserManager();

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
        storeManager.init();
        shopManger.init();
    }
}
