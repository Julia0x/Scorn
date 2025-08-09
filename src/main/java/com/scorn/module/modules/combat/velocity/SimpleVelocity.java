package com.scorn.module.modules.combat.velocity;

import com.scorn.event.bus.Listener;
import com.scorn.event.bus.annotations.EventLink;
import com.scorn.event.impl.network.EventPacket;
import com.scorn.mixin.accesors.EntityVelocityUpdateS2CPacketAccessor;
import com.scorn.module.modules.combat.Velocity;
import com.scorn.module.setting.impl.newmodesetting.SubMode;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.s2c.play.EntityVelocityUpdateS2CPacket;

public class SimpleVelocity extends SubMode<Velocity> {
    public SimpleVelocity(String name, Velocity parentModule) {
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
                    if (getParentModule().horizontalSimple.getValueInt() == 0 && getParentModule().verticalSimple.getValueInt() == 0) {
                        event.cancel();
                        return;
                    }

                    s12.setVelocityX((int) (s12.getVelocityX() * (getParentModule().horizontalSimple.getValueInt() / 100D)));
                    s12.setVelocityZ((int) (s12.getVelocityZ() * (getParentModule().horizontalSimple.getValueInt() / 100D)));
                    s12.setVelocityY((int) (s12.getVelocityY() * (getParentModule().verticalSimple.getValueInt() / 100D)));
                }
            }
        }
    };
}
