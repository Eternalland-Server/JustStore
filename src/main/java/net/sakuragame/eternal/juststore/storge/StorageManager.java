package net.sakuragame.eternal.juststore.storge;

import com.lambdaworks.redis.api.StatefulRedisConnection;
import net.sakuragame.eternal.juststore.util.Utils;
import net.sakuragame.serversystems.manage.client.api.ClientManagerAPI;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class StorageManager {

    private final StatefulRedisConnection<String, String> connection;

    public StorageManager() {
        this.connection = ClientManagerAPI.getRedisManager().getStandaloneConn();
    }

    public Map<String, Integer> getMallRecord(UUID uuid) {
        Map<String, String> result = connection.sync().hgetall(this.getMallKey(uuid));
        if (result == null) return new HashMap<>();

        HashMap<String, Integer> map = new HashMap<>();
        result.forEach((key, value) -> map.put(key, Integer.parseInt(value)));

        return map;
    }

    public void setMallRecord(UUID uuid, String id, int amount) {
        String key = this.getMallKey(uuid);
        this.connection.sync().hset(key, id, String.valueOf(amount));
        this.connection.sync().expire(key, Utils.getNextDayTime());
    }

    public Map<String, Integer> getShopRecord(UUID uuid) {
        Map<String, String> result = connection.sync().hgetall(this.getShopKey(uuid));
        if (result == null) return new HashMap<>();

        HashMap<String, Integer> map = new HashMap<>();
        result.forEach((key, value) -> map.put(key, Integer.parseInt(value)));

        return map;
    }

    public void setShopRecord(UUID uuid, String id, int amount) {
        String key = this.getShopKey(uuid);
        this.connection.sync().hset(key, id, String.valueOf(amount));
        this.connection.sync().expire(key, Utils.getNextDayTime());
    }

    public String getMallKey(UUID uuid) {
        return "JustStore:Mall:" + uuid.toString();
    }

    public String getShopKey(UUID uuid) {
        return "JustStore:Shop:" + uuid.toString();
    }
}
