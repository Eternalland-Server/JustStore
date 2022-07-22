package net.sakuragame.eternal.juststore.core.merchant.goods;

import ink.ptms.zaphkiel.ZaphkielAPI;
import net.sakuragame.eternal.gemseconomy.api.GemsEconomyAPI;
import net.sakuragame.eternal.justmessage.api.MessageAPI;
import net.sakuragame.eternal.juststore.api.event.ShopTradeEvent;
import net.sakuragame.eternal.juststore.core.EnumCharge;
import net.sakuragame.eternal.juststore.core.merchant.TradeType;
import net.sakuragame.eternal.juststore.file.sub.ConfigFile;
import net.sakuragame.eternal.juststore.util.Utils;
import org.bukkit.Sound;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.UUID;

public class BuyGoods extends Goods {

    public BuyGoods(String ID, ConfigurationSection section) {
        super(ID, TradeType.BUY, section);
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

        if (Utils.getEmptySlotCount(player) == 0) {
            player.playSound(player.getLocation(), Sound.ENTITY_VILLAGER_NO, 0.6f, 1);
            player.sendMessage(ConfigFile.prefix + "交易前请确保背包内有空余的槽位");
            return;
        }

        if (charge != EnumCharge.NONE) {
            double balance = GemsEconomyAPI.getBalance(uuid, charge.getCurrency());
            if (balance < price) {
                MessageAPI.sendActionTip(player, "&c&l你没有足够的" + charge.getCurrency().getDisplay());
                return;
            }
        }

        if (this.getConsume().size() != 0) {
            if (!Utils.checkItem(player, this.getConsume(quantity))) {
                player.playSound(player.getLocation(), Sound.ENTITY_VILLAGER_NO, 0.6f, 1);
                MessageAPI.sendActionTip(player, "&c&l购买失败，背包内材料不足");
                return;
            }

            Utils.consumeItem(player, this.getConsume(quantity));
        }

        ItemStack boughtGoods = ZaphkielAPI.INSTANCE.getItemStack(this.getItem(), player);
        if (boughtGoods == null) {
            player.playSound(player.getLocation(), Sound.ENTITY_VILLAGER_NO, 0.6f, 1);
            player.sendMessage(" §c§l购买失败，请联系管理员");
            return;
        }
        if (charge != EnumCharge.NONE) charge.withdraw(player, price);

        boughtGoods.setAmount(quantity * this.getAmount());
        player.getInventory().addItem(boughtGoods);

        MessageAPI.sendActionTip(player, "&a&l购买成功！");
        player.sendMessage(ConfigFile.prefix + "你购买了: §f" + this.getName());
        player.playSound(player.getLocation(), Sound.ENTITY_VILLAGER_TRADING, 0.6f, 1);

        ShopTradeEvent.Post postEvent = new ShopTradeEvent.Post(player, this, quantity);
        postEvent.call();
    }
}
