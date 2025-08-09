package com.scorn.module.modules.combat.criticals;

import com.scorn.event.bus.Listener;
import com.scorn.event.bus.annotations.EventLink;
import com.scorn.event.impl.player.EventAttack;
import com.scorn.module.modules.combat.Criticals;
import com.scorn.module.setting.impl.newmodesetting.SubMode;

public class WatchdogCriticals extends SubMode<Criticals> {
    public WatchdogCriticals(String name, Criticals parentModule) {
        super(name, parentModule);
    }

    @EventLink
    public final Listener<EventAttack> eventAttackListener = event -> {
        if (isNull()) {
            return;
        }
        if (mc.player.isOnGround()) {
            mc.player.setPosition(mc.player.getX(), mc.player.getY() + 0.001D, mc.player.getZ());
        }
    };
}
