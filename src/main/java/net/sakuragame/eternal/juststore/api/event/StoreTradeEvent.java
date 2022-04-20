package net.sakuragame.eternal.juststore.api.event;

import lombok.Getter;
import net.sakuragame.eternal.juststore.core.store.Commodity;
import org.bukkit.entity.Player;

public class StoreTradeEvent {
    
    @Getter
    public static class Pre extends JustEvent {
        private final Commodity commodity;
        private final int quantity;

        public Pre(Player who, Commodity commodity, int quantity) {
            super(who);
            this.commodity = commodity;
            this.quantity = quantity;
        }
    }

    @Getter
    public static class Post extends JustEvent {
        private final Commodity commodity;
        private final int quantity;

        public Post(Player who, Commodity commodity, int quantity) {
            super(who);
            this.commodity = commodity;
            this.quantity = quantity;
        }
    }
}
