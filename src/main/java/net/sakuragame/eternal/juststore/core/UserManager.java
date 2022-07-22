package net.sakuragame.eternal.juststore.core;

import net.sakuragame.eternal.juststore.JustStore;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.UUID;

public class UserManager {

    private final HashMap<UUID, UserAccount> account;

    public UserManager() {
        this.account = new HashMap<>();
    }

    public void loadAccount(UUID uuid) {
        UserAccount data = new UserAccount(uuid);
        data.setMallRecord(JustStore.getStorageManager().getMallRecord(uuid));
        data.setShopRecord(JustStore.getStorageManager().getShopRecord(uuid));

        account.put(uuid, data);
    }

    public UserAccount getAccount(Player player) {
        return getAccount(player.getUniqueId());
    }

    public UserAccount getAccount(UUID uuid) {
        return account.get(uuid);
    }

    public void removeAccount(UUID uuid) {
        account.remove(uuid);
    }
}
