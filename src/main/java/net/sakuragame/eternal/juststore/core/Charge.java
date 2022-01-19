package net.sakuragame.eternal.juststore.core;

import lombok.Getter;
import net.sakuragame.eternal.gemseconomy.api.GemsEconomyAPI;
import net.sakuragame.eternal.gemseconomy.currency.EternalCurrency;
import org.bukkit.entity.Player;

@Getter
public enum Charge {

    MONEY("money", EternalCurrency.Money),
    COINS("coins", EternalCurrency.Coins),
    POINTS("points", EternalCurrency.Points);

    private final String id;
    private final EternalCurrency currency;

    Charge(String id, EternalCurrency currency) {
        this.id = id;
        this.currency = currency;
    }

    public String getSymbol() {
        return getCurrency().getSymbol();
    }

    public void withdraw(Player player, double value) {
        GemsEconomyAPI.withdraw(player.getUniqueId(), value, getCurrency());
    }
}
