package net.sakuragame.eternal.juststore.ui;

import net.sakuragame.eternal.justinventory.JustInventory;
import net.sakuragame.eternal.justinventory.ui.UIManager;
import net.sakuragame.eternal.juststore.ui.screen.ShopScreen;
import net.sakuragame.eternal.juststore.ui.screen.StoreScreen;

public class ScreenManager {

    private final UIManager uiManager;

    public ScreenManager() {
        this.uiManager = JustInventory.getInstance().getUiManager();
    }

    public void init() {
        uiManager.registerUI(new ShopScreen());
        uiManager.registerUI(new StoreScreen());
    }

}
