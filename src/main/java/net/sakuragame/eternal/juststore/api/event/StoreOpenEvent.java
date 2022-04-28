package net.sakuragame.eternal.juststore.api.event;

import lombok.Getter;
import net.sakuragame.eternal.juststore.core.store.StoreType;
import org.bukkit.entity.Player;

@Getter
public class StoreOpenEvent extends JustEvent {

    private StoreType type;

    public StoreOpenEvent(Player who, StoreType type) {
        super(who);
        this.type = type;
    }

    public void setType(StoreType type) {
        this.type = type;
    }
}
