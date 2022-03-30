package net.sakuragame.eternal.juststore.ui;

import lombok.Getter;

@Getter
public enum Operation {

    Category(0),
    Trade(1),
    ShopOrder(11),
    StoreOrder(12);

    private final int id;

    Operation(int id) {
        this.id = id;
    }

    public static Operation match(int id) {
        for (Operation operation : values()) {
            if (operation.getId() == id) return operation;
        }

        return null;
    }
}
