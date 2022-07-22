package net.sakuragame.eternal.juststore.core;

import lombok.Getter;
import net.sakuragame.eternal.juststore.JustStore;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Getter
public class UserAccount {

    private final UUID uuid;
    private Map<String, Integer> mallRecord;
    private Map<String, Integer> shopRecord;

    public UserAccount(UUID uuid) {
        this.uuid = uuid;
        this.mallRecord = new HashMap<>();
        this.shopRecord = new HashMap<>();
    }

    public void setMallRecord(Map<String, Integer> mallRecord) {
        this.mallRecord = mallRecord;
    }

    public void setShopRecord(Map<String, Integer> shopRecord) {
        this.shopRecord = shopRecord;
    }

    public int getMallCount(String id) {
        return this.mallRecord.getOrDefault(id, 0);
    }

    public int getShopCount(String id) {
        return this.shopRecord.getOrDefault(id, 0);
    }

    public int addMallCount(String id, int amount) {
        int count = this.getMallCount(id) + amount;
        this.mallRecord.put(id, count);

        JustStore.getStorageManager().setMallRecord(this.uuid, id, count);

        return count;
    }

    public int addShopCount(String id, int amount) {
        int count = this.getShopCount(id) + amount;
        this.shopRecord.put(id, count);

        JustStore.getStorageManager().setShopRecord(this.uuid, id, count);

        return count;
    }
}
