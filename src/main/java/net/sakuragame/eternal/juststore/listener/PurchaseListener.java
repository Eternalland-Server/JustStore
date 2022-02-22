package net.sakuragame.eternal.juststore.listener;

import net.sakuragame.eternal.dragoncore.network.PacketSender;
import net.sakuragame.eternal.justmessage.api.MessageAPI;
import net.sakuragame.eternal.juststore.JustStore;
import net.sakuragame.eternal.juststore.api.event.StorePurchaseEvent;
import net.sakuragame.eternal.juststore.api.event.StorePurchasedEvent;
import net.sakuragame.eternal.juststore.core.UserPurchaseData;
import net.sakuragame.eternal.juststore.file.sub.ConfigFile;
import net.sakuragame.eternal.juststore.ui.screen.StoreScreen;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

public class PurchaseListener implements Listener {

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPurchasePre(StorePurchaseEvent e) {
        if (e.isCancelled()) return;

        String id = e.getCommodityID();
        Integer limit = ConfigFile.purchaseLimit.get(id);

        if (limit == null) return;

        Player player = e.getPlayer();
        UserPurchaseData account = JustStore.getUserManager().getAccount(player);

        int amount = e.getQuantity();
        int current = account.getCount(id);
        if (current + amount <= limit) return;

        e.setCancelled(true);
        if (current == limit) {
            MessageAPI.sendActionTip(player, "&c&l该商品今日购买次数已达到上限!");
        }
        else {
            MessageAPI.sendActionTip(player, "&c&l该商品你还能购买 &a&l" + (limit - current) + " &c&l次");
        }
    }

    @EventHandler
    public void onPurchasePost(StorePurchasedEvent e) {
        String id = e.getCommodityID();
        Integer limit = ConfigFile.purchaseLimit.get(id);

        if (limit == null) return;

        Player player = e.getPlayer();
        UserPurchaseData account = JustStore.getUserManager().getAccount(player);

        int count = account.addRecord(id, e.getQuantity());

        updateButton(player, e.getCommodityID(), count, limit);
    }

    private void updateButton(Player player, String id, int current, int limit) {
        String buttonID = id + "_buy";
        String text = "&f&l购买(" + current + "/" + limit + ")";
        PacketSender.sendRunFunction(player, StoreScreen.screenID, "func.Component_Set('" + buttonID + "', 'text', '" + text + "');", false);
    }
}
