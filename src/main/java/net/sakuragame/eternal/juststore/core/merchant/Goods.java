package net.sakuragame.eternal.juststore.core.merchant;

import com.taylorswiftcn.justwei.util.MegumiUtil;
import lombok.Getter;
import net.sakuragame.eternal.juststore.core.EnumCharge;
import net.sakuragame.eternal.juststore.util.Utils;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

import java.util.LinkedHashMap;
import java.util.Map;

@Getter
public abstract class Goods {

    private final String ID;
    private final TradeType type;
    private final String item;
    private final String name;
    private boolean single;
    private final int amount;
    private final EnumCharge charge;
    private final double price;
    private final Map<String, Integer> consume;

    public Goods(String ID, TradeType type, ConfigurationSection section) {
        this.ID = ID;
        this.type = type;
        this.item = section.getString("item");
        this.name = MegumiUtil.onReplace(section.getString("name"));
        this.single = section.getBoolean("single");
        this.amount = section.getInt("amount", 1);
        this.charge = EnumCharge.valueOf(section.getString("charge").toUpperCase());
        this.price = section.getDouble("price", 0);
        this.consume = new LinkedHashMap<>();
        for (String key : section.getStringList("consume")) {
            String[] args = key.split(":", 2);
            String consumeID = args[0];
            int amount = Integer.parseInt(args[1]);
            consume.put(consumeID, amount);
        }
    }

    public abstract void trade(Player player);

    public abstract void trade(Player player, int quantity);

    public void setSingle(boolean single) {
        this.single = single;
    }

    public String formatPrice() {
        if (this.charge == EnumCharge.NONE) return "&f不收费";

        String parse = Utils.unitFormatting(this.getPrice());
        return this.charge.getSymbol() + parse + charge.getCurrency().getDisplay();
    }

    public String getGoodsSlot() {
        return "merchant_" + this.ID + "_s";
    }

    public String getConsumeSlot(int index, int i) {
        return "merchant_c_" + index + "_" + i;
    }

    public Map<String, Integer> getConsume() {
        return new LinkedHashMap<>(this.consume);
    }

    public Map<String, Integer> getConsume(int multiply) {
        Map<String, Integer> result = new LinkedHashMap<>();
        for (String key : this.consume.keySet()) {
            result.put(key, this.consume.get(key) * multiply);
        }

        return result;
    }
}
