package com.scorn.module.modules.player.antivoid;

import com.scorn.Client;
import com.scorn.event.bus.Listener;
import com.scorn.event.bus.annotations.EventLink;
import com.scorn.event.impl.player.EventTickPre;
import com.scorn.module.modules.movement.Fly;
import com.scorn.module.modules.player.AntiVoid;
import com.scorn.module.setting.impl.newmodesetting.SubMode;
import com.scorn.utils.mc.PlayerUtil;
import net.minecraft.util.math.Vec3d;

public class VanillaAntiVoid extends SubMode<AntiVoid> {
    public VanillaAntiVoid(String name, AntiVoid parentModule) {
        super(name, parentModule);
    }

    private Vec3d lastSafePos;

    @EventLink
    public final Listener<EventTickPre> eventTickListener = event -> {
        if (isNull()) {
            return;
        }

        if (Client.INSTANCE.getModuleManager().getModule(Fly.class).isEnabled()) {
            return;
        }

        if (PlayerUtil.isOverVoid() && mc.player.fallDistance >= getParentModule().minFallDistance.getValueFloat()) {
            mc.player.setPosition(lastSafePos);
        } else if (mc.player.isOnGround()) {
            lastSafePos = new Vec3d(mc.player.getBlockPos().toCenterPos().x, mc.player.getY(), mc.player.getBlockPos().toCenterPos().z);
        }
    };
}
