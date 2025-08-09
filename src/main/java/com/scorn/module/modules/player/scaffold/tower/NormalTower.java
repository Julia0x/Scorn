package com.scorn.module.modules.player.scaffold.tower;

import com.scorn.event.bus.Listener;
import com.scorn.event.bus.annotations.EventLink;
import com.scorn.event.impl.player.EventTickPre;
import com.scorn.module.modules.player.Scaffold;
import com.scorn.module.setting.impl.newmodesetting.SubMode;
import com.scorn.utils.mc.MoveUtils;

public class NormalTower extends SubMode<Scaffold> {
    public NormalTower(String name, Scaffold parentModule) {
        super(name, parentModule);
    }

    @EventLink
    public final Listener<EventTickPre> eventTickPreListener = event -> {
        if (isNull()) {
            return;
        }
        if (getParentModule().canTower()) {
            MoveUtils.setMotionY(0.42F);
        }
    };
}
