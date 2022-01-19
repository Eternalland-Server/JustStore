package net.sakuragame.eternal.juststore.core.store;

import lombok.Getter;

@Getter
public enum StoreType {

    Prop(1, "prop.yml"),
    Consume(2, "consume.yml"),
    Clothes(3, "clothes.yml"),
    Gift(4, "gift.yml"),
    Pet(5, "pet.yml");

    private final int id;
    private final String file;

    StoreType(int id, String file) {
        this.id = id;
        this.file = file;
    }

    public static StoreType match(int id) {
        for (StoreType type : values()) {
            if (type.getId() == id) return type;
        }

        return null;
    }
}
