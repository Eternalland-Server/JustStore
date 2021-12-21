package net.sakuragame.eternal.juststore.api.event;

import lombok.Getter;
import net.sakuragame.eternal.juststore.core.shop.ShopCategory;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerEvent;

@Getter
public class ShopPurchasedEvent extends PlayerEvent {

    private final String shopID;
    private final ShopCategory category;
    private final String goodsID;
    private final int amount;

    private final static HandlerList handlerList = new HandlerList();

    public ShopPurchasedEvent(Player who, String shopID, ShopCategory category, String goodsID, int amount) {
        super(who);
        this.shopID = shopID;
        this.category = category;
        this.goodsID = goodsID;
        this.amount = amount;
    }

    @Override
    public HandlerList getHandlers() {
        return getHandlerList();
    }

    public static HandlerList getHandlerList() {
        return handlerList;
    }

    public void call() {
        Bukkit.getPluginManager().callEvent(this);
    }
}
