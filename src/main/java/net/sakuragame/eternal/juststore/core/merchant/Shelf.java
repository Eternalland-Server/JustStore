package net.sakuragame.eternal.juststore.core.merchant;

import lombok.Getter;

import java.util.List;

@Getter
public class Shelf {

    private final String ID;
    private final String name;
    private final List<String> goods;

    public Shelf(String ID, String name, List<String> goods) {
        this.ID = ID;
        this.name = name;
        this.goods = goods;
    }
}
