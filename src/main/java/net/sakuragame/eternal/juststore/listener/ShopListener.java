package net.sakuragame.eternal.juststore.listener;

import com.taylorswiftcn.megumi.uifactory.event.comp.UIFCompSubmitEvent;
import net.sakuragame.eternal.justmessage.api.common.QuantityBox;
import net.sakuragame.eternal.justmessage.api.event.quantity.QuantityBoxCancelEvent;
import net.sakuragame.eternal.justmessage.api.event.quantity.QuantityBoxConfirmEvent;
import net.sakuragame.eternal.justmessage.screen.ui.QuantityScreen;
import net.sakuragame.eternal.juststore.JustStore;
import net.sakuragame.eternal.juststore.api.event.ShopTradeEvent;
import net.sakuragame.eternal.juststore.api.event.ShopTradedEvent;
import net.sakuragame.eternal.juststore.core.StoreManager;
import net.sakuragame.eternal.juststore.core.order.ShopOrder;
import net.sakuragame.eternal.juststore.core.shop.Shop;
import net.sakuragame.eternal.juststore.core.shop.goods.BuyGoods;
import net.sakuragame.eternal.juststore.core.shop.goods.Goods;
import net.sakuragame.eternal.juststore.ui.Operation;
import net.sakuragame.eternal.juststore.ui.ScreenManager;
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
            int category = e.getParams().getParamI(2);
            handleCategory(player, category);
            return;
        }

        if (operation == Operation.Trade) {
            String shopID = e.getParams().getParam(2);
            int category = e.getParams().getParamI(3);
            String goodsID = e.getParams().getParam(4);

            Shop shop = JustStore.getStoreManager().getShop(shopID);
            if (shop == null) return;

            Goods goods = shop.getGoods(category, goodsID);

            if (goods instanceof BuyGoods) {
                if (!goods.isSingle()) {
                    QuantityBox box = new QuantityBox(Operation.ShopOrder.name(), "&6&l购买数量", "&7" + goods.getName());
                    box.open(player, true);
                    StoreManager.addShopOrder(player.getUniqueId(), new ShopOrder(shopID, category, goodsID));
                    return;
                }
            }

            this.trade(player, shopID, category, goods, 1);
        }
    }

    private void handleCategory(Player player, int category) {
        UUID uuid = player.getUniqueId();
        String openShop = StoreManager.getOpenShop(uuid);
        if (openShop == null) {
            player.closeInventory();
            return;
        }

        ScreenManager.openShop(player, openShop, category);
    }

    @EventHandler
    public void onOrderConfirm(QuantityBoxConfirmEvent e) {
        Player player = e.getPlayer();
        UUID uuid = player.getUniqueId();

        String key = e.getKey();
        if (!key.equals(Operation.ShopOrder.name())) return;

        ShopOrder order = StoreManager.getShopOrder(uuid);
        if (order == null) return;

        Shop shop = JustStore.getStoreManager().getShop(order.getShopID());
        if (shop == null) return;

        Goods goods = shop.getGoods(order.getCategory(), order.getGoodsID());
        int count = e.getCount();

        this.trade(player, order.getShopID(), order.getCategory(), goods, count);
        QuantityScreen.hide(player);
    }

    @EventHandler
    public void onOrderCancel(QuantityBoxCancelEvent e) {
        Player player = e.getPlayer();

        String key = e.getKey();
        if (!key.equals(Operation.ShopOrder.name())) return;

        e.setCancelled(true);
        StoreManager.delShopOrder(player.getUniqueId());
        QuantityScreen.hide(player);
    }

    private void trade(Player player, String shopID, int categoryID, Goods goods, int quantity) {
        ShopTradeEvent tradeEvent = new ShopTradeEvent(player, shopID, categoryID, goods, quantity);
        tradeEvent.call();
        if (tradeEvent.isCancelled()) return;

        goods.trade(player, quantity);

        ShopTradedEvent tradedEvent = new ShopTradedEvent(player, shopID, categoryID, goods, quantity);
        tradedEvent.call();
    }
}
