package com.scorn.module.modules.movement;

import com.scorn.module.ModuleCategory;
import com.scorn.module.Module;
import com.scorn.module.setting.impl.BooleanSetting;

public class KeepSprint extends Module {
    public static final BooleanSetting sprint = new BooleanSetting("Sprint",true);

    public KeepSprint() {
        super("KeepSprint", "Removes attack slowdown", 0, ModuleCategory.MOVEMENT);
        this.addSetting(sprint);
    }
}
