package com.scorn.module.modules.other;

import com.scorn.module.Module;
import com.scorn.module.ModuleCategory;
import com.scorn.module.setting.impl.BooleanSetting;

public class AntiCheat extends Module {
    public static final BooleanSetting checkSelf = new BooleanSetting("Check self", true);

    public AntiCheat() {
        super("AntiCheat", "Detects if other players are cheating", 0, ModuleCategory.OTHER);
        this.addSettings(checkSelf);
    }
}