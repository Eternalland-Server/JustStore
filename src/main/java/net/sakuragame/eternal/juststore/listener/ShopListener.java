package net.sakuragame.eternal.juststore.listener;

import com.taylorswiftcn.megumi.uifactory.event.comp.UIFCompSubmitEvent;
import net.sakuragame.eternal.justmessage.api.event.quantity.QuantityBoxCancelEvent;
import net.sakuragame.eternal.justmessage.api.event.quantity.QuantityBoxConfirmEvent;
import net.sakuragame.eternal.justmessage.screen.ui.QuantityScreen;
import net.sakuragame.eternal.juststore.JustStore;
import net.sakuragame.eternal.juststore.core.shop.ShopCategory;
import net.sakuragame.eternal.juststore.core.shop.ShopOrder;
import net.sakuragame.eternal.juststore.ui.Operation;
import net.sakuragame.eternal.juststore.ui.screen.ShopScreen;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.util.UUID;

public class ShopListener implements Listener {

    @EventHandler
    public void onSubmit(UIFCompSubmitEvent e) {
        Player player = e.getPlayer();

        String screenID = e.getScreenID();
        if (!screenID.equals(ShopScreen.screenID)) return;

        String plugin = e.getParams().getParam(0);
        if (!plugin.equals(JustStore.getInstance().getName())) return;

        int i = e.getParams().getParamI(1);
        Operation operation = Operation.match(i);
        if (operation == null) return;

        if (operation == Operation.Category) {
            int id = e.getParams().getParamI(2);
            ShopCategory category = ShopCategory.match(id);
            handleCategory(player, category);
            return;
        }

        if (operation == Operation.Buy) {
            int id = e.getParams().getParamI(2);
            ShopCategory category = ShopCategory.match(id);
            if (category == null) return;

            String goodsID = e.getParams().getParam(3);
            JustStore.getMallManager().shopBuying(player, category, goodsID);
        }
    }

    private void handleCategory(Player player, ShopCategory category) {
        UUID uuid = player.getUniqueId();
        String openShop = JustStore.getMallManager().getOpenShop(uuid);
        if (openShop == null) {
            player.closeInventory();
            return;
        }

        JustStore.getMallManager().openShop(player, openShop, category);
    }

    @EventHandler
    public void onOrderConfirm(QuantityBoxConfirmEvent e) {
        Player player = e.getPlayer();
        UUID uuid = player.getUniqueId();

        String key = e.getKey();
        if (!key.equals(Operation.ShopOrder.name())) return;

        ShopOrder order = JustStore.getMallManager().getShopOrder(uuid);
        if (order == null) return;

        int count = e.getCount();

        JustStore.getMallManager().shopBuying(player, ShopCategory.Material, order.getGoodsID(), count);
        QuantityScreen.hide(player);
    }

    @EventHandler
    public void onOrderCancel(QuantityBoxCancelEvent e) {
        Player player = e.getPlayer();

        String key = e.getKey();
        if (!key.equals(Operation.ShopOrder.name())) return;

        e.setCancelled(true);
        JustStore.getMallManager().delShopOrder(player.getUniqueId());
        QuantityScreen.hide(player);
    }
}
