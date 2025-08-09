package com.scorn.event.impl.player;

import com.scorn.event.types.Event;
import lombok.Getter;
import lombok.Setter;

public class EventYawMoveFix implements Event {
    @Getter
    @Setter
    float yaw;

    public EventYawMoveFix(float yaw) {
        this.yaw = yaw;
    }
}
