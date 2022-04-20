package net.sakuragame.eternal.juststore.listener;

import com.taylorswiftcn.megumi.uifactory.event.comp.UIFCompSubmitEvent;
import net.sakuragame.eternal.justmessage.api.common.QuantityBox;
import net.sakuragame.eternal.justmessage.api.event.quantity.QuantityBoxCancelEvent;
import net.sakuragame.eternal.justmessage.api.event.quantity.QuantityBoxConfirmEvent;
import net.sakuragame.eternal.justmessage.screen.ui.QuantityScreen;
import net.sakuragame.eternal.juststore.JustStore;
import net.sakuragame.eternal.juststore.api.event.MerchantTradeEvent;
import net.sakuragame.eternal.juststore.core.merchant.BuyGoods;
import net.sakuragame.eternal.juststore.core.merchant.Goods;
import net.sakuragame.eternal.juststore.ui.Operation;
import net.sakuragame.eternal.juststore.ui.ScreenManager;
import net.sakuragame.eternal.juststore.ui.view.MerchantUI;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.util.UUID;

public class MerchantListener implements Listener {

    @EventHandler
    public void onSubmit(UIFCompSubmitEvent e) {
        Player player = e.getPlayer();

        String screenID = e.getScreenID();
        if (!screenID.equals(MerchantUI.screenID)) return;

        int i = e.getParams().getParamI(0);
        Operation operation = Operation.match(i);
        if (operation == null) return;

        if (operation == Operation.Category) {
            String merchantID = e.getParams().getParam(1);
            String shelfID = e.getParams().getParam(2);
            ScreenManager.openMerchant(player, merchantID, shelfID);
            return;
        }

        if (operation == Operation.Trade) {
            String goodsID = e.getParams().getParam(1);
            Goods goods = JustStore.getMerchantManger().getGoods(goodsID);
            if (goods == null) return;

            if (goods instanceof BuyGoods) {
                if (!goods.isSingle()) {
                    JustStore.getMerchantManger().addCache(player.getUniqueId(), goodsID);
                    QuantityBox box = new QuantityBox(Operation.ShopOrder.name(), "&6&l购买数量", "&7" + goods.getName());
                    box.open(player, true);
                    return;
                }
            }

            this.trade(player, goods, 1);
        }
    }

    @EventHandler
    public void onOrderConfirm(QuantityBoxConfirmEvent e) {
        Player player = e.getPlayer();
        UUID uuid = player.getUniqueId();

        String key = e.getKey();
        if (!key.equals(Operation.ShopOrder.name())) return;

        String goodsID = JustStore.getMerchantManger().getCache(uuid);
        if (goodsID == null) return;

        Goods goods = JustStore.getMerchantManger().getGoods(goodsID);
        int count = e.getCount();

        this.trade(player, goods, count);
        QuantityScreen.hide(player);
    }

    @EventHandler
    public void onOrderCancel(QuantityBoxCancelEvent e) {
        Player player = e.getPlayer();

        String key = e.getKey();
        if (!key.equals(Operation.ShopOrder.name())) return;

        e.setCancelled(true);
        JustStore.getMerchantManger().delCache(player.getUniqueId());
        QuantityScreen.hide(player);
    }

    private void trade(Player player, Goods goods, int quantity) {
        MerchantTradeEvent.Pre preEvent = new MerchantTradeEvent.Pre(player, goods, quantity);
        preEvent.call();
        if (preEvent.isCancelled()) return;

        goods.trade(player, quantity);
    }
}
