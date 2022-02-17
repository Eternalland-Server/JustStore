package net.sakuragame.eternal.juststore.ui.comp.economy;

import org.bukkit.entity.Player;

public abstract class CurrencyComp {

    public final static String screenID = "eternal_shop_economy";

    public abstract void send(Player player);
}
