package net.sakuragame.eternal.juststore.api.event;

import lombok.Getter;
import org.bukkit.entity.Player;

@Getter
public class ShopSellEvent extends JustEvent {

    private final String shopID;
    private final String category;
    private final String goodsID;
    private final int amount;

    public ShopSellEvent(Player who, String shopID, String category, String goodsID, int amount) {
        super(who);
        this.shopID = shopID;
        this.category = category;
        this.goodsID = goodsID;
        this.amount = amount;
    }
}
