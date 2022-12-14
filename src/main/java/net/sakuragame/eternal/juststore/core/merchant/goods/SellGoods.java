package net.sakuragame.eternal.juststore.core.merchant.goods;

import net.sakuragame.eternal.gemseconomy.api.GemsEconomyAPI;
import net.sakuragame.eternal.gemseconomy.currency.EternalCurrency;
import net.sakuragame.eternal.justmessage.api.MessageAPI;
import net.sakuragame.eternal.juststore.api.event.ShopTradeEvent;
import net.sakuragame.eternal.juststore.core.EnumCharge;
import net.sakuragame.eternal.juststore.core.merchant.TradeType;
import net.sakuragame.eternal.juststore.file.sub.ConfigFile;
import net.sakuragame.eternal.juststore.util.Utils;
import org.bukkit.Sound;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class SellGoods extends Goods {

    public SellGoods(String ID, ConfigurationSection section) {
        super(ID, TradeType.SELL, section);
    }

    @Override
    public void trade(Player player) {
        this.trade(player, 1);
    }

    @Override
    public void trade(Player player, int quantity) {
        UUID uuid = player.getUniqueId();

        EnumCharge charge = this.getCharge();
        double price = this.getPrice() * quantity;

        Map<String, Integer> sellItem = new HashMap<>();
        sellItem.put(this.getItem(), quantity);
        if (!Utils.checkItem(player, new HashMap<>(sellItem))) {
            player.playSound(player.getLocation(), Sound.ENTITY_VILLAGER_NO, 0.6f, 1);
            MessageAPI.sendActionTip(player, "&c&l出售失败，你背包内没有足够的 &a&l" + this.getName());
            return;
        }

        EternalCurrency currency = charge.getCurrency();
        Utils.consumeItem(player, sellItem);
        GemsEconomyAPI.deposit(uuid, price, charge.getCurrency());
        MessageAPI.sendActionTip(player, "&a&l出售成功！");
        player.sendMessage(
                ConfigFile.prefix + "§7你出售了 §f" + this.getName() + (quantity == 1 ? "" : "*" + quantity) +
                " §7获得了 §f" + Utils.unitFormatting(price) + " §7" + currency.getDisplay());
        player.playSound(player.getLocation(), Sound.ENTITY_VILLAGER_TRADING, 0.6f, 1);

        ShopTradeEvent.Post postEvent = new ShopTradeEvent.Post(player, this, quantity);
        postEvent.call();
    }
}
