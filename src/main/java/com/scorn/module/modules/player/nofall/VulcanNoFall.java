package com.scorn.module.modules.player.nofall;

import com.scorn.event.bus.Listener;
import com.scorn.event.bus.annotations.EventLink;
import com.scorn.event.impl.network.EventPacket;
import com.scorn.event.types.TransferOrder;
import com.scorn.mixin.accesors.PlayerMoveC2SPacketAccessor;
import com.scorn.module.modules.player.NoFall;
import com.scorn.module.setting.impl.newmodesetting.SubMode;
import com.scorn.utils.mc.MoveUtils;
import com.scorn.utils.mc.PlayerUtil;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;

public class VulcanNoFall extends SubMode<NoFall> {
    public VulcanNoFall(String name, NoFall parentModule) {
        super(name, parentModule);
    }

    @EventLink
    public final Listener<EventPacket> eventTickPreListener = event -> {
        if (isNull()) {
            return;
        }
        if (PlayerUtil.isOverVoid()) {
            return;
        }
        if (event.getOrder() == TransferOrder.SEND) {
            if (event.getPacket() instanceof PlayerMoveC2SPacket packet) {
                PlayerMoveC2SPacketAccessor accessor = (PlayerMoveC2SPacketAccessor) packet;

                if (mc.player.fallDistance >= getParentModule().minFallDistance.getValue()) {
                    accessor.setOnGround(true);
                    mc.player.fallDistance = 0f;
                    MoveUtils.setMotionY(0);
                }
            }
        }
    };
}
