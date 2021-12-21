package net.sakuragame.eternal.juststore.listener;

import net.sakuragame.eternal.juststore.JustStore;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerListener implements Listener {

    @EventHandler
    public void onQuit(PlayerQuitEvent e) {
        Player player = e.getPlayer();
        JustStore.getMallManager().clearDate(player.getUniqueId());
    }
}
