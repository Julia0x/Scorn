package com.scorn.module.modules.client;

import com.scorn.module.Module;
import com.scorn.module.ModuleCategory;
import com.scorn.module.setting.impl.ModeSetting;
import com.scorn.module.setting.impl.NumberSetting;

public class Notifications extends Module {
    public static final NumberSetting opacity = new NumberSetting("BG Opacity", 0, 255, 80, 1);
    public static final ModeSetting fontMode = new ModeSetting("Font", "MC", "MC");

    public Notifications() {
        super("Notifications", "Notifies you of client events", 0, ModuleCategory.CLIENT);
        this.addSettings(opacity, fontMode);
    }

    public static int getOpacity() {
        return opacity.getValueInt();
    }

    public static String getFontMode() {
        return fontMode.getMode();
    }
}
