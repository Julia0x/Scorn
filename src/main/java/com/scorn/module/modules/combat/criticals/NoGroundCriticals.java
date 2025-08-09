package com.scorn.module.modules.combat.criticals;

import com.scorn.event.bus.Listener;
import com.scorn.event.bus.annotations.EventLink;
import com.scorn.event.impl.network.EventPacket;
import com.scorn.event.types.TransferOrder;
import com.scorn.mixin.accesors.PlayerMoveC2SPacketAccessor;
import com.scorn.module.modules.combat.Criticals;
import com.scorn.module.setting.impl.newmodesetting.SubMode;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;

public class NoGroundCriticals extends SubMode<Criticals> {
    public NoGroundCriticals(String name, Criticals parentModule) {
        super(name, parentModule);
    }

    @EventLink
    public final Listener<EventPacket> eventPacketListener = event -> {
        if (isNull()) {
            return;
        }
        if (event.getOrder() == TransferOrder.SEND) {
            if (event.getPacket() instanceof PlayerMoveC2SPacket packet) {
                PlayerMoveC2SPacketAccessor accessor = (PlayerMoveC2SPacketAccessor) packet;
                accessor.setOnGround(false);
            }
        }
    };
}
