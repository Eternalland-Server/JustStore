package net.sakuragame.eternal.juststore.ui;

import com.taylorswiftcn.megumi.uifactory.generate.function.Statements;
import com.taylorswiftcn.megumi.uifactory.generate.function.SubmitParams;
import com.taylorswiftcn.megumi.uifactory.generate.type.ActionType;
import com.taylorswiftcn.megumi.uifactory.generate.ui.component.BasicComponent;
import com.taylorswiftcn.megumi.uifactory.generate.ui.component.base.LabelComp;
import com.taylorswiftcn.megumi.uifactory.generate.ui.component.base.SlotComp;
import com.taylorswiftcn.megumi.uifactory.generate.ui.component.base.TextureComp;
import com.taylorswiftcn.megumi.uifactory.generate.ui.component.custom.ScrollBarComp;
import ink.ptms.zaphkiel.ZaphkielAPI;
import net.sakuragame.eternal.dragoncore.network.PacketSender;
import net.sakuragame.eternal.juststore.JustStore;
import net.sakuragame.eternal.juststore.core.UserPurchaseData;
import net.sakuragame.eternal.juststore.core.merchant.Goods;
import net.sakuragame.eternal.juststore.core.merchant.TradeType;
import net.sakuragame.eternal.juststore.core.store.Commodity;
import net.sakuragame.eternal.juststore.core.store.Tag;
import net.sakuragame.eternal.juststore.file.sub.ConfigFile;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.*;

public class LayoutHandler {

    public static LinkedList<BasicComponent> build(Player player, int index, Goods goods) {
        LinkedList<BasicComponent> components = new LinkedList<>();

        String y = (index == 0 ? ScrollBarComp.EXTEND_ID + ".y" : "g_" + (index - 1) + ".y + 51");
        String bodyID = "g_" + index;
        String frameID = bodyID + "_f";
        String itemID = bodyID + "_i";
        String nameID = bodyID + "_n";
        String priceID = bodyID + "_p";
        String requireID = bodyID + "_r";
        String consumeID = bodyID + "_c";
        String operateID = bodyID + "_o";

        components.add(new TextureComp(bodyID)
                .setTexture("ui/store/shop/goods_bg.png")
                .setXY("goods_sub.x", y)
                .setCompSize("goods_sub.width", "52")
        );
        components.add(new TextureComp(frameID)
                .setTexture("ui/pack/slot_bg.png")
                .setXY(bodyID + ".x + 6", bodyID + ".y + 6")
                .setCompSize(40, 40)
        );
        components.add(new SlotComp(itemID, goods.getGoodsSlot())
                .setDrawBackground(false)
                .setXY(frameID + ".x + 2", frameID + ".y + 2")
                .setScale(2.25)
        );
        components.add(new LabelComp(nameID, goods.getName())
                .setX(frameID + ".x + 46")
                .setY(frameID + ".y + 2")
        );
        components.add(new LabelComp(priceID, goods.formatPrice())
                .setX(bodyID + ".x + (" + bodyID + ".width - " + priceID + ".width) -6")
                .setY(nameID + ".y")
        );
        components.add(new TextureComp(consumeID)
                .setXY(requireID + ".x", requireID + ".y + 9")
        );

        ItemStack item = ZaphkielAPI.INSTANCE.getItemStack(goods.getItem(), null);
        if (item != null) {
            item.setAmount(goods.getAmount());
        }
        PacketSender.putClientSlotItem(player, goods.getGoodsSlot(), item);

        TradeType type = goods.getType();
        if (type != TradeType.SELL) {
            Map<String, Integer> consume = goods.getConsume();
            List<String> keys = new ArrayList<>(consume.keySet());
            if (keys.size() != 0) {
                components.add(new LabelComp(requireID, "&7需要材料:")
                        .setX(nameID + ".x")
                        .setY(nameID + ".y + 12")
                );
                int i = 0;
                for (String key : consume.keySet()) {
                    ItemStack consumeItem = ZaphkielAPI.INSTANCE.getItemStack(key, null);
                    int amount = consume.get(key);
                    do {
                        String id = "g_" + index + "_sbg_" + i;
                        components.add(new TextureComp(id)
                                .setTexture("ui/pack/slot_bg.png")
                                .setX(consumeID + ".x + 19*" + i)
                                .setY(consumeID + ".y")
                                .setCompSize(16, 16)
                        );
                        components.add(new SlotComp("g_" + index + "_c_" + i, goods.getConsumeSlot(index, i))
                                .setDrawBackground(false)
                                .setXY(id + ".x + 2", id + ".y + 2")
                                .setScale(0.75)
                        );

                        if (consumeItem != null) {
                            if (amount > 64) {
                                consumeItem.setAmount(64);
                                amount = amount - 64;
                            }
                            else {
                                consumeItem.setAmount(amount);
                                amount = 0;
                            }
                            PacketSender.putClientSlotItem(player, goods.getConsumeSlot(index, i), consumeItem);
                        }
                        i++;
                    } while (amount != 0);
                }
            }
        }

        components.add(new TextureComp(operateID)
                .setTexture("ui/store/button/" + type.getNormal())
                .setXY(bodyID + ".x + 158", bodyID + ".y + 30")
                .setCompSize(40, 16)
                .addAction(ActionType.Left_Click, new Statements()
                        .add("func.Sound_Play();")
                        .add(operateID + ".texture = 'ui/store/button/" + type.getPress() + "';")
                        .build()
                )
                .addAction(ActionType.Left_Release, operateID + ".texture = 'ui/store/button/" + type.getNormal() + "';")
                .addAction(ActionType.Left_Release, new SubmitParams()
                        .addValue("Trade")
                        .addValue(Operation.Trade.getId())
                        .addValue(goods.getID())
                )
        );

        return components;
    }

    public static LinkedList<BasicComponent> build(Player player, int index, Commodity commodity) {
        LinkedList<BasicComponent> components = new LinkedList<>();

        String bodyID = "g_" + index;
        String nameID = bodyID + "_n";
        String priceID = bodyID + "_p";
        String itemID = bodyID + "_i";
        String tagID = bodyID + "_t";
        String buyID = commodity.getId() + "_b";

        String x = getX(index - 1);
        String y = getY(index - 1);

        components.add(new TextureComp(bodyID)
                .setTexture("ui/store/frame.png")
                .setXY(x, y)
                .setCompSize("80*(w/960)", "116*(w/960)")
        );
        components.add(new TextureComp(nameID)
                .setText(commodity.getName())
                .setTexture("0,0,0,0")
                .setXY(bodyID + ".x", bodyID + ".y + 65*(w/960)")
                .setCompSize("80", "10")
                .setScale("w/960")
        );
        components.add(new TextureComp(priceID)
                .setText(commodity.formatPrice(player))
                .setTexture("0,0,0,0")
                .setXY(bodyID + ".x", nameID + ".y + 11*(w/960)")
                .setCompSize("80", "10")
                .setScale("w/960")
        );
        components.add(new SlotComp(itemID, commodity.getSlot())
                .setDrawBackground(false)
                .setXY(bodyID + ".x + 18.4*(w/960)", bodyID + ".y + 18.4*(w/960)")
                .setScale("2.7*(w/960)")
        );

        UserPurchaseData account = JustStore.getUserManager().getAccount(player);
        String commodityID = commodity.getId();
        Integer limit = ConfigFile.purchaseLimit.get(commodityID);
        String buyText = (limit != null) ? "&f&l购买(" + account.getCount(commodityID) + "/" + limit + ")" : "&f&l购买";
        components.add(new TextureComp(buyID)
                .setText(buyText)
                .setTexture("ui/common/button_normal_a.png")
                .setXY(bodyID + ".x + 8*(w/960)", bodyID + ".y + 87*(w/960)")
                .setCompSize("64", "24")
                .setScale("w/960")
                .addAction(ActionType.Left_Click, new Statements()
                        .add("func.Sound_Play();")
                        .add(buyID + ".texture = 'ui/common/button_normal_a_press.png';")
                        .build()
                )
                .addAction(ActionType.Left_Release, buyID + ".texture = 'ui/common/button_normal_a.png';")
                .addAction(ActionType.Left_Release, new SubmitParams()
                        .addValue("Trade")
                        .addValue(Operation.Trade.getId())
                        .addValue(commodity.getId())
                )
        );

        if (commodity.getTag() != Tag.NONE) {
            components.add(new TextureComp(tagID)
                    .setTexture(commodity.getTag().getTexture())
                    .setXY(bodyID + ".x + 6*(w/960)", bodyID + ".y")
                    .setCompSize("30*(w/960)", "30*(w/960)")
            );
        }

        ItemStack item = ZaphkielAPI.INSTANCE.getItemStack(commodity.getItem(), null);
        if (item != null) {
            item.setAmount(commodity.getAmount());
            PacketSender.putClientSlotItem(player, commodity.getSlot(), item);
        }

        return components;
    }

    private static String getX(int index) {
        if (index == 0) return ScrollBarComp.EXTEND_ID + ".x";

        int line = index % 5;
        if (line == 0) return "g_" + (index - 4) + ".x";

        return "g_" + index + ".x + 86 * (w / 960)";
    }

    private static String getY(int index) {
        if (index == 0) return ScrollBarComp.EXTEND_ID + ".y";

        int line = index % 5;
        if (line == 0) return "g_" + (index - 4) + ".y + 126 * (w / 960)";

        return "g_" + index + ".y";
    }
}
