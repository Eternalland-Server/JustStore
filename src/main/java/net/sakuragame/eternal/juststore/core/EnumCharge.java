package net.sakuragame.eternal.juststore.core;

import lombok.Getter;
import net.sakuragame.eternal.gemseconomy.api.GemsEconomyAPI;
import net.sakuragame.eternal.gemseconomy.currency.EternalCurrency;
import org.bukkit.entity.Player;

import java.util.UUID;

@Getter
public enum EnumCharge {

    NONE(),
    MONEY("money", "§6", EternalCurrency.Money),
    COINS("coins", "§b", EternalCurrency.Coins),
    POINTS("points", "§c", EternalCurrency.Points),
    FISH("fish", "§a", EternalCurrency.Fish);

    private final String id;
    private final String color;
    private final EternalCurrency currency;

    EnumCharge() {
        this.id = null;
        this.color = null;
        this.currency = null;
    }

    EnumCharge(String id, String color, EternalCurrency currency) {
        this.id = id;
        this.color = color;
        this.currency = currency;
    }

    public String getSymbol() {
        return getCurrency().getSymbol();
    }

    public void withdraw(Player player, double value) {
        GemsEconomyAPI.withdraw(player.getUniqueId(), value, getCurrency());
    }

    public void withdraw(Player player, double value, String reason) {
        GemsEconomyAPI.withdraw(player.getUniqueId(), value, getCurrency(), reason);
    }
}
