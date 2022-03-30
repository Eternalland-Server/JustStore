package net.sakuragame.eternal.juststore.core.order;

import lombok.AllArgsConstructor;
import lombok.Getter;
import net.sakuragame.eternal.juststore.core.store.StoreType;

@Getter
@AllArgsConstructor
public class StoreOrder {

    private final StoreType type;
    private final String commodityID;

}
