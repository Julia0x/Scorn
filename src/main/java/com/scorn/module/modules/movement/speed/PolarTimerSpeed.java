package com.scorn.module.modules.movement.speed;

import com.scorn.event.bus.Listener;
import com.scorn.event.bus.annotations.EventLink;
import com.scorn.event.impl.network.EventPacket;
import com.scorn.event.impl.player.EventTickPre;
import com.scorn.event.types.TransferOrder;
import com.scorn.module.modules.movement.Speed;
import com.scorn.module.setting.impl.newmodesetting.SubMode;
import com.scorn.utils.mc.MoveUtils;
import com.scorn.utils.mc.PlayerUtil;
import com.scorn.utils.timer.MillisTimer;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.c2s.common.CommonPongC2SPacket;

import java.util.ArrayList;

/*
    this does work but you will be silenting
    to not silent you need to do some extra stuff i wont add here
 */

public class PolarTimerSpeed extends SubMode<Speed> {

    public PolarTimerSpeed(String name, Speed parentModule) {
        super(name, parentModule);
    }

    public static final ArrayList<Packet<?>> transPackets = new ArrayList<>();
    private final MillisTimer millisTimer = new MillisTimer();
    private boolean boosting = false;

    @EventLink
    public final Listener<EventPacket> eventPacketListener = event -> {
        if (isNull()) {
            return;
        }

        if (event.getOrder() == TransferOrder.SEND) {
            if (event.getPacket() instanceof CommonPongC2SPacket) {
                transPackets.add(event.getPacket());
                event.cancel();
            }
        }
    };

    @EventLink
    public final Listener<EventTickPre> eventTickPreListener = event -> {
        if (isNull()) {
            return;
        }
        if (!MoveUtils.isMoving2()) {
            return;
        }

        if (millisTimer.hasElapsed(1150L)) {
            boosting = true;
        }

        if (millisTimer.hasElapsed(7000L)) {
            getParentModule().toggle();
            return;
        }

        if (boosting) {
            PlayerUtil.setTimer(mc.player.age % 2 == 0 ? 1.5f : 1.25f);
        } else {
            PlayerUtil.setTimer(0.05f);
        }
    };

    @Override
    public void onEnable() {
        boosting = false;
        millisTimer.reset();
        transPackets.clear();
        super.onEnable();
    }
}