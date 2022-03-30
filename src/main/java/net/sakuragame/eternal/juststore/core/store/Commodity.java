package net.sakuragame.eternal.juststore.core.store;

import lombok.AllArgsConstructor;
import lombok.Getter;
import net.sakuragame.eternal.juststore.core.Charge;
import net.sakuragame.eternal.juststore.util.Utils;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

@Getter
@AllArgsConstructor
public class Commodity {

    private final String id;
    private final String item;
    private final Tag tag;
    private final boolean single;
    private final String name;
    private final int amount;
    private final Charge charge;
    private final Double price;

    public Commodity(String id, ConfigurationSection section) {
        this.id = id;
        this.item = section.getString("item");
        this.tag = Tag.valueOf(section.getString("tag", "NONE").toUpperCase());
        this.single = section.getBoolean("single");
        this.name = section.getString("name");
        this.amount = section.getInt("amount");
        this.charge = Charge.valueOf(section.getString("charge").toUpperCase());
        this.price = section.getDouble("price");
    }

    public String getPriceFormat(Player player) {
        double lastPrice = getPrice() * Utils.getDiscount(player);
        String s = Utils.unitFormatting(lastPrice);
        return charge.getSymbol() + " " + s + " " + charge.getColor() + charge.getCurrency().getDisplay();
    }

    public String getSlot() {
        return "store_" + id + "_slot";
    }

}