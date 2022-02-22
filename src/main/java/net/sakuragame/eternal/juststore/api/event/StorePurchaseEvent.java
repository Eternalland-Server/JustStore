package net.sakuragame.eternal.juststore.api.event;

import lombok.Getter;
import net.sakuragame.eternal.juststore.core.store.StoreType;
import org.bukkit.entity.Player;

@Getter
public class StorePurchaseEvent extends JustEvent {

    private final StoreType type;
    private final String commodityID;
    private final int quantity;

    public StorePurchaseEvent(Player who, StoreType type, String commodityID, int quantity) {
        super(who);
        this.type = type;
        this.commodityID = commodityID;
        this.quantity = quantity;
    }
}
