package net.sakuragame.eternal.juststore.ui.screen;

import com.taylorswiftcn.megumi.uifactory.generate.function.SubmitParams;
import com.taylorswiftcn.megumi.uifactory.generate.type.ActionType;
import com.taylorswiftcn.megumi.uifactory.generate.type.FunctionType;
import com.taylorswiftcn.megumi.uifactory.generate.ui.component.base.LabelComp;
import com.taylorswiftcn.megumi.uifactory.generate.ui.component.base.TextureComp;
import net.sakuragame.eternal.justinventory.ui.BaseInventory;
import net.sakuragame.eternal.justinventory.ui.comp.BalanceComp;
import net.sakuragame.eternal.justinventory.ui.comp.InventoryComp;
import net.sakuragame.eternal.justmessage.screen.ScreenManager;
import net.sakuragame.eternal.juststore.JustStore;
import net.sakuragame.eternal.juststore.ui.Operation;
import net.sakuragame.eternal.juststore.ui.comp.CommodityComp;

import java.util.Arrays;

public class StoreScreen extends BaseInventory {

    private static final JustStore plugin = JustStore.getInstance();
    public final static String screenID = "eternal_store";
    public final static String bannerID = "banner";

    public StoreScreen() {
        super(screenID);
        this.init();
    }

    private void init() {
        ui
                .addImports(
                        Arrays.asList(
                                BalanceComp.screenID,
                                InventoryComp.screenID,
                                CommodityComp.storeID,
                                bannerID,
                                ScreenManager.getQuantityID()
                        )
                )
                .setPressEKeyClose()
                .addFunctions(FunctionType.Open)
                .addComponent(new TextureComp("body", "ui/pack/background.png")
                        .setXY("0", "0")
                        .setWidth("w")
                        .setHeight("h")
                )
                .addComponent(new TextureComp("top_bg", "0,0,0,200")
                        .setXY("0", "0")
                        .setWidth("w")
                        .setHeight("80*(w/960)")
                )
                .addComponent(new TextureComp("top_split", "255,255,255,30")
                        .setXY("0", "80*(w/960)")
                        .setWidth("w")
                        .setHeight("1")
                )
                .addComponent(new TextureComp("shade_bg", "ui/pack/shade_background.png")
                        .setXY("0", "0")
                        .setWidth("w")
                        .setHeight("h")
                        .setAlpha(0.2)
                )
                .addComponent(new TextureComp("store_icon", "ui/store/icon.png")
                        .setXY("body.x+42*(w/960)", "body.y+42*(w/960)")
                        .setWidth("83*(w/960)")
                        .setHeight("38*(w/960)")
                )
                .addComponent(new TextureComp("category_bar", "ui/pack/frame_c.png")
                        .setXY("store_icon.x", "top_split.y + 12*(w/960)")
                        .setWidth("438*(w/960)")
                        .setHeight("27*(w/960)")
                )
                .addComponent(new TextureComp("category_1", "0,0,0,30")
                        .setText("global.eternal_store_category == 1 ? '&f&l道具' : '&7&l道具'")
                        .setXY("category_bar.x + 2", "category_bar.y + 2")
                        .setWidth("56*(w/960)")
                        .setHeight("23*(w/960)")
                        .addAction(ActionType.Enter, "category_1.texture = '255,255,255,30';")
                        .addAction(ActionType.Leave, "category_1.texture = '0,0,0,30';")
                        .addAction(ActionType.Left_Release, new SubmitParams(plugin)
                                .setCondition("global.eternal_store_category != 1")
                                .addValue(Operation.Category.getId())
                                .addValue(1)
                        )
                )
                .addComponent(new TextureComp("category_2", "0,0,0,30")
                        .setText("global.eternal_store_category == 2 ? '&f&l装备' : '&7&l装备'")
                        .setXY("category_1.x + 58*(w/960)", "category_1.y")
                        .setWidth("56*(w/960)")
                        .setHeight("23*(w/960)")
                        .addAction(ActionType.Enter, "category_2.texture = '255,255,255,30';")
                        .addAction(ActionType.Leave, "category_2.texture = '0,0,0,30';")
                        .addAction(ActionType.Left_Release, new SubmitParams(plugin)
                                .setCondition("global.eternal_store_category != 2")
                                .addValue(Operation.Category.getId())
                                .addValue(2)
                        )
                )
                .addComponent(new TextureComp("category_3", "0,0,0,30")
                        .setText("global.eternal_store_category == 3 ? '&f&l时装' : '&7&l时装'")
                        .setXY("category_2.x + 58*(w/960)", "category_1.y")
                        .setWidth("56*(w/960)")
                        .setHeight("23*(w/960)")
                        .addAction(ActionType.Enter, "category_3.texture = '255,255,255,30';")
                        .addAction(ActionType.Leave, "category_3.texture = '0,0,0,30';")
                        .addAction(ActionType.Left_Release, new SubmitParams(plugin)
                                .setCondition("global.eternal_store_category != 3")
                                .addValue(Operation.Category.getId())
                                .addValue(3)
                        )
                )
                .addComponent(new TextureComp("category_4", "0,0,0,30")
                        .setText("global.eternal_store_category == 4 ? '&f&l礼包' : '&7&l礼包'")
                        .setXY("category_3.x + 58", "category_1.y")
                        .setWidth("56*(w/960)")
                        .setHeight("23*(w/960)")
                        .addAction(ActionType.Enter, "category_4.texture = '255,255,255,30';")
                        .addAction(ActionType.Leave, "category_4.texture = '0,0,0,30';")
                        .addAction(ActionType.Left_Release, new SubmitParams(plugin)
                                .setCondition("global.eternal_store_category != 4")
                                .addValue(Operation.Category.getId())
                                .addValue(4)
                        )
                )
                .addComponent(new TextureComp("category_5", "0,0,0,30")
                        .setText("global.eternal_store_category == 5 ? '&f&l宠物' : '&7&l宠物'")
                        .setXY("category_4.x + 58*(w/960)", "category_1.y")
                        .setWidth("56*(w/960)")
                        .setHeight("23*(w/960)")
                        .addAction(ActionType.Enter, "category_5.texture = '255,255,255,30';")
                        .addAction(ActionType.Leave, "category_5.texture = '0,0,0,30';")
                        .addAction(ActionType.Left_Release, new SubmitParams(plugin)
                                .setCondition("global.eternal_store_category != 5")
                                .addValue(Operation.Category.getId())
                                .addValue(5)
                        )
                )
                .addComponent(new TextureComp("category_split_1", "255,255,255,20")
                        .setXY("category_1.x + 56*(w/960)", "category_1.y + 2*(w/960)")
                        .setWidth("2*(w/960)")
                        .setHeight("19*(w/960)")
                )
                .addComponent(new TextureComp("category_split_2", "255,255,255,20")
                        .setXY("category_2.x + 56*(w/960)", "category_2.y + 2*(w/960)")
                        .setWidth("2*(w/960)")
                        .setHeight("19*(w/960)")
                )
                .addComponent(new TextureComp("category_split_3", "255,255,255,20")
                        .setXY("category_3.x + 56*(w/960)", "category_3.y + 2*(w/960)")
                        .setWidth("2*(w/960)")
                        .setHeight("19*(w/960)")
                )
                .addComponent(new TextureComp("category_split_4", "255,255,255,20")
                        .setXY("category_4.x + 56*(w/960)", "category_4.y + 2*(w/960)")
                        .setWidth("2*(w/960)")
                        .setHeight("19*(w/960)")
                )
                .addComponent(new TextureComp("tip_frame", "ui/store/tip_frame.png")
                        .setXY("category_bar.x", "category_bar.y + 300*(w/960)")
                        .setWidth("434*(w/960)")
                        .setHeight("63*(w/960)")
                )
                .addComponent(new TextureComp("goods_sub")
                        .setTexture("0,0,0,0")
                        .setXY("category_bar.x", "category_bar.y + 36*(w/960)")
                        .setCompSize("428*(w/960)", "252*(w/960)")
                )
                .addComponent(new LabelComp("tip_contents", "&6&lTIP.&f商场所有的东西都很好多买点，有意身心健康，&6樱花&f说他也非常喜欢买这个东西&7充3w没有解决不了的事情")
                        .setXY("tip_frame.x + 12*(w/960)", "tip_frame.y + 12*(w/960)")
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
