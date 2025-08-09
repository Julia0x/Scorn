package com.scorn.module.modules.player;

import com.scorn.event.bus.Listener;
import com.scorn.event.bus.annotations.EventLink;
import com.scorn.event.impl.player.EventTickPre;
import com.scorn.module.ModuleCategory;
import com.scorn.module.Module;
import com.scorn.module.modules.player.nofall.*;
import com.scorn.module.setting.impl.NumberSetting;
import com.scorn.module.setting.impl.newmodesetting.NewModeSetting;

public class NoFall extends Module {
    public final NewModeSetting nofallMode = new NewModeSetting("NoFall Mode", "Packet",
            new PacketNoFall("Packet", this),
            new NoGroundNoFall("No Ground", this),
            new WatchdogTimerNoFall("Watchdog Timer", this),
            new VulcanNoFall("Vulcan", this),
            new MospixelNoFall("Mospixel", this));

    public final NumberSetting minFallDistance = new NumberSetting("Min NoFall Distance", 2, 30, 4, 0.1);

    public NoFall() {
        super("NoFall", "Removes fall damage", 0, ModuleCategory.PLAYER);
        this.addSettings(nofallMode, minFallDistance);
    }

    @EventLink
    public final Listener<EventTickPre> eventTickPreListener = event -> {
        this.setSuffix(nofallMode.getCurrentMode().getName());
    };
}
