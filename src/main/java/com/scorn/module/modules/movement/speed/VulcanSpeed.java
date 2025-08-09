package com.scorn.module.modules.movement.speed;

import com.scorn.event.bus.Listener;
import com.scorn.event.bus.annotations.EventLink;
import com.scorn.event.impl.network.EventPacket;
import com.scorn.event.impl.player.EventTickPre;
import com.scorn.event.types.TransferOrder;
import com.scorn.mixin.accesors.PlayerMoveC2SPacketAccessor;
import com.scorn.module.modules.movement.Speed;
import com.scorn.module.setting.impl.newmodesetting.SubMode;
import com.scorn.utils.mc.MoveUtils;
import com.scorn.utils.mc.PlayerUtil;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;

public class VulcanSpeed extends SubMode<Speed> {
    public VulcanSpeed(String name, Speed parentModule) {
        super(name, parentModule);
    }

    @EventLink
    public final Listener<EventPacket> eventPacketListener = event -> {
        if (isNull()) {
            return;
        }

        if (event.getOrder() == TransferOrder.SEND) {
            if (PlayerUtil.getDistanceToGround() < 1.5) {
                if (event.getPacket() instanceof PlayerMoveC2SPacket && mc.player.getVelocity().y < 0) {
                    ((PlayerMoveC2SPacketAccessor) event.getPacket()).setOnGround(true);
                }
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

        if (mc.player.isOnGround()) {
            MoveUtils.strafe(getParentModule().vulcanGroundSpeed.getValue());
        } else {
            MoveUtils.strafe();
            if (PlayerUtil.inAirTicks() == 1) {
                MoveUtils.setMotionY(-0.1);
            }
        }
    };
}
