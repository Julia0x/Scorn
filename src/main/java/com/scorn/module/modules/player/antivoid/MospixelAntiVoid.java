package com.scorn.module.modules.player.antivoid;

import com.scorn.Client;
import com.scorn.event.bus.Listener;
import com.scorn.event.bus.annotations.EventLink;
import com.scorn.event.impl.network.EventPacket;
import com.scorn.module.modules.movement.Fly;
import com.scorn.module.modules.player.AntiVoid;
import com.scorn.module.setting.impl.newmodesetting.SubMode;
import com.scorn.utils.mc.PlayerUtil;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;
import net.minecraft.network.packet.s2c.play.PlayerPositionLookS2CPacket;

public class MospixelAntiVoid extends SubMode<AntiVoid> {
    public MospixelAntiVoid(String name, AntiVoid parentModule) {
        super(name, parentModule);
    }

    @EventLink
    public final Listener<EventPacket> eventPacketListener = event -> {
        if (isNull()) {
            return;
        }

        if (Client.INSTANCE.getModuleManager().getModule(Fly.class).isEnabled()) {
            return;
        }
        Packet<?> packet = event.getPacket();

        if (packet instanceof PlayerPositionLookS2CPacket && mc.player.fallDistance > 3.125) {
            mc.player.fallDistance = 3.125f;
        } else if (packet instanceof PlayerMoveC2SPacket) {
            if (mc.player.fallDistance >= getParentModule().minFallDistance.getValue() && mc.player.getVelocity().y <= 0 && PlayerUtil.isOverVoid()) {
                ((PlayerMoveC2SPacket) packet).y += 11;
            }
        }
    };
}
