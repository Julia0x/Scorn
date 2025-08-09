package com.scorn.module.modules.player.scaffold.tower;

import com.scorn.event.bus.Listener;
import com.scorn.event.bus.annotations.EventLink;
import com.scorn.event.impl.player.EventTickPre;
import com.scorn.module.modules.player.Scaffold;
import com.scorn.module.setting.impl.newmodesetting.SubMode;
import com.scorn.utils.mc.MoveUtils;
import com.scorn.utils.mc.PlayerUtil;

public class VulcanTower extends SubMode<Scaffold> {
    public VulcanTower(String name, Scaffold parentModule) {
        super(name, parentModule);
    }

    @EventLink
    public final Listener<EventTickPre> eventTickPreListener = event -> {
        if (isNull()) {
            return;
        }
        if (getParentModule().canTower()) {
            if (mc.player.isOnGround()) {
                MoveUtils.setMotionY(0.42F);
            }
            switch (PlayerUtil.inAirTicks() % 3) {
                case 0:
                    MoveUtils.setMotionY(0.41985 + (Math.random() * 0.000095));
                    break;
                case 2:
                    MoveUtils.setMotionY(Math.ceil(mc.player.getY()) - mc.player.getY());
                    break;
            }
        }
    };
}
