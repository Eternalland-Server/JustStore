package net.sakuragame.eternal.juststore.ui.comp;

import com.taylorswiftcn.megumi.uifactory.generate.ui.component.BasicComponent;
import com.taylorswiftcn.megumi.uifactory.generate.ui.component.base.TextureComp;
import com.taylorswiftcn.megumi.uifactory.generate.ui.component.custom.ScrollBarComp;
import com.taylorswiftcn.megumi.uifactory.generate.ui.screen.ScreenUI;
import net.sakuragame.eternal.dragoncore.config.FolderType;
import net.sakuragame.eternal.dragoncore.network.PacketSender;
import net.sakuragame.eternal.juststore.JustStore;
import net.sakuragame.eternal.juststore.core.merchant.goods.Goods;
import net.sakuragame.eternal.juststore.core.merchant.Shelf;
import net.sakuragame.eternal.juststore.core.store.Commodity;
import net.sakuragame.eternal.juststore.ui.LayoutHandler;
import org.bukkit.entity.Player;

import java.util.List;

public class CommodityComp {

    private ScreenUI ui;
    public final static String Merchant_SHELF_ID = "eternal_shop_shelf";
    public final static String STORE_SHELF_ID = "eternal_store_shelf";

    public void sendMerchant(Player player, Shelf shelf) {
        ScreenUI ui = new ScreenUI(Merchant_SHELF_ID);

        List<String> goodsList = shelf.getGoods();
        int size = goodsList.size();

        int surplus = Math.max(1, size - 5);

        ScrollBarComp comp = new ScrollBarComp("goods_scrollbar", 14, 237.0 / surplus, 51);
        comp
                .setTexture("0,0,0,0")
                .setXY("goods_frame.x", "goods_frame.y + 9")
                .setCompSize("goods_frame.width", "goods_sub.height");

        BasicComponent thumb = new TextureComp("goods_scrollbar_thumb")
                .setTexture("ui/store/shop/scrollbar_thumb.png")
                .setXY("scrollbar_track.x", "scrollbar_track.y + 9")
                .setCompSize(9, 19);

        if (size > 5) {
            thumb.setMaxMoveY("scrollbar_track.height - 37");
        }

        comp
                .setTrack((TextureComp) new TextureComp("scrollbar_track")
                        .setTexture("ui/store/shop/scrollbar_track.png")
                        .setXY("goods_frame.x + 204", "goods_frame.y + 1")
                        .setCompSize(9, 274)
                        .setAlpha(0.7)
                )
                .setThumb((TextureComp) thumb);

        for (int i = 0; i < size; i++) {
            Goods goods = JustStore.getShopManger().getGoods(goodsList.get(i));
            if (goods == null) continue;
            comp.addContent(LayoutHandler.build(player, i, goods));
        }

        ui.addComponent(comp);

        PacketSender.sendYaml(player, FolderType.Gui, Merchant_SHELF_ID, ui.build(player));
    }

    public void sendStore(Player player, List<String> commodityID) {
        ScreenUI ui = new ScreenUI(STORE_SHELF_ID);

        int line = (int) Math.ceil(commodityID.size() / 5.0);
        int surplus = Math.max(1, line - 2);

        ScrollBarComp comp = new ScrollBarComp("goods_scrollbar", 12, 216.0 / surplus, 126.5);
        comp
                .setTexture("0,0,0,0")
                .setXY("goods_sub.x", "goods_sub.y")
                .setCompSize("goods_sub.width", "goods_sub.height");

        BasicComponent thumb = new TextureComp("goods_scrollbar_thumb")
                .setTexture("ui/store/shop/scrollbar_thumb.png")
                .setXY("scrollbar_track.x", "scrollbar_track.y + 9*(w/960)")
                .setCompSize("9*(w/960)", "19*(w/960)");
        if (line > 2) {
            thumb.setMaxMoveY("scrollbar_track.height - 37*(w/960)");
        }

        comp
                .setTrack((TextureComp) new TextureComp("scrollbar_track")
                        .setTexture("ui/store/shop/scrollbar_track.png")
                        .setXY("category_bar.x + category_bar.width - 10*(w/960)", "category_bar.y + 36*(w/960)")
                        .setWidth("9*(w/960)")
                        .setHeight("253*(w/960)")
                )
                .setThumb((TextureComp) thumb);

        int i = 1;
        for (String key : commodityID) {
            Commodity commodity = JustStore.getStoreManager().getCommodity(key);
            comp.addContent(LayoutHandler.build(player, i, commodity));
            i++;
        }
        ui.addComponent(comp);

        PacketSender.sendYaml(player, FolderType.Gui, STORE_SHELF_ID, ui.build(player));
    }
}
