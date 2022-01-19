package net.sakuragame.eternal.juststore.ui.comp;

import com.taylorswiftcn.megumi.uifactory.generate.function.SubmitParams;
import com.taylorswiftcn.megumi.uifactory.generate.type.ActionType;
import com.taylorswiftcn.megumi.uifactory.generate.ui.component.base.TextureComp;
import net.sakuragame.eternal.dragoncore.config.FolderType;
import net.sakuragame.eternal.dragoncore.network.PacketSender;
import net.sakuragame.eternal.justinventory.ui.BaseInventory;
import net.sakuragame.eternal.juststore.JustStore;
import net.sakuragame.eternal.juststore.core.shop.Shop;
import net.sakuragame.eternal.juststore.ui.Operation;
import org.bukkit.entity.Player;

public class CategoryComp extends BaseInventory {

    public final static String screenID = "eternal_shop_category";

    public CategoryComp() {
        super(screenID);
    }

    public void sendCategory(Player player, Shop shop) {

        for (int i = 0; i < Math.min(6, shop.getGoodsShelf().size()); i++) {
            String name = shop.getGoodsShelf().get(i).getName();

            ui
                    .addComponent(new TextureComp(i + "_category")
                            .setText(name)
                            .setTexture("(global.eternal_shop_category == " + i + ") ? 'ui/store/shop/selected.png' : 'ui/store/shop/unselected.png'")
                            .setExtend("category_" + (i + 1))
                            .addAction(ActionType.Left_Click, new SubmitParams(JustStore.getInstance())
                                    .setCondition("global.eternal_shop_category != " + i)
                                    .addValue(Operation.Category.getId())
                                    .addValue(i)
                            )

                    );
        }

        yaml = ui.build(null);

        /*try {
            File file = new File(JustStore.getInstance().getDataFolder(), "category.yml");
            yaml.save(file);
        }
        catch (Exception e) {
            e.printStackTrace();
        }*/

        PacketSender.sendYaml(player, FolderType.Gui, screenID, yaml);
    }
}
