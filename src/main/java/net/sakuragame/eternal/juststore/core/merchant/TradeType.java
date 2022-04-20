package net.sakuragame.eternal.juststore.core.merchant;

import lombok.Getter;

@Getter
public enum TradeType {

    BUY("buy.png", "buy_press.png"),
    SELL("sell.png", "sell_press.png"),
    UPGRADE("upgrade.png", "upgrade_press.png");

    private final String normal;
    private final String press;

    TradeType(String normal, String press) {
        this.normal = normal;
        this.press = press;
    }

}
