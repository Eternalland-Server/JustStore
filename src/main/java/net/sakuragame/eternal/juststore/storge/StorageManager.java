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

    public HashMap<String, Integer> getUserData(UUID uuid) {
        Map<String, String> result = connection.sync().hgetall(getUserKey(uuid));
        if (result == null) return new HashMap<>();

        HashMap<String, Integer> map = new HashMap<>();
        result.forEach((key, value) -> map.put(key, Integer.parseInt(value)));

        return map;
    }

    public void setUserData(UUID uuid, String id, int amount, boolean expire) {
        String key = getUserKey(uuid);
        this.connection.async().hset(key, id, String.valueOf(amount));
        if (expire) {
            this.connection.async().expire(key, Utils.getNextDayTime());
        }
    }

    public static String getUserKey(UUID uuid) {
        return "JustStore:" + uuid.toString();
    }

    public StatefulRedisConnection<String, String> getConnection() {
        return connection;
    }
}
