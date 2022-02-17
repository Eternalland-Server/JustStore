package net.sakuragame.eternal.juststore.ui.comp.economy;

import com.taylorswiftcn.megumi.uifactory.generate.ui.component.base.LabelComp;
import com.taylorswiftcn.megumi.uifactory.generate.ui.component.base.TextureComp;
import com.taylorswiftcn.megumi.uifactory.generate.ui.screen.ScreenUI;
import net.sakuragame.eternal.dragoncore.config.FolderType;
import net.sakuragame.eternal.dragoncore.network.PacketSender;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

public class FishComp extends CurrencyComp {
    
    @Override
    public void send(Player player) {
        ScreenUI ui = new ScreenUI(screenID);
        
        ui
                .addComponent(new TextureComp("fish_icon", "symbol/economy/fish.png")
                        .setXY("body.x + 10", "body.y + 26")
                        .setWidth("16")
                        .setHeight("16")
                )
                .addComponent(new TextureComp("fish_bg", "ui/pack/number_background.png")
                        .setXY("fish_icon.x + 16", "fish_icon.y + 1")
                        .setWidth("52")
                        .setHeight("14")
                )
                .addComponent(new LabelComp("fish_info", "&f%player_balance_fish%")
                        .setXY("fish_bg.x+fish_bg.width-fish_info.width-2", "fish_bg.y+3")
                );

        YamlConfiguration yaml = ui.build(null);
        PacketSender.sendYaml(player, FolderType.Gui, screenID, yaml);
    }
}
