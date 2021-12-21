package net.sakuragame.eternal.juststore.core.shop;

import lombok.AllArgsConstructor;
import lombok.Getter;
import net.sakuragame.eternal.juststore.core.common.Charge;
import org.bukkit.configuration.ConfigurationSection;

import java.util.HashMap;

@Getter
@AllArgsConstructor
public class Goods {

    private final String id;
    private final String item;
    private final String name;
    private final Charge charge;
    private final Double price;
    private final HashMap<String, Integer> consume;

    public Goods(String id, ConfigurationSection section) {
        this.id = id;
        this.item = section.getString("item");
        this.name = section.getString("name");
        this.charge = Charge.valueOf(section.getString("charge").toUpperCase());
        this.price = section.getDouble("price");
        this.consume = new HashMap<>();
        for (String key : section.getStringList("consume")) {
            String[] args = key.split(":", 2);
            String consumeID = args[0];
            int amount = Integer.parseInt(args[1]);
            consume.put(consumeID, amount);
        }
    }

    public String getFormatPrice() {
        return charge.formatting(price);
    }

    public String getGoodsSlot() {
        return "shop_" + id + "_slot";
    }

    public String getConsumeSlot(int i) {
        return id + "_consume_slot_" + i;
    }
}
