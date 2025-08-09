package com.scorn.module.modules.movement.fly;

import com.scorn.event.bus.Listener;
import com.scorn.event.bus.annotations.EventLink;
import com.scorn.event.impl.world.EventBlockShape;
import com.scorn.module.modules.movement.Fly;
import com.scorn.module.setting.impl.newmodesetting.SubMode;
import net.minecraft.util.shape.VoxelShapes;

public class AirWalkFly extends SubMode<Fly> {
    public AirWalkFly(String name, Fly parentModule) {
        super(name, parentModule);
    }

    @EventLink
    public final Listener<EventBlockShape> eventBlockShapeListener = event -> {
        if (isNull()) {
            return;
        }
        if (event.getPos().getY() < mc.player.getBlockY()) {
            event.setShape(VoxelShapes.fullCube());
        }
    };
}
