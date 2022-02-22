package net.sakuragame.eternal.juststore.util;

import com.taylorswiftcn.justwei.util.MegumiUtil;
import com.taylorswiftcn.justwei.util.UnitConvert;
import ink.ptms.zaphkiel.ZaphkielAPI;
import ink.ptms.zaphkiel.api.Item;
import net.sakuragame.eternal.juststore.file.sub.ConfigFile;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.text.DecimalFormat;
import java.util.Calendar;
import java.util.Map;

public class Utils {

    private final static ZaphkielAPI zap = ZaphkielAPI.INSTANCE;
    private final static DecimalFormat a = new DecimalFormat("0");

    public static double getDiscount(Player player) {
        for (String key : ConfigFile.discount.keySet()) {
            double discount = ConfigFile.discount.get(key);
            if (!player.hasPermission("eternal." + key)) continue;
            return discount;
        }

        return 1;
    }

    public static String formatting(double value) {
        return a.format(value);
    }

    public static String unitFormatting(double value) {
        if (value > 100000) {
            UnitConvert.formatCN(UnitConvert.TenThousand, value);
        }
        return formatting(value);
    }

    public static boolean checkItem(Player player, Map<String, Integer> consume) {
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

    public static void consumeItem(Player player, Map<String, Integer> consume) {
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

    public static long getNextDayTime() {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_MONTH, 1);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);

        return calendar.getTimeInMillis() - System.currentTimeMillis();
    }
}
