package net.sakuragame.eternal.juststore.ui.screen;

import com.taylorswiftcn.megumi.uifactory.generate.type.FunctionType;
import com.taylorswiftcn.megumi.uifactory.generate.ui.component.base.TextureComp;
import com.taylorswiftcn.megumi.uifactory.generate.ui.component.custom.BodyComp;
import com.taylorswiftcn.megumi.uifactory.generate.ui.screen.ScreenUI;
import net.sakuragame.eternal.dragoncore.config.FolderType;
import net.sakuragame.eternal.dragoncore.network.PacketSender;
import net.sakuragame.eternal.justinventory.ui.BaseInventory;
import net.sakuragame.eternal.justmessage.screen.ScreenManager;
import net.sakuragame.eternal.juststore.ui.comp.CategoryComp;
import net.sakuragame.eternal.juststore.ui.comp.CommodityComp;
import net.sakuragame.eternal.juststore.ui.comp.economy.CurrencyComp;
import org.bukkit.entity.Player;

import java.util.Arrays;

public class ShopScreen extends BaseInventory {

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
                                CurrencyComp.screenID,
                                CommodityComp.SHOP_SHELF_ID,
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
