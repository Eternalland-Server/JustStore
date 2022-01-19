package net.sakuragame.eternal.juststore.core;

import lombok.Getter;
import net.sakuragame.eternal.juststore.JustStore;
import org.bukkit.Bukkit;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Getter
public class UserPurchaseData {

    private final UUID uuid;
    private Map<String, Integer> records;

    public UserPurchaseData(UUID uuid) {
        this.uuid = uuid;
        this.records = new HashMap<>();
    }

    public UserPurchaseData(UUID uuid, Map<String, Integer> records) {
        this.uuid = uuid;
        this.records = records;
    }

    public void setRecords(Map<String, Integer> records) {
        this.records = records;
    }

    public int getCount(String id) {
        return records.getOrDefault(id, 0);
    }

    public int addRecord(String id, int amount) {
        boolean expire = records.size() == 0;
        int count = getCount(id) + amount;
        records.put(id, count);

        Bukkit.getScheduler().runTaskAsynchronously(JustStore.getInstance(), () -> {
            JustStore.getStorageManager().setUserData(uuid, id, count, expire);
        });

        return count;
    }
}
