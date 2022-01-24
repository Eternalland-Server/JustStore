package net.sakuragame.eternal.juststore.ui;

import com.taylorswiftcn.megumi.uifactory.generate.function.Statements;
import com.taylorswiftcn.megumi.uifactory.generate.function.SubmitParams;
import com.taylorswiftcn.megumi.uifactory.generate.type.ActionType;
import com.taylorswiftcn.megumi.uifactory.generate.ui.component.BasicComponent;
import com.taylorswiftcn.megumi.uifactory.generate.ui.component.base.LabelComp;
import com.taylorswiftcn.megumi.uifactory.generate.ui.component.base.SlotComp;
import com.taylorswiftcn.megumi.uifactory.generate.ui.component.base.TextureComp;
import ink.ptms.zaphkiel.ZaphkielAPI;
import net.sakuragame.eternal.dragoncore.network.PacketSender;
import net.sakuragame.eternal.juststore.JustStore;
import net.sakuragame.eternal.juststore.core.UserPurchaseData;
import net.sakuragame.eternal.juststore.core.shop.Goods;
import net.sakuragame.eternal.juststore.core.store.Commodity;
import net.sakuragame.eternal.juststore.core.store.Tag;
import net.sakuragame.eternal.juststore.file.sub.ConfigFile;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.*;

public class ScreenHandler {

    private static final JustStore plugin = JustStore.getInstance();

    public static LinkedList<BasicComponent> build(Player player, int index, Goods goods) {
        LinkedList<BasicComponent> components = new LinkedList<>();

        String y = (index == 1 ? "goods_scrollbar_region.y" : "goods_" + (index - 1) + ".y + 51");
        String bodyID = "goods_" + index;
        String frameID = bodyID + "_frame";
        String itemID = bodyID + "_item";
        String nameID = bodyID + "_name";
        String priceID = bodyID + "_price";
        String requireID = bodyID + "_require";
        String consumeID = bodyID + "_consume";
        String buyID = bodyID + "_buy";

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
        components.add(new LabelComp(priceID, goods.getFormatPrice())
                .setX(bodyID + ".x + (" + bodyID + ".width - " + priceID + ".width) -6")
                .setY(nameID + ".y")
        );
        components.add(new TextureComp(consumeID)
                .setXY(requireID + ".x", requireID + ".y + 9")
        );

        PacketSender.putClientSlotItem(player, goods.getGoodsSlot(), ZaphkielAPI.INSTANCE.getItemStack(goods.getItem(), null));

        Map<String, Integer> consume = goods.getConsume();
        List<String> keys = new ArrayList<>(consume.keySet());
        if (keys.size() != 0) {
            components.add(new LabelComp(requireID, "&7需要材料:")
                    .setX(nameID + ".x")
                    .setY(nameID + ".y + 12")
            );
            int i = 0;
            for (String key : consume.keySet()) {
                ItemStack item = ZaphkielAPI.INSTANCE.getItemStack(key, null);
                int amount = consume.get(key);
                do {
                    String id = "goods_" + index + "_slot_bg_" + i;
                    components.add(new TextureComp(id)
                            .setTexture("ui/pack/slot_bg.png")
                            .setX(consumeID + ".x + 19*" + i)
                            .setY(consumeID + ".y")
                            .setCompSize(16, 16)
                    );
                    components.add(new SlotComp("goods_" + index + "_consume_" + i, goods.getConsumeSlot(i))
                            .setDrawBackground(false)
                            .setXY(id + ".x + 2", id + ".y + 2")
                            .setScale(0.75)
                    );

                    if (item != null) {
                        if (amount > 64) {
                            item.setAmount(64);
                            amount = amount - 64;
                        }
                        else {
                            item.setAmount(amount);
                            amount = 0;
                        }
                        PacketSender.putClientSlotItem(player, goods.getConsumeSlot(i), item);
                    }
                    i++;
                } while (amount != 0);
            }
        }


        components.add(new TextureComp(buyID)
                .setTexture("ui/store/button/buy.png")
                .setXY(bodyID + ".x + 158", bodyID + ".y + 30")
                .setCompSize(40, 16)
                .addAction(ActionType.Left_Click, new Statements()
                        .add("func.Sound_Play();")
                        .add(buyID + ".texture = 'ui/store/button/buy_press.png';")
                        .build()
                )
                .addAction(ActionType.Left_Release, buyID + ".texture = 'ui/store/button/buy.png';")
                .addAction(ActionType.Left_Release, new SubmitParams(plugin)
                        .addValue(Operation.Buy.getId())
                        .add("global.eternal_shop_category")
                        .addValue(goods.getId())
                )
        );

        return components;
    }

    public static LinkedList<BasicComponent> build(Player player, int index, Commodity commodity) {
        LinkedList<BasicComponent> components = new LinkedList<>();

        String bodyID = "goods_" + index;
        String nameID = bodyID + "_name";
        String priceID = bodyID + "+price";
        String itemID = bodyID + "_item";
        String tagID = bodyID + "_tag";
        String buyID = commodity.getId() + "_buy";

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
                .setText(commodity.getPriceFormat())
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
        String buyText = limit != null ? "&f&l购买(" + account.getCount(commodityID) + "/" + limit + ")" : "&f&l购买";
        components.add(new TextureComp(buyID)
                .setText(buyText)
                .setTexture("ui/common/button_normal_a.png")
                .setXY(bodyID + ".x + 8*(w/960)", bodyID + ".y + 87*(w/960)")
                .setCompSize("64*(w/960)", "24*(w/960)")
                .addAction(ActionType.Left_Click, new Statements()
                        .add("func.Sound_Play();")
                        .add(buyID + ".texture = 'ui/common/button_normal_a_press.png';")
                        .build()
                )
                .addAction(ActionType.Left_Release, buyID + ".texture = 'ui/common/button_normal_a.png';")
                .addAction(ActionType.Left_Release, new SubmitParams(plugin)
                        .addValue(Operation.Buy.getId())
                        .add("global.eternal_store_category")
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
        if (index == 0) return "goods_scrollbar_region.x";

        int line = index % 5;
        if (line == 0) return "goods_" + (index - 4) + ".x";

        return "goods_" + index + ".x + 86 * (w / 960)";
    }

    private static String getY(int index) {
        if (index == 0) return "goods_scrollbar_region.y";

        int line = index % 5;
        if (line == 0) return "goods_" + (index - 4) + ".y + 126 * (w / 960)";

        return "goods_" + index + ".y";
    }
}
