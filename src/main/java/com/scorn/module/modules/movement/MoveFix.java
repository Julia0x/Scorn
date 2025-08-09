package com.scorn.module.modules.movement;

import com.scorn.module.Module;
import com.scorn.module.ModuleCategory;
import com.scorn.module.setting.impl.BooleanSetting;

public class MoveFix extends Module {
    public static final BooleanSetting silent = new BooleanSetting("Silent", false);

    public MoveFix() {
        super("MoveFix", "Fixes your movement", 0, ModuleCategory.MOVEMENT);
        this.addSettings(silent);
    }
}
