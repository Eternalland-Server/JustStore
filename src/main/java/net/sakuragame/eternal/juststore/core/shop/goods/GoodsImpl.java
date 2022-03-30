package net.sakuragame.eternal.juststore.core.shop.goods;

import org.bukkit.entity.Player;

public interface GoodsImpl {

    void trade(Player player);

    void trade(Player player, int quantity);
}
