package net.sakuragame.eternal.juststore.listener;

import com.taylorswiftcn.megumi.uifactory.event.comp.UIFCompSubmitEvent;
import net.sakuragame.eternal.dragoncore.network.PacketSender;
import net.sakuragame.eternal.justmessage.api.MessageAPI;
import net.sakuragame.eternal.justmessage.api.common.QuantityBox;
import net.sakuragame.eternal.justmessage.api.event.quantity.QuantityBoxCancelEvent;
import net.sakuragame.eternal.justmessage.api.event.quantity.QuantityBoxConfirmEvent;
import net.sakuragame.eternal.justmessage.screen.ui.QuantityScreen;
import net.sakuragame.eternal.juststore.JustStore;
import net.sakuragame.eternal.juststore.api.event.ShopTradeEvent;
import net.sakuragame.eternal.juststore.core.UserAccount;
import net.sakuragame.eternal.juststore.core.merchant.goods.BuyGoods;
import net.sakuragame.eternal.juststore.core.merchant.goods.Goods;
import net.sakuragame.eternal.juststore.ui.Operation;
import net.sakuragame.eternal.juststore.ui.ScreenManager;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

import java.util.UUID;

public class ShopListener implements Listener {

    @EventHandler
    public void onSubmit(UIFCompSubmitEvent e) {
        Player player = e.getPlayer();

        String screenID = e.getScreenID();
        if (!screenID.equals(ScreenManager.Shop_ID)) return;

        String s = e.getParams().getParam(0);
        if (!s.equals("Trade")) return;

        int i = e.getParams().getParamI(1);
        Operation operation = Operation.match(i);
        if (operation == null) return;

        if (operation == Operation.Category) {
            String merchantID = e.getParams().getParam(2);
            String shelfID = e.getParams().getParam(3);
            ScreenManager.openShop(player, merchantID, shelfID);
            return;
        }

        if (operation == Operation.Trade) {
            String goodsID = e.getParams().getParam(2);
            Goods goods = JustStore.getShopManger().getGoods(goodsID);
            if (goods == null) return;

            if (!goods.isSingle()) {
                JustStore.getShopManger().addCache(player.getUniqueId(), goodsID);
                QuantityBox box = new QuantityBox(Operation.ShopOrder.name(), "&6&l交易数量", "&7" + goods.getName());
                box.open(player, true);
                return;
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

        String goodsID = JustStore.getShopManger().getCache(uuid);
        if (goodsID == null) return;

        Goods goods = JustStore.getShopManger().getGoods(goodsID);
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
        JustStore.getShopManger().delCache(player.getUniqueId());
        QuantityScreen.hide(player);
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onTradePre(ShopTradeEvent.Pre e) {
        if (e.isCancelled()) return;

        Goods goods = e.getGoods();
        int limit = goods.getLimit();
        if (limit <= 0) return;

        Player player = e.getPlayer();
        UserAccount account = JustStore.getUserManager().getAccount(player);

        int amount = e.getQuantity();
        int current = account.getShopCount(goods.getID());
        if (current + amount <= limit) return;

        e.setCancelled(true);
        if (current == limit) {
            player.playSound(player.getLocation(), Sound.ENTITY_VILLAGER_NO, 0.6f, 1);
            MessageAPI.sendActionTip(player, "&c&l该商品今日购买次数已达到上限!");
        }
    }

    @EventHandler
    public void onTradePost(ShopTradeEvent.Post e) {
        Player player = e.getPlayer();
        player.playSound(player.getLocation(), Sound.ENTITY_VILLAGER_TRADING, 0.6f, 1);

        Goods goods = e.getGoods();
        int limit = goods.getLimit();

        if (limit <= 0) return;

        UserAccount account = JustStore.getUserManager().getAccount(player);
        int count = account.addShopCount(goods.getID(), e.getQuantity());

        PacketSender.sendRunFunction(
                player,
                ScreenManager.Shop_ID,
                "func.Component_Set('l_" + goods.getID() + "', 'text', '(" + count + "/" + limit + ")');",
                false
        );
    }

    private void trade(Player player, Goods goods, int quantity) {
        ShopTradeEvent.Pre preEvent = new ShopTradeEvent.Pre(player, goods, quantity);
        preEvent.call();
        if (preEvent.isCancelled()) return;

        goods.trade(player, quantity);
    }
}
