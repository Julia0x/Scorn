package com.scorn.utils.render.notifications;

import com.scorn.Client;
import com.scorn.event.bus.Listener;
import com.scorn.event.bus.annotations.EventLink;
import com.scorn.event.impl.player.EventTickPre;
import com.scorn.event.impl.render.EventRender2D;
import com.scorn.module.modules.client.Notifications;
import com.scorn.utils.Utils;
import com.scorn.utils.render.notifications.impl.Notification;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class NotificationManager implements Utils {

    private final List<Notification> notifications = new CopyOnWriteArrayList<>();

    @EventLink
    public final Listener<EventTickPre> eventTickPreListener = event -> {
        notifications.removeIf(Notification::shouldDisappear);
    };

    @EventLink
    public final Listener<EventRender2D> eventRender2DListener = event -> {
        if (mc.player == null || mc.world == null) {
            return;
        }
        if (!Client.INSTANCE.getModuleManager().getModule(Notifications.class).isEnabled()) {
            return;
        }
        int yOffset = 0;
        for (Notification notification : notifications) {
            notification.render(event, yOffset);
            yOffset += 17;
        }
    };

    public void addNewNotification(Notification notification) {
        notifications.add(notification);
    }
}
