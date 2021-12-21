package net.sakuragame.eternal.juststore.core.shop;

import lombok.Getter;

@Getter
public enum ShopCategory {

    Weapon(1),
    Equip(2),
    Accessory(3),
    Material(4);

    private final int id;

    ShopCategory(int id) {
        this.id = id;
    }

    public static ShopCategory match(int id) {
        for (ShopCategory category : values()) {
            if (category.getId() == id) return category;
        }

        return null;
    }
}
