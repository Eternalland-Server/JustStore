package net.sakuragame.eternal.juststore.core.store;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class StoreOrder {

    private final StoreType type;
    private final String commodityID;

}
