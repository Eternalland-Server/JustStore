package net.sakuragame.eternal.juststore.api.event;

import lombok.Getter;
import net.sakuragame.eternal.juststore.core.store.StoreType;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerEvent;

@Getter
public class StorePurchasedEvent extends PlayerEvent {

    private final StoreType type;
    private final String commodityID;
    private final int amount;

    private final static HandlerList handlerList = new HandlerList();

    public StorePurchasedEvent(Player who, StoreType type, String commodityID, int amount) {
        super(who);
        this.type = type;
        this.commodityID = commodityID;
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
