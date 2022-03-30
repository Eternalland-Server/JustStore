package net.sakuragame.eternal.juststore.core.shop.goods;

import com.taylorswiftcn.justwei.util.MegumiUtil;
import lombok.Getter;
import net.sakuragame.eternal.juststore.core.Charge;
import net.sakuragame.eternal.juststore.core.shop.TradeType;
import net.sakuragame.eternal.juststore.util.Utils;
import org.bukkit.configuration.ConfigurationSection;

import java.util.LinkedHashMap;

@Getter
public abstract class Goods implements GoodsImpl {

    private final String id;
    private final TradeType type;
    private final String item;
    private final String name;
    private boolean single;
    private final int amount;
    private final Charge charge;
    private final Double price;
    private final LinkedHashMap<String, Integer> consume;

    public Goods(String id, TradeType type, ConfigurationSection section) {
        this.id = id;
        this.type = type;
        this.item = section.getString("item");
        this.name = MegumiUtil.onReplace(section.getString("name"));
        this.single = section.getBoolean("single");
        this.amount = section.getInt("amount", 1);
        this.charge = Charge.valueOf(section.getString("charge").toUpperCase());
        this.price = section.getDouble("price", 0);
        this.consume = new LinkedHashMap<>();
        for (String key : section.getStringList("consume")) {
            String[] args = key.split(":", 2);
            String consumeID = args[0];
            int amount = Integer.parseInt(args[1]);
            consume.put(consumeID, amount);
        }
    }

    public void setSingle(boolean single) {
        this.single = single;
    }

    public static Goods newInstance(String id, TradeType type, ConfigurationSection section) {
        if (type == TradeType.BUY) return new BuyGoods(id, type, section);
        if (type == TradeType.SELL) return new SellGoods(id, type, section);
        return new UpgradeGoods(id, type, section);
    }

    public String getFormatPrice() {
        if (charge == Charge.NONE) return "&f不收费";

        String s = Utils.unitFormatting(getPrice());
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
