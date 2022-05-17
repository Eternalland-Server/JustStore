package net.sakuragame.eternal.juststore.core.merchant;

import ink.ptms.zaphkiel.ZaphkielAPI;
import net.sakuragame.eternal.gemseconomy.api.GemsEconomyAPI;
import net.sakuragame.eternal.justmessage.api.MessageAPI;
import net.sakuragame.eternal.juststore.api.event.MerchantTradeEvent;
import net.sakuragame.eternal.juststore.core.EnumCharge;
import net.sakuragame.eternal.juststore.file.sub.ConfigFile;
import net.sakuragame.eternal.juststore.util.Utils;
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

        if (charge != EnumCharge.NONE) {
            double balance = GemsEconomyAPI.getBalance(uuid, charge.getCurrency());
            if (balance < price) {
                MessageAPI.sendActionTip(player, "&c&l你没有足够的" + charge.getCurrency().getDisplay());
                return;
            }
        }

        if (this.getConsume().size() != 0) {
            if (!Utils.checkItem(player, this.getConsume(quantity))) {
                MessageAPI.sendActionTip(player, "&c&l购买失败，背包内材料不足");
                return;
            }

            Utils.consumeItem(player, this.getConsume(quantity));
        }

        ItemStack boughtGoods = ZaphkielAPI.INSTANCE.getItemStack(this.getItem(), player);
        if (boughtGoods == null) {
            player.sendMessage(" §c§l购买失败，请联系管理员");
            return;
        }
        if (charge != EnumCharge.NONE) charge.withdraw(player, price);

        boughtGoods.setAmount(quantity * this.getAmount());
        player.getInventory().addItem(boughtGoods);

        MessageAPI.sendActionTip(player, "&a&l购买成功！");
        player.sendMessage(ConfigFile.prefix + "你购买了 " + this.getName());

        MerchantTradeEvent.Post postEvent = new MerchantTradeEvent.Post(player, this, quantity);
        postEvent.call();
    }
}
