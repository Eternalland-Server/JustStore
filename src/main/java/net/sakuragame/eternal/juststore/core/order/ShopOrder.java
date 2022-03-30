package net.sakuragame.eternal.juststore.core.order;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ShopOrder {

    private String shopID;
    private Integer category;
    private String goodsID;

}
