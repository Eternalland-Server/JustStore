package net.sakuragame.eternal.juststore.ui.comp;

import com.taylorswiftcn.megumi.uifactory.generate.ui.component.BasicComponent;
import com.taylorswiftcn.megumi.uifactory.generate.ui.component.base.TextureComp;
import com.taylorswiftcn.megumi.uifactory.generate.ui.component.custom.ScrollBarComp;
import com.taylorswiftcn.megumi.uifactory.generate.ui.screen.ScreenUI;
import net.sakuragame.eternal.dragoncore.config.FolderType;
import net.sakuragame.eternal.dragoncore.network.PacketSender;
import net.sakuragame.eternal.justinventory.ui.BaseInventory;
import net.sakuragame.eternal.juststore.core.shop.GoodsShelf;
import net.sakuragame.eternal.juststore.core.shop.goods.Goods;
import net.sakuragame.eternal.juststore.core.store.Commodity;
import net.sakuragame.eternal.juststore.ui.ScreenHandler;
import org.bukkit.entity.Player;

import java.util.Map;

public class CommodityComp extends BaseInventory {

    public final static String SHOP_SHELF_ID = "eternal_shop_shelf";
    public final static String STORE_SHELF_ID = "eternal_store_shelf";

    public CommodityComp() {
        super(SHOP_SHELF_ID);
    }

    public void sendShop(Player player, String shopID, GoodsShelf shelf) {
        ScreenUI ui = new ScreenUI(SHOP_SHELF_ID);

        Map<String, Goods> goodsMap = shelf.getGoods();
        int size = goodsMap.size();

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

        int i = 1;
        for (Goods goods : goodsMap.values()) {
            comp.addContent(ScreenHandler.build(player, shopID, i, goods));
            i++;
        }
        ui.addComponent(comp);

        yaml = ui.build(player);

        /*try {
            File file = new File(JustStore.getInstance().getDataFolder(), "goods.yml");
            yaml.save(file);
        }
        catch (Exception e) {
            e.printStackTrace();
        }*/

        PacketSender.sendYaml(player, FolderType.Gui, SHOP_SHELF_ID, yaml);
    }

    public void sendStore(Player player, Map<String, Commodity> commodities) {
        ScreenUI ui = new ScreenUI(STORE_SHELF_ID);

        int line = (int) Math.ceil(commodities.size() / 5.0);
        int surplus = Math.max(1, line - 2);

        ScrollBarComp comp = new ScrollBarComp("goods_scrollbar", "12", 216.0 / surplus + "*(w/960)", "126.5");
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
        for (Commodity commodity : commodities.values()) {
            comp.addContent(ScreenHandler.build(player, i, commodity));
            i++;
        }
        ui.addComponent(comp);

        yaml = ui.build(player);

        /*try {
            File file = new File(JustStore.getInstance().getDataFolder(), "commodity.yml");
            yaml.save(file);
        }
        catch (Exception e) {
            e.printStackTrace();
        }*/

        PacketSender.sendYaml(player, FolderType.Gui, STORE_SHELF_ID, yaml);
    }
}
