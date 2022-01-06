package net.sakuragame.eternal.juststore.listener;

import com.taylorswiftcn.megumi.uifactory.event.comp.UIFCompSubmitEvent;
import net.sakuragame.eternal.dragoncore.api.CoreAPI;
import net.sakuragame.eternal.dragoncore.api.KeyPressEvent;
import net.sakuragame.eternal.justmessage.api.event.quantity.QuantityBoxCancelEvent;
import net.sakuragame.eternal.justmessage.api.event.quantity.QuantityBoxConfirmEvent;
import net.sakuragame.eternal.justmessage.screen.ui.QuantityScreen;
import net.sakuragame.eternal.juststore.JustStore;
import net.sakuragame.eternal.juststore.core.store.StoreOrder;
import net.sakuragame.eternal.juststore.core.store.StoreType;
import net.sakuragame.eternal.juststore.ui.Operation;
import net.sakuragame.eternal.juststore.ui.screen.StoreScreen;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.util.UUID;

public class StoreListener implements Listener {

    public StoreListener() {
        CoreAPI.registerKey("G");
    }

    @EventHandler
    public void onKeyPress(KeyPressEvent e) {
        Player player = e.getPlayer();
        String key = e.getKey();

        if (!key.equals("G")) return;

        JustStore.getMallManager().openStore(player, StoreType.Prop);
    }

    @EventHandler
    public void onHudSubmit(UIFCompSubmitEvent e) {
        Player player = e.getPlayer();
        if (!e.getScreenID().equals("function_hud")) return;
        if (!e.getCompID().equals("store")) return;

        JustStore.getMallManager().openStore(player, StoreType.Prop);
    }

    @EventHandler
    public void onSubmit(UIFCompSubmitEvent e) {
        Player player = e.getPlayer();

        String screenID = e.getScreenID();
        if (!screenID.equals(StoreScreen.screenID)) return;

        String plugin = e.getParams().getParam(0);
        if (!plugin.equals(JustStore.getInstance().getName())) return;

        int i = e.getParams().getParamI(1);
        Operation operation = Operation.match(i);
        if (operation == null) return;

        if (operation == Operation.Category) {
            int id = e.getParams().getParamI(2);
            StoreType type = StoreType.match(id);
            if (type == null) return;

            JustStore.getMallManager().openStore(player, type);
            return;
        }

        if (operation == Operation.Buy) {
            int category = e.getParams().getParamI(2);
            String commodityID = e.getParams().getParam(3);

            StoreType type = StoreType.match(category);
            if (type == null) return;

            JustStore.getMallManager().storeBuying(player, type, commodityID);
        }
    }

    @EventHandler
    public void onOrderConfirm(QuantityBoxConfirmEvent e) {
        Player player = e.getPlayer();
        UUID uuid = player.getUniqueId();

        String key = e.getKey();
        if (!key.equals(Operation.StoreOrder.name())) return;

        StoreOrder order = JustStore.getMallManager().getStoreOrder(uuid);
        if (order == null) return;

        int count = e.getCount();

        JustStore.getMallManager().storeBuying(player, order.getType(), order.getCommodityID(), count);
        QuantityScreen.hide(player);
    }

    @EventHandler
    public void onOrderCancel(QuantityBoxCancelEvent e) {
        Player player = e.getPlayer();

        String key = e.getKey();
        if (!key.equals(Operation.StoreOrder.name())) return;

        e.setCancelled(true);
        JustStore.getMallManager().delStoreOrder(player.getUniqueId());
        QuantityScreen.hide(player);
    }
}
