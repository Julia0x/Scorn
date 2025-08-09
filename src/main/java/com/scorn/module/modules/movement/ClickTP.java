package com.scorn.module.modules.movement;

import com.scorn.event.bus.Listener;
import com.scorn.event.bus.annotations.EventLink;
import com.scorn.event.impl.player.EventTickPre;
import com.scorn.module.Module;
import com.scorn.module.ModuleCategory;
import com.scorn.module.modules.movement.clicktp.MospixelClickTp;
import com.scorn.module.modules.movement.clicktp.VanillaClickTp;
import com.scorn.module.setting.impl.newmodesetting.NewModeSetting;

public class ClickTP extends Module {
    public final NewModeSetting clickTPMode = new NewModeSetting("ClickTP Mode", "Vanilla",
            new VanillaClickTp("Vanilla", this),
            new MospixelClickTp("Mospixel", this));

    public ClickTP() {
        super("ClickTP", "Middle Click To Teleport", 0, ModuleCategory.MOVEMENT);
        this.addSettings(clickTPMode);
    }

    @EventLink
    public final Listener<EventTickPre> eventTickPreListener = event -> {
        this.setSuffix(clickTPMode.getCurrentMode().getName());
    };
}
