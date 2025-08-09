package com.scorn.module.modules.movement;

import com.scorn.event.bus.Listener;
import com.scorn.event.bus.annotations.EventLink;
import com.scorn.event.impl.network.EventPacket;
import com.scorn.event.impl.player.EventTravel;
import com.scorn.mixin.accesors.AccessorPlayerMoveC2SPacket;
import com.scorn.module.Module;
import com.scorn.module.ModuleCategory;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;
import net.minecraft.util.math.Vec3d;

public class ElytraFly extends Module {
    public ElytraFly() {
        super("ElytraFly", "Control and fly with the elytra better", 0, ModuleCategory.MOVEMENT);
    }

    float pitch = 0;

    @EventLink
    public final Listener<EventTravel> eventTickPreListener = event -> {
        if (isNull() || !mc.player.isFallFlying()) {
            return;
        }
        event.cancel();
        float forward = mc.player.input.movementForward;
        float strafe = mc.player.input.movementSideways;
        float yaw = mc.player.getYaw();
        if (forward == 0.0f && strafe == 0.0f) {
            setMotionXZ(0.0, 0.0);
        } else {
            pitch = 12;
            double rx = Math.cos(Math.toRadians(yaw + 90.0f));
            double rz = Math.sin(Math.toRadians(yaw + 90.0f));
            setMotionXZ(((forward * 1 * rx)
                    + (strafe * 1 * rz)), (forward * 1 * rz)
                    - (strafe * 1 * rx));
        }
        setMotionY(0.0);
        pitch = 0;
        if (mc.options.jumpKey.isPressed()) {
            pitch = -51;
            setMotionY(1);
        } else if (mc.options.sneakKey.isPressed()) {
            setMotionY(-1);
        }
    };

    @EventLink
    public final Listener<EventPacket> eventPacketListener = event -> {
        if (mc.player == null) {
            return;
        }
        if (event.getPacket() instanceof PlayerMoveC2SPacket packet
                && packet.changesLook() && mc.player.isFallFlying()) {
            if (mc.options.leftKey.isPressed()) {
                ((AccessorPlayerMoveC2SPacket) packet).hookSetYaw(packet.getYaw(0.0f) - 90.0f);
            }
            if (mc.options.rightKey.isPressed()) {
                ((AccessorPlayerMoveC2SPacket) packet).hookSetYaw(packet.getYaw(0.0f) + 90.0f);
            }

            ((AccessorPlayerMoveC2SPacket) packet).hookSetPitch(pitch);
        }
    };

    public void setMotionY(double y) {
        Vec3d motion = mc.player.getVelocity();
        mc.player.setVelocity(motion.getX(), y, motion.getZ());
    }

    public void setMotionXZ(double x, double z) {
        Vec3d motion = mc.player.getVelocity();
        mc.player.setVelocity(x, motion.y, z);
    }
}
