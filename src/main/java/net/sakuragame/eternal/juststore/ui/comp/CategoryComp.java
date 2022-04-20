package net.sakuragame.eternal.juststore.ui.comp;

import com.taylorswiftcn.megumi.uifactory.generate.function.SubmitParams;
import com.taylorswiftcn.megumi.uifactory.generate.type.ActionType;
import com.taylorswiftcn.megumi.uifactory.generate.ui.component.base.TextureComp;
import net.sakuragame.eternal.dragoncore.config.FolderType;
import net.sakuragame.eternal.dragoncore.network.PacketSender;
import net.sakuragame.eternal.justinventory.ui.BaseInventory;
import net.sakuragame.eternal.juststore.core.merchant.Merchant;
import net.sakuragame.eternal.juststore.ui.Operation;
import org.bukkit.entity.Player;

public class CategoryComp extends BaseInventory {

    public final static String screenID = "eternal_shop_category";

    public CategoryComp() {
        super(screenID);
    }

    public void send(Player player, Merchant merchant) {
        int i = 1;
        for (String key : merchant.getShelf().keySet()) {
            String name = merchant.getShelf().get(key).getName();

            ui
                    .addComponent(new TextureComp("c_" + key)
                            .setText(name)
                            .setTexture("(global.eternal_shop_category=='" + key + "')?'ui/store/shop/selected.png':'ui/store/shop/unselected.png'")
                            .setExtend("category_" + i)
                            .addAction(ActionType.Left_Click, new SubmitParams()
                                    .setCondition("global.eternal_shop_category != '" + key + "'")
                                    .addValue(Operation.Category.getId())
                                    .addValue(merchant.getID())
                                    .addValue(key)
                            )
                    );
            i++;
        }

        yaml = ui.build(null);

        PacketSender.sendYaml(player, FolderType.Gui, screenID, yaml);
    }
}
