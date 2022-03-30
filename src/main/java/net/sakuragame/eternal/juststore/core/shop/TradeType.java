package net.sakuragame.eternal.juststore.core.shop;

import lombok.Getter;

@Getter
public enum TradeType {

    BUY("buy.png", "buy_press.png"),
    SELL("sell.png", "sell_press.png"),
    UPGRADE("upgrade.png", "upgrade_press.png");

    private final String a;
    private final String b;

    TradeType(String a, String b) {
        this.a = a;
        this.b = b;
    }

}
