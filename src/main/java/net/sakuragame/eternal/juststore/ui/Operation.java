package net.sakuragame.eternal.juststore.ui;

import lombok.Getter;

@Getter
public enum Operation {

    Category(0),
    Buy(1),
    ShopOrder(2),
    StoreOrder(3);

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
