package net.sakuragame.eternal.juststore.api.event;

import lombok.Getter;
import net.sakuragame.eternal.juststore.core.shop.goods.Goods;
import org.bukkit.entity.Player;

@Getter
public class ShopTradedEvent extends JustEvent {

    private final String shopID;
    private final int categoryID;
    private final Goods goods;
    private final int quantity;

    public ShopTradedEvent(Player who, String shopID, int categoryID, Goods goods, int quantity) {
        super(who);
        this.shopID = shopID;
        this.categoryID = categoryID;
        this.goods = goods;
        this.quantity = quantity;
    }
}
