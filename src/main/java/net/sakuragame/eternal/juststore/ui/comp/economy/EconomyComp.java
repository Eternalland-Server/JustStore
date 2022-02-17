package net.sakuragame.eternal.juststore.ui.comp.economy;

import com.taylorswiftcn.megumi.uifactory.generate.ui.component.base.LabelComp;
import com.taylorswiftcn.megumi.uifactory.generate.ui.component.base.TextureComp;
import com.taylorswiftcn.megumi.uifactory.generate.ui.screen.ScreenUI;
import net.sakuragame.eternal.dragoncore.config.FolderType;
import net.sakuragame.eternal.dragoncore.network.PacketSender;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

public class EconomyComp extends CurrencyComp {

    @Override
    public void send(Player player) {
        ScreenUI ui = new ScreenUI(screenID);

        ui
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
                );

        YamlConfiguration yaml = ui.build(null);
        PacketSender.sendYaml(player, FolderType.Gui, screenID, yaml);
    }
}
