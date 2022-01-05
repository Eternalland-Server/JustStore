package net.sakuragame.eternal.juststore.core.common;

import lombok.Getter;
import net.sakuragame.eternal.gemseconomy.api.GemsEconomyAPI;
import net.sakuragame.eternal.gemseconomy.currency.EternalCurrency;
import org.bukkit.entity.Player;

@Getter
public enum Charge {

    MONEY("money", "Γ", EternalCurrency.Money),
    COINS("coins", "☪", EternalCurrency.Coins),
    POINTS("points", "㊉", EternalCurrency.Points);

    private final String id;
    private final String symbol;
    private final EternalCurrency currency;

    Charge(String id, String symbol, EternalCurrency currency) {
        this.id = id;
        this.symbol = symbol;
        this.currency = currency;
    }

    public void withdraw(Player player, double value) {
        GemsEconomyAPI.withdraw(player.getUniqueId(), value, getCurrency());
    }
}
