package net.sakuragame.eternal.juststore.core.store;

import lombok.Getter;

@Getter
public enum Tag {

    NONE(""),
    HOT("ui/store/tag/hot.png"),
    LIMIT("ui/store/tag/limit.png"),
    NEW("ui/store/tag/new.png");

    private final String texture;

    Tag(String texture) {
        this.texture = texture;
    }
}
