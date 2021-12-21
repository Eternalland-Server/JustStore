package net.sakuragame.eternal.juststore.ui.comp;

import com.taylorswiftcn.megumi.uifactory.generate.ui.component.base.TextureComp;
import com.taylorswiftcn.megumi.uifactory.generate.ui.component.custom.ScrollBarComp;
import com.taylorswiftcn.megumi.uifactory.generate.ui.screen.ScreenUI;
import net.sakuragame.eternal.dragoncore.config.FolderType;
import net.sakuragame.eternal.dragoncore.network.PacketSender;
import net.sakuragame.eternal.justinventory.ui.BaseInventory;
import net.sakuragame.eternal.juststore.core.shop.Goods;
import net.sakuragame.eternal.juststore.core.store.Commodity;
import net.sakuragame.eternal.juststore.util.StoreUtil;
import org.bukkit.entity.Player;

import java.util.Map;

public class GoodsShelfComp extends BaseInventory {

    public final static String shopID = "eternal_shop_shelf";
    public final static String storeID = "eternal_store_shelf";

    public GoodsShelfComp() {
        super(shopID);
    }

    public void sendShopGoods(Player player, Map<String, Goods> goodsList) {
        ScreenUI ui = new ScreenUI(shopID);

        int surplus = Math.max(0, goodsList.size() - 4);
        double thumbRollDistance = surplus == 0 ? 0 : (255.0 / surplus);

        ScrollBarComp comp = new ScrollBarComp("goods_scrollbar", thumbRollDistance, 51);
        comp
                .setTexture("0,0,0,0")
                .setXY("goods_frame.x", "goods_frame.y + 9")
                .setCompSize("goods_frame.width", "goods_sub.height");

        comp
                .setTrack((TextureComp) new TextureComp("scrollbar_track")
                        .setTexture("ui/store/shop/scrollbar_track.png")
                        .setXY("goods_frame.x + 204", "goods_frame.y + 1")
                        .setCompSize(9, 274)
                        .setAlpha(0.7)
                )
                .setThumb((TextureComp) new TextureComp("goods_scrollbar_thumb")
                        .setTexture("ui/store/shop/scrollbar_thumb.png")
                        .setXY("scrollbar_track.x", "scrollbar_track.y + 9")
                        .setCompSize(9, 19)
                        .setMaxMoveY("scrollbar_track.height - 37")
                );

        int i = 1;
        for (Goods goods : goodsList.values()) {
            comp.addContent(StoreUtil.buildGoodsComponent(player, i, goods));
            i++;
        }
        ui.addComponent(comp);

        yaml = ui.build(player);

        /*try {
            File file = new File(JustShop.getInstance().getDataFolder(), "comp.yml");
            yaml.save(file);
        }
        catch (Exception e) {
            e.printStackTrace();
        }*/

        PacketSender.sendYaml(player, FolderType.Gui, shopID, yaml);
    }

    public void sendStoreGoods(Player player, Map<String, Commodity> commodities) {
        ScreenUI ui = new ScreenUI(storeID);

        int surplus = Math.max(0, (int) Math.ceil(commodities.size() / 5.0 - 2));
        double thumbRollDistance = surplus == 0 ? 0 : (252.0 / surplus);

        ScrollBarComp comp = new ScrollBarComp("goods_scrollbar", thumbRollDistance, 126);
        comp
                .setTexture("0,0,0,0")
                .setXY("goods_sub.x", "goods_sub.y")
                .setCompSize("goods_sub.width", "goods_sub.height");

        comp
                .setTrack((TextureComp) new TextureComp("scrollbar_track")
                        .setTexture("ui/store/shop/scrollbar_track.png")
                        .setXY("category_bar.x + category_bar.width - 10*(w/960)", "category_bar.y + 36*(w/960)")
                        .setWidth("9*(w/960)")
                        .setHeight("253*(w/960)")
                )
                .setThumb((TextureComp) new TextureComp("goods_scrollbar_thumb")
                        .setTexture("ui/store/shop/scrollbar_thumb.png")
                        .setXY("scrollbar_track.x", "scrollbar_track.y + 9*(w/960)")
                        .setCompSize(9, 19)
                        .setMaxMoveY("scrollbar_track.height - 37*(w/960)")
                );

        int i = 1;
        for (Commodity commodity : commodities.values()) {
            comp.addContent(StoreUtil.buildCommodityComponent(player, i, commodity));
            i++;
        }
        ui.addComponent(comp);

        yaml = ui.build(player);

        /*try {
            File file = new File(JustStore.getInstance().getDataFolder(), "comp.yml");
            yaml.save(file);
        }
        catch (Exception e) {
            e.printStackTrace();
        }*/

        PacketSender.sendYaml(player, FolderType.Gui, storeID, yaml);
    }
}
