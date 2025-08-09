package com.scorn.module.modules.combat.velocity;

import com.scorn.event.bus.Listener;
import com.scorn.event.bus.annotations.EventLink;
import com.scorn.event.impl.network.EventPacket;
import com.scorn.mixin.accesors.EntityVelocityUpdateS2CPacketAccessor;
import com.scorn.module.modules.combat.Velocity;
import com.scorn.module.setting.impl.newmodesetting.SubMode;
import com.scorn.utils.mc.PlayerUtil;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.s2c.play.EntityVelocityUpdateS2CPacket;

public class WatchdogAirVelocity extends SubMode<Velocity> {
    public WatchdogAirVelocity(String name, Velocity parentModule) {
        super(name, parentModule);
    }

    @EventLink
    public final Listener<EventPacket> eventPacketListener = event -> {
        if (isNull()) {
            return;
        }
        Packet<?> packet = event.getPacket();

        if (packet instanceof EntityVelocityUpdateS2CPacket) {
            EntityVelocityUpdateS2CPacketAccessor s12 = (EntityVelocityUpdateS2CPacketAccessor) packet;
            if (s12.getId() == mc.player.getId()) {
                if (getParentModule().canWork(event)) {
                    if (PlayerUtil.inAirTicks() <= getParentModule().airTicks.getValue()) {
                        mc.player.setVelocity(mc.player.getVelocity().x, s12.getVelocityY() / 8000D, mc.player.getVelocity().z);
                    }
                    event.cancel();
                }
            }
        }
    };
}
