package net.sakuragame.eternal.juststore.util;

import com.taylorswiftcn.justwei.util.MegumiUtil;
import ink.ptms.zaphkiel.ZaphkielAPI;
import ink.ptms.zaphkiel.api.Item;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;

public class Utils {

    private final static ZaphkielAPI zap = ZaphkielAPI.INSTANCE;

    public static boolean checkItem(Player player, HashMap<String, Integer> consume) {
        for (ItemStack item : player.getInventory().getContents()) {
            if (MegumiUtil.isEmpty(item)) continue;

            Item zapItem = zap.getItem(item);
            if (zapItem == null) continue;
            String id = zapItem.getId();

            if (!consume.containsKey(id)) continue;

            int surplus = consume.get(id) - item.getAmount();
            if (surplus <= 0) {
                consume.remove(id);
            }
            else {
                consume.put(id, surplus);
            }

            if (consume.size() == 0) return true;
        }

        return false;
    }

    public static void consumeItem(Player player, HashMap<String, Integer> consume) {
        System.out.println(consume.keySet());
        for (int i = 0; i < player.getInventory().getSize(); i++) {
            ItemStack item = player.getInventory().getItem(i);
            if (MegumiUtil.isEmpty(item)) continue;

            Item zapItem = zap.getItem(item);
            if (zapItem == null) continue;
            String id = zapItem.getId();

            if (!consume.containsKey(id)) continue;

            int surplus = consume.get(id) - item.getAmount();
            if (surplus <= 0) {
                int abs = Math.abs(surplus);
                if (abs == 0) {
                    player.getInventory().setItem(i, new ItemStack(Material.AIR));
                }
                else {
                    item.setAmount(abs);
                    player.getInventory().setItem(i, item);
                }
                consume.remove(id);
            }
            else {
                player.getInventory().setItem(i, new ItemStack(Material.AIR));
                consume.put(id, surplus);
            }

            if (consume.size() == 0) return;
        }
    }
}
