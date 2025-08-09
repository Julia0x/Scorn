package com.scorn.module.modules.combat.criticals;

import com.scorn.event.bus.Listener;
import com.scorn.event.bus.annotations.EventLink;
import com.scorn.event.impl.player.EventAttack;
import com.scorn.module.modules.combat.Criticals;
import com.scorn.module.setting.impl.newmodesetting.SubMode;
import com.scorn.utils.mc.PacketUtils;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;

public class VanillaCriticals extends SubMode<Criticals> {
    public VanillaCriticals(String name, Criticals parentModule) {
        super(name, parentModule);
    }

    @EventLink
    public final Listener<EventAttack> eventAttackListener = event -> {
        if (isNull()) {
            return;
        }
        boolean willCritLegit = mc.player.fallDistance > 0.0F && !mc.player.isOnGround() && !mc.player.isClimbing() && !mc.player.isTouchingWater() && !mc.player.hasStatusEffect(StatusEffects.BLINDNESS) && !mc.player.hasVehicle() && event.getTarget() instanceof LivingEntity;

        if (willCritLegit) {
            return;
        }

        PacketUtils.sendPacket(new PlayerMoveC2SPacket.PositionAndOnGround(mc.player.getPos().x, mc.player.getPos().y + 0.2, mc.player.getPos().z, false));
        PacketUtils.sendPacket(new PlayerMoveC2SPacket.PositionAndOnGround(mc.player.getPos().x, mc.player.getPos().y + 0.01, mc.player.getPos().z, false));
    };
}
