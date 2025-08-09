package com.scorn.module.modules.movement.spider;

import com.scorn.event.bus.Listener;
import com.scorn.event.bus.annotations.EventLink;
import com.scorn.event.impl.player.EventTickPre;
import com.scorn.module.modules.movement.Spider;
import com.scorn.module.setting.impl.newmodesetting.SubMode;
import com.scorn.utils.mc.PlayerUtil;

public class VulcanSpider extends SubMode<Spider> {
    public VulcanSpider(String name, Spider parentModule) {
        super(name, parentModule);
    }

    @EventLink
    public final Listener<EventTickPre> eventTickListener = event -> {
        if (isNull()) {
            return;
        }
        if (mc.player.horizontalCollision && mc.player.fallDistance < 1) {
            if (PlayerUtil.ticksExisted() % 2 == 0) {
                mc.player.jump();
            }
        }
    };
}
