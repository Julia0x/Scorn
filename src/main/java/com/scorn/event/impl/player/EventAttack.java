package com.scorn.event.impl.player;

import com.scorn.event.types.Event;
import lombok.Getter;
import net.minecraft.entity.Entity;

@Getter
public class EventAttack implements Event {
    Entity target;
    public EventAttack(Entity target){
        this.target = target;
    }
}
