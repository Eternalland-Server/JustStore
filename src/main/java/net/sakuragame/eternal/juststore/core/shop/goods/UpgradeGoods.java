package net.sakuragame.eternal.juststore.core.shop.goods;

import ink.ptms.zaphkiel.ZaphkielAPI;
import ink.ptms.zaphkiel.api.ItemStream;
import ink.ptms.zaphkiel.taboolib.module.nms.ItemTag;
import ink.ptms.zaphkiel.taboolib.module.nms.ItemTagData;
import net.sakuragame.eternal.dragoncore.util.Pair;
import net.sakuragame.eternal.gemseconomy.api.GemsEconomyAPI;
import net.sakuragame.eternal.justmessage.api.MessageAPI;
import net.sakuragame.eternal.juststore.core.Charge;
import net.sakuragame.eternal.juststore.core.shop.TradeType;
import net.sakuragame.eternal.juststore.file.sub.ConfigFile;
import net.sakuragame.eternal.juststore.util.Utils;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.Map;
import java.util.UUID;

public class UpgradeGoods extends Goods {

    public UpgradeGoods(String id, TradeType type, ConfigurationSection section) {
        super(id, type, section);
        this.setSingle(true);
    }

    @Override
    public void trade(Player player) {
        this.trade(player, 1);
    }

    @Override
    public void trade(Player player, int quantity) {
        UUID uuid = player.getUniqueId();

        Charge charge = this.getCharge();
        double price = this.getPrice() * quantity;

        if (charge != Charge.NONE) {
            double balance = GemsEconomyAPI.getBalance(uuid, charge.getCurrency());
            if (balance < price) {
                MessageAPI.sendActionTip(player, "&c&l你没有足够的" + charge.getCurrency().getDisplay());
                return;
            }
        }

        Map<String, Integer> consume = this.getConsume();
        if (consume.size() == 0) return;

        if (!Utils.checkItem(player, this.getConsume(quantity))) {
            MessageAPI.sendActionTip(player, "&c&l购买失败，背包内材料不足");
            return;
        }

        String equipID = consume.entrySet().stream().findFirst().get().getKey();
        Pair<ItemTagData, ItemTagData> potencyData = Utils.getFirstEquipPotency(player, equipID);
        Utils.consumeItem(player, this.getConsume(quantity));

        ItemStream goodsItem = ZaphkielAPI.INSTANCE.getItem(this.getItem(), player);
        if (goodsItem == null) {
            player.sendMessage(" §c§l升级失败，请联系管理员");
            return;
        }
        if (potencyData != null) {
            ItemTag itemTag = goodsItem.getZaphkielData();
            itemTag.putDeep("justattribute.grade", potencyData.getKey());
            itemTag.putDeep("justattribute.potency", potencyData.getValue());
        }

        ItemStack result = goodsItem.rebuildToItemStack(player);

        charge.withdraw(player, price);

        result.setAmount(quantity * this.getAmount());
        player.getInventory().addItem(result);

        MessageAPI.sendActionTip(player, "&a&l升级成功！");
        player.sendMessage(ConfigFile.prefix + "你将装备升级成了 " + this.getName());
    }
}
