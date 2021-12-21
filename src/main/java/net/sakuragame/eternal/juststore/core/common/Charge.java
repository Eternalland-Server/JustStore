package net.sakuragame.eternal.juststore.core.common;

import lombok.Getter;
import net.sakuragame.eternal.gemseconomy.api.GemsEconomyAPI;
import net.sakuragame.eternal.gemseconomy.currency.EternalCurrency;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.text.DecimalFormat;

@Getter
public enum Charge {

    MONEY("money", "Γ", EternalCurrency.Money),
    COINS("coins", "☪", EternalCurrency.Coins),
    POINTS("points", "㊉", EternalCurrency.Points);

    private final String id;
    private final String symbol;
    private final EternalCurrency currency;

    private final static DecimalFormat a = new DecimalFormat("0");

    Charge(String id, String symbol, EternalCurrency currency) {
        this.id = id;
        this.symbol = symbol;
        this.currency = currency;
    }

    public String formatting(double value) {
        return getSymbol() + a.format(value) + getCurrency().getDisplay();
    }

    public void withdraw(Player player, double value) {
        GemsEconomyAPI.withdraw(player.getUniqueId(), value, getCurrency());
    }
}
