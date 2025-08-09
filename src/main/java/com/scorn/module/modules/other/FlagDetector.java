package com.scorn.module.modules.other;

import com.scorn.Client;
import com.scorn.event.bus.Listener;
import com.scorn.event.bus.annotations.EventLink;
import com.scorn.event.impl.network.EventPacket;
import com.scorn.event.types.TransferOrder;
import com.scorn.module.Module;
import com.scorn.module.ModuleCategory;
import com.scorn.utils.render.notifications.impl.Notification;
import com.scorn.utils.render.notifications.impl.NotificationMoode;
import net.minecraft.network.packet.s2c.play.PlayerPositionLookS2CPacket;

public class FlagDetector extends Module {
    public FlagDetector() {
        super("FlagDetector", "Detects flags", 0, ModuleCategory.OTHER);
    }

    @EventLink
    public final Listener<EventPacket> eventPacketListener = event -> {
        if (isNull()) {
            return;
        }
        if (event.getOrder() == TransferOrder.RECEIVE) {
            if (event.getPacket() instanceof PlayerPositionLookS2CPacket) {
                Client.INSTANCE.getNotificationManager().addNewNotification(new Notification("You flagged", 1000, NotificationMoode.WARNING));
            }
        }
    };
}
