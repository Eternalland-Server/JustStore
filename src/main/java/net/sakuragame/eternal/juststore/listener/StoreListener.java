package net.sakuragame.eternal.juststore.listener;

import com.taylorswiftcn.megumi.uifactory.event.comp.UIFCompSubmitEvent;
import net.sakuragame.eternal.dragoncore.network.PacketSender;
import net.sakuragame.eternal.justmessage.api.MessageAPI;
import net.sakuragame.eternal.justmessage.api.event.quantity.QuantityBoxCancelEvent;
import net.sakuragame.eternal.justmessage.api.event.quantity.QuantityBoxConfirmEvent;
import net.sakuragame.eternal.justmessage.screen.ui.QuantityScreen;
import net.sakuragame.eternal.juststore.JustStore;
import net.sakuragame.eternal.juststore.api.event.StoreTradeEvent;
import net.sakuragame.eternal.juststore.core.UserPurchaseData;
import net.sakuragame.eternal.juststore.core.store.StoreType;
import net.sakuragame.eternal.juststore.file.sub.ConfigFile;
import net.sakuragame.eternal.juststore.ui.Operation;
import net.sakuragame.eternal.juststore.ui.ScreenManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

import java.util.UUID;

public class StoreListener implements Listener {

    @EventHandler
    public void onHudSubmit(UIFCompSubmitEvent e) {
        Player player = e.getPlayer();
        if (!e.getScreenID().equals("esc")) return;
        if (!e.getCompID().equals("store")) return;

        ScreenManager.openStore(player, StoreType.Prop);
    }

    @EventHandler
    public void onSubmit(UIFCompSubmitEvent e) {
        Player player = e.getPlayer();

        String screenID = e.getScreenID();
        if (!screenID.equals(ScreenManager.Store_ID)) return;

        int i = e.getParams().getParamI(0);
        Operation operation = Operation.match(i);
        if (operation == null) return;

        if (operation == Operation.Category) {
            int id = e.getParams().getParamI(1);
            StoreType type = StoreType.match(id);
            if (type == null) return;

            ScreenManager.openStore(player, type);
            return;
        }

        if (operation == Operation.Trade) {
            String commodityID = e.getParams().getParam(1);
            JustStore.getStoreManager().trade(player, commodityID);
        }
    }

    @EventHandler
    public void onOrderConfirm(QuantityBoxConfirmEvent e) {
        Player player = e.getPlayer();
        UUID uuid = player.getUniqueId();

        String key = e.getKey();
        if (!key.equals(Operation.StoreOrder.name())) return;

        String id = JustStore.getStoreManager().getCache(uuid);
        if (id == null) return;

        int count = e.getCount();

        JustStore.getStoreManager().trade(player, id, count);
        QuantityScreen.hide(player);
    }

    @EventHandler
    public void onOrderCancel(QuantityBoxCancelEvent e) {
        Player player = e.getPlayer();

        String key = e.getKey();
        if (!key.equals(Operation.StoreOrder.name())) return;

        e.setCancelled(true);
        JustStore.getStoreManager().delCache(player.getUniqueId());
        QuantityScreen.hide(player);
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPurchasePre(StoreTradeEvent.Pre e) {
        if (e.isCancelled()) return;

        String id = e.getCommodity().getId();
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
    public void onPurchasePost(StoreTradeEvent.Post e) {
        String id = e.getCommodity().getId();
        Integer limit = ConfigFile.purchaseLimit.get(id);

        if (limit == null) return;

        Player player = e.getPlayer();
        UserPurchaseData account = JustStore.getUserManager().getAccount(player);

        int count = account.addRecord(id, e.getQuantity());

        updateButton(player, id, count, limit);
    }

    private void updateButton(Player player, String id, int current, int limit) {
        String buttonID = id + "_b";
        String text = "&f&l购买(" + current + "/" + limit + ")";
        PacketSender.sendRunFunction(player, ScreenManager.Store_ID, "func.Component_Set('" + buttonID + "', 'text', '" + text + "');", false);
    }

}
