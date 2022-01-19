package net.sakuragame.eternal.juststore.api.event;

import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerEvent;

public class ShopPurchaseEvent extends PlayerEvent implements Cancellable {

    @Getter private final String shopID;
    @Getter private final String category;
    @Getter private final String commodityID;
    @Getter private final int amount;
    private boolean cancel;

    private final static HandlerList handlerList = new HandlerList();

    public ShopPurchaseEvent(Player who, String shopID, String category, String commodityID, int amount) {
        super(who);
        this.shopID = shopID;
        this.category = category;
        this.commodityID = commodityID;
        this.amount = amount;
    }

    @Override
    public boolean isCancelled() {
        return this.cancel;
    }

    @Override
    public void setCancelled(boolean cancel) {
        this.cancel = cancel;
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