package net.sakuragame.eternal.juststore.ui.screen;

import com.taylorswiftcn.megumi.uifactory.generate.type.FunctionType;
import com.taylorswiftcn.megumi.uifactory.generate.ui.component.base.LabelComp;
import com.taylorswiftcn.megumi.uifactory.generate.ui.component.base.TextureComp;
import com.taylorswiftcn.megumi.uifactory.generate.ui.component.custom.BodyComp;
import com.taylorswiftcn.megumi.uifactory.generate.ui.screen.ScreenUI;
import net.sakuragame.eternal.dragoncore.config.FolderType;
import net.sakuragame.eternal.dragoncore.network.PacketSender;
import net.sakuragame.eternal.justinventory.ui.BaseInventory;
import net.sakuragame.eternal.justmessage.screen.ScreenManager;
import net.sakuragame.eternal.juststore.JustStore;
import net.sakuragame.eternal.juststore.ui.comp.CategoryComp;
import net.sakuragame.eternal.juststore.ui.comp.CommodityComp;
import org.bukkit.entity.Player;

import java.io.File;
import java.util.Arrays;

public class ShopScreen extends BaseInventory {

    private static final JustStore plugin = JustStore.getInstance();
    public final static String screenID = "eternal_shop";

    public ShopScreen() {
        super(screenID);
        this.init();
    }

    public void send(Player player) {
        PacketSender.sendYaml(player, FolderType.Gui, screenID, yaml);
    }

    private void init() {
        ScreenUI ui = new ScreenUI(screenID);
        ui
                .addImports(
                        Arrays.asList(
                                CommodityComp.shopID,
                                CategoryComp.screenID,
                                ScreenManager.getQuantityID()
                        )
                )
                .setPressEKeyClose()
                .addFunctions(FunctionType.Open)
                .addComponent(new BodyComp()
                        .setTexture("ui/store/shop/background.png")
                        .setXY("(w - body.width)/2", "(h - body.height)/2 - 15")
                        .setCompSize(224, 356)
                )
                .addComponent(new TextureComp("title", "ui/store/shop/title_shade.png")
                        .setText("%eternal_shop_name%")
                        .setXY("body.x + 3", "body.y + 3")
                        .setWidth("218")
                        .setHeight("16")
                )
                .addComponent(new TextureComp("money_icon", "ui/pack/icon/money.png")
                        .setXY("body.x + 10", "body.y + 26")
                        .setWidth("16")
                        .setHeight("16")
                )
                .addComponent(new TextureComp("money_bg", "ui/pack/number_background.png")
                        .setXY("money_icon.x + 16", "money_icon.y + 1")
                        .setWidth("52")
                        .setHeight("14")
                )
                .addComponent(new LabelComp("money_info", "&f%player_balance_money%")
                        .setXY("money_bg.x+money_bg.width-money_info.width-2", "money_bg.y+3")
                )
                .addComponent(new TextureComp("coins_icon", "ui/pack/icon/coins.png")
                        .setXY("money_icon.x + 68", "money_icon.y")
                        .setWidth("16")
                        .setHeight("16")
                )
                .addComponent(new TextureComp("coins_bg", "ui/pack/number_background.png")
                        .setXY("coins_icon.x + 16", "coins_icon.y + 1")
                        .setWidth("52")
                        .setHeight("14")
                )
                .addComponent(new LabelComp("coins_info", "&f%player_balance_coins%")
                        .setXY("coins_bg.x+coins_bg.width-coins_info.width-2", "coins_bg.y+3")
                )
                .addComponent(new TextureComp("points_icon", "ui/pack/icon/points.png")
                        .setXY("coins_icon.x + 68", "coins_icon.y")
                        .setWidth("16")
                        .setHeight("16")
                )
                .addComponent(new TextureComp("points_bg", "ui/pack/number_background.png")
                        .setXY("points_icon.x + 16", "points_icon.y + 1")
                        .setWidth("52")
                        .setHeight("14")
                )
                .addComponent(new LabelComp("points_info", "&f%player_balance_points%")
                        .setXY("points_bg.x+points_bg.width-points_info.width-2", "points_bg.y+3")
                )
                .addComponent(new TextureComp("goods_frame", "ui/store/shop/goods_frame.png")
                        .setXY("body.x + 5", "body.y + 63")
                        .setWidth("214")
                        .setHeight("276")
                )
                .addComponent(new TextureComp("category_1", "0,0,0,0")
                        .setXY("goods_frame.x", "body.y + 50")
                        .setWidth("32")
                        .setHeight("14")
                )
                .addComponent(new TextureComp("category_2", "0,0,0,0")
                        .setXY("goods_frame.x + 34", "category_1.y")
                        .setWidth("32")
                        .setHeight("14")
                )
                .addComponent(new TextureComp("category_3", "0,0,0,0")
                        .setXY("category_2.x + 34", "category_1.y")
                        .setWidth("32")
                        .setHeight("14")
                )
                .addComponent(new TextureComp("category_4", "0,0,0,0")
                        .setXY("category_3.x + 34", "category_1.y")
                        .setWidth("32")
                        .setHeight("14")
                )
                .addComponent(new TextureComp("category_5", "0,0,0,0")
                        .setXY("category_4.x + 34", "category_1.y")
                        .setWidth("32")
                        .setHeight("14")
                )
                .addComponent(new TextureComp("category_6", "0,0,0,0")
                        .setXY("category_5.x + 34", "category_1.y")
                        .setWidth("32")
                        .setHeight("14")
                )
                .addComponent(new TextureComp("goods_sub", "0,0,0,0")
                        .setXY("goods_frame.x", "goods_frame.y + 10")
                        .setWidth("goods_frame.width - 9")
                        .setHeight("goods_frame.height - 20")
                )
                .addComponent(new TextureComp("random_tip", "0,0,0,0")
                        .setXY("body.x", "body.y + 340")
                        .setWidth("body.width")
                        .setHeight("12")
                );

        yaml = ui.build(null);

        /*try {
            File file = new File(JustStore.getInstance().getDataFolder(), "main.yml");
            yaml.save(file);
        }
        catch (Exception e) {
            e.printStackTrace();
        }*/
    }
}
