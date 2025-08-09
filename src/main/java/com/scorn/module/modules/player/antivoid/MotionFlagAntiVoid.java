package com.scorn.module.modules.player.antivoid;

import com.scorn.Client;
import com.scorn.event.bus.Listener;
import com.scorn.event.bus.annotations.EventLink;
import com.scorn.event.impl.player.EventTickPre;
import com.scorn.module.modules.movement.Fly;
import com.scorn.module.modules.player.AntiVoid;
import com.scorn.module.setting.impl.newmodesetting.SubMode;
import com.scorn.utils.mc.MoveUtils;
import com.scorn.utils.mc.PlayerUtil;

public class MotionFlagAntiVoid extends SubMode<AntiVoid> {
    public MotionFlagAntiVoid(String name, AntiVoid parentModule) {
        super(name, parentModule);
    }

    @EventLink
    public final Listener<EventTickPre> eventTickListener = event -> {
        if (isNull()) {
            return;
        }

        if (Client.INSTANCE.getModuleManager().getModule(Fly.class).isEnabled()) {
            return;
        }

        if (PlayerUtil.isOverVoid() && mc.player.fallDistance >= getParentModule().minFallDistance.getValueFloat() && mc.player.getBlockY() + mc.player.getVelocity().y < Math.floor(mc.player.getBlockY())) {
            MoveUtils.setMotionY(3);
            mc.player.fallDistance = 0;
        }
    };
}
