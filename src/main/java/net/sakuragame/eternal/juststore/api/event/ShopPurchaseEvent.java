package net.sakuragame.eternal.juststore.api.event;

import lombok.Getter;
import org.bukkit.entity.Player;

@Getter
public class ShopPurchaseEvent extends JustEvent {

    private final String shopID;
    private final String category;
    private final String goodsID;
    private final int quantity;

    public ShopPurchaseEvent(Player who, String shopID, String category, String goodsID, int quantity) {
        super(who);
        this.shopID = shopID;
        this.category = category;
        this.goodsID = goodsID;
        this.quantity = quantity;
    }
}