package com.scorn.module.modules.movement.fly;

import com.scorn.event.bus.Listener;
import com.scorn.event.bus.annotations.EventLink;
import com.scorn.event.impl.player.EventTickPre;
import com.scorn.module.modules.movement.Fly;
import com.scorn.module.setting.impl.newmodesetting.SubMode;

public class VulcanGlideFly extends SubMode<Fly> {
    public VulcanGlideFly(String name, Fly parentModule) {
        super(name, parentModule);
    }

    @EventLink
    public final Listener<EventTickPre> eventTickPreListener = event -> {
        if (isNull()) {
            return;
        }
        if (mc.player.fallDistance > 0.1) {
            if (mc.player.age % 2 == 0) {
                mc.player.getVelocity().y = -0.155;
            } else {
                mc.player.getVelocity().y = -0.1;
            }
        }
    };
}
