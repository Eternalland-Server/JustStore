package net.sakuragame.eternal.juststore.listener;

import net.sakuragame.eternal.juststore.JustStore;
import net.sakuragame.eternal.juststore.core.UserPurchaseData;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.UUID;

public class PlayerListener implements Listener {

    @EventHandler(priority = EventPriority.HIGH)
    public void onPreLogin(AsyncPlayerPreLoginEvent event) {
        if (event.getLoginResult() != AsyncPlayerPreLoginEvent.Result.ALLOWED) {
            return;
        }
        UUID uuid = event.getUniqueId();
        JustStore.getUserManager().loadAccount(uuid);
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onPreLoginMonitor(AsyncPlayerPreLoginEvent event) {
        if (event.getLoginResult() != AsyncPlayerPreLoginEvent.Result.ALLOWED) {
            JustStore.getUserManager().removeAccount(event.getUniqueId());
        }
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onPlayerLogin(PlayerLoginEvent e) {
        Player player = e.getPlayer();

        UserPurchaseData account = JustStore.getUserManager().getAccount(player.getUniqueId());

        if (account == null) {
            e.setResult(PlayerLoginEvent.Result.KICK_OTHER);
            e.setKickMessage("你的账户未被正确载入，请重新进入！");
        }
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent e) {
        Player player = e.getPlayer();
        JustStore.getStoreManager().clearDate(player.getUniqueId());
    }
}
