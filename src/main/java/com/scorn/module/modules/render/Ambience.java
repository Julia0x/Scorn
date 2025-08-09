package com.scorn.module.modules.render;

import com.scorn.event.bus.Listener;
import com.scorn.event.bus.annotations.EventLink;
import com.scorn.event.impl.network.EventPacket;
import com.scorn.event.impl.player.EventTickPre;
import com.scorn.event.types.TransferOrder;
import com.scorn.mixin.accesors.WorldTimeUpdateS2CPacketAccesor;
import com.scorn.module.Module;
import com.scorn.module.ModuleCategory;
import com.scorn.module.setting.impl.NumberSetting;
import net.minecraft.network.packet.s2c.play.WorldTimeUpdateS2CPacket;
import net.minecraft.world.World;

public class Ambience extends Module {
    public static final NumberSetting timeOfDay = new NumberSetting("Time", World.field_30969 / 2, World.field_30969, 0, 1);

    public Ambience() {
        super("Ambience", "Changes the game", 0, ModuleCategory.RENDER);
        this.addSettings(timeOfDay);
    }

    @EventLink
    public final Listener<EventTickPre> eventTickPreListener = event -> {
        if (isNull()) {
            return;
        }
        mc.world.setTimeOfDay(timeOfDay.getValueInt());
    };

    @EventLink
    public final Listener<EventPacket> eventPacketListener = event -> {
        if (isNull()) {
            return;
        }
        if (event.getOrder() == TransferOrder.SEND) {
            return;
        }
        if (event.getPacket() instanceof WorldTimeUpdateS2CPacket a) {
            ((WorldTimeUpdateS2CPacketAccesor) a).setTimeOfDay(timeOfDay.getValueInt());
        }
    };
}
