package net.sakuragame.eternal.juststore.core.shop;

import com.taylorswiftcn.justwei.util.MegumiUtil;
import com.taylorswiftcn.justwei.util.UnitConvert;
import lombok.AllArgsConstructor;
import lombok.Getter;
import net.sakuragame.eternal.juststore.core.Charge;
import net.sakuragame.eternal.juststore.util.Utils;
import org.bukkit.configuration.ConfigurationSection;

import java.util.LinkedHashMap;

@Getter
@AllArgsConstructor
public class Goods {

    private final String id;
    private final String item;
    private final String name;
    private final boolean single;
    private final boolean sell;
    private final int amount;
    private final Charge charge;
    private final Double price;
    private final LinkedHashMap<String, Integer> consume;

    public Goods(String id, ConfigurationSection section) {
        this.id = id;
        this.item = section.getString("item");
        this.name = MegumiUtil.onReplace(section.getString("name"));
        this.single = section.getBoolean("single");
        this.sell = section.getBoolean("sell", false);
        this.amount = section.getInt("amount", 1);
        this.charge = Charge.valueOf(section.getString("charge").toUpperCase());
        this.price = section.getDouble("price");
        this.consume = new LinkedHashMap<>();
        for (String key : section.getStringList("consume")) {
            String[] args = key.split(":", 2);
            String consumeID = args[0];
            int amount = Integer.parseInt(args[1]);
            consume.put(consumeID, amount);
        }
    }

    public String getFormatPrice() {
        String s = getPrice() > 10000 ? UnitConvert.formatCN(UnitConvert.TenThousand, getPrice(), 2) : Utils.formatting(getPrice());
        return charge.getSymbol() + s + charge.getCurrency().getDisplay();
    }

    public String getGoodsSlot() {
        return "shop_" + id + "_slot";
    }

    public String getConsumeSlot(int i) {
        return id + "_consume_slot_" + i;
    }

    public LinkedHashMap<String, Integer> getConsume() {
        return new LinkedHashMap<>(consume);
    }

    public LinkedHashMap<String, Integer> getConsume(int i) {
        LinkedHashMap<String, Integer> map = new LinkedHashMap<>();

        for (String key : consume.keySet()) {
            int count = consume.get(key);
            map.put(key, count * i);
        }

        return map;
    }
}
