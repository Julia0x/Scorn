package com.scorn.event.impl.render;

import com.scorn.event.types.Event;
import lombok.Getter;
import net.minecraft.client.util.math.MatrixStack;

@Getter
public class EventRender3D implements Event {
    MatrixStack matrixStack;

    public EventRender3D(MatrixStack matrixStack) {
        this.matrixStack = matrixStack;
    }
}
