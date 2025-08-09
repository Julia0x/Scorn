package com.scorn.module.modules.player.nofall;

import com.scorn.Client;
import com.scorn.event.bus.Listener;
import com.scorn.event.bus.annotations.EventLink;
import com.scorn.event.impl.player.EventTickPre;
import com.scorn.module.modules.player.NoFall;
import com.scorn.module.setting.impl.newmodesetting.SubMode;
import com.scorn.utils.mc.PacketUtils;
import com.scorn.utils.mc.PlayerUtil;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;

public class WatchdogTimerNoFall extends SubMode<NoFall> {
    public WatchdogTimerNoFall(String name, NoFall parentModule) {
        super(name, parentModule);
    }

    @EventLink
    public final Listener<EventTickPre> eventTickPreListener = event -> {
        if (isNull()) {
            return;
        }
        if (PlayerUtil.isOverVoid()) {
            return;
        }
        if (mc.player.fallDistance >= getParentModule().minFallDistance.getValue()) {
            PlayerUtil.setTimer(0.5f);

            PacketUtils.sendPacketSilently(new PlayerMoveC2SPacket.OnGroundOnly(true));

            mc.player.fallDistance = 0f;

            Client.INSTANCE.getDelayUtil().queue(ev -> PlayerUtil.setTimer(1F), 1);
        }
    };
}
