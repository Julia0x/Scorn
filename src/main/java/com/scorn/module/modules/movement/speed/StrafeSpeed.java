package com.scorn.module.modules.movement.speed;

import com.scorn.event.bus.Listener;
import com.scorn.event.bus.annotations.EventLink;
import com.scorn.event.impl.player.EventTickPre;
import com.scorn.module.modules.movement.Speed;
import com.scorn.module.setting.impl.newmodesetting.SubMode;
import com.scorn.utils.mc.MoveUtils;

public class StrafeSpeed extends SubMode<Speed> {
    public StrafeSpeed(String name, Speed parentModule) {
        super(name, parentModule);
    }
    @EventLink
    public final Listener<EventTickPre> eventTickPreListener = event -> {
        if (isNull()) {
            return;
        }
        if (!MoveUtils.isMoving2()) {
            return;
        }
        MoveUtils.strafe();
    };
}
