package net.sakuragame.eternal.juststore.core;

import net.sakuragame.eternal.juststore.JustStore;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.UUID;

public class UserManager {

    private final HashMap<UUID, UserPurchaseData> account;

    public UserManager() {
        this.account = new HashMap<>();
    }

    public void loadAccount(UUID uuid) {
        UserPurchaseData data = new UserPurchaseData(uuid);
        data.setRecords(JustStore.getStorageManager().getUserData(uuid));

        account.put(uuid, data);
    }

    public UserPurchaseData getAccount(Player player) {
        return getAccount(player.getUniqueId());
    }

    public UserPurchaseData getAccount(UUID uuid) {
        return account.get(uuid);
    }

    public void removeAccount(UUID uuid) {
        account.remove(uuid);
    }
}
