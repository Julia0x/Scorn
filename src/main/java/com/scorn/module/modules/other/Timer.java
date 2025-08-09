package com.scorn.module.modules.other;

import com.scorn.event.bus.Listener;
import com.scorn.event.bus.annotations.EventLink;
import com.scorn.event.impl.player.EventTickPre;
import com.scorn.module.Module;
import com.scorn.module.ModuleCategory;
import com.scorn.module.setting.impl.NumberSetting;
import com.scorn.utils.mc.PlayerUtil;

public class Timer extends Module {
    public static final NumberSetting dhauohfeidbf = new NumberSetting("Timer", 0.1, 10, 1, 0.001);

    public Timer() {
        super("Timer", "Modify game speed", 0, ModuleCategory.OTHER);
        this.addSetting(dhauohfeidbf);
    }

    @Override
    public void onEnable() {
        PlayerUtil.setTimer(dhauohfeidbf.getValueFloat());
        super.onEnable();
    }

    @EventLink
    public final Listener<EventTickPre> eventPacketListener = event -> {
        if (isNull()) {
            return;
        }
        PlayerUtil.setTimer(dhauohfeidbf.getValueFloat());
    };

    @Override
    public void onDisable() {
        PlayerUtil.setTimer(1.0f);
        super.onDisable();
    }
}
