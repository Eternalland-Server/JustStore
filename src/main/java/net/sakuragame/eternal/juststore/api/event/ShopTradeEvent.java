package net.sakuragame.eternal.juststore.api.event;

import lombok.Getter;
import net.sakuragame.eternal.juststore.core.merchant.goods.Goods;
import org.bukkit.entity.Player;

public class ShopTradeEvent {

    @Getter
    public static class Pre extends JustEvent {
        private final Goods goods;
        private final int quantity;

        public Pre(Player who, Goods goods, int quantity) {
            super(who);
            this.goods = goods;
            this.quantity = quantity;
        }
    }

    @Getter
    public static class Post extends JustEvent {
        private final Goods goods;
        private final int quantity;

        public Post(Player who, Goods goods, int quantity) {
            super(who);
            this.goods = goods;
            this.quantity = quantity;
        }
    }
}
