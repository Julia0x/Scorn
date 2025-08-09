package com.scorn.module.modules.combat;

import com.scorn.event.bus.Listener;
import com.scorn.event.bus.annotations.EventLink;
import com.scorn.event.impl.player.EventAttack;
import com.scorn.event.impl.player.EventTickPre;
import com.scorn.module.ModuleCategory;
import com.scorn.module.Module;
import com.scorn.module.modules.combat.criticals.*;
import com.scorn.module.setting.impl.newmodesetting.NewModeSetting;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffects;

public class Criticals extends Module {
    public final NewModeSetting critMode = new NewModeSetting("Crit Mode", "Vanilla",
            new VanillaCriticals("Vanilla", this),
            new NoGroundCriticals("NoGround", this),
            new WatchdogCriticals("Watchdog", this),
            new VulcanCriticals("Vulcan", this),
            new MospixelCriticals("Mospixel", this));

    public Criticals() {
        super("Criticals", "Always deal criticals", 0, ModuleCategory.COMBAT);
        this.addSetting(critMode);
    }

    @EventLink
    public final Listener<EventTickPre> eventTickPreListener = event -> {
        this.setSuffix(critMode.getCurrentMode().getName());
    };

    @EventLink
    public final Listener<EventAttack> eventAttackListener = event -> {
        if (isNull()) {
            return;
        }
        boolean willCritLegit = mc.player.fallDistance > 0.0F && !mc.player.isOnGround() && !mc.player.isClimbing() && !mc.player.isTouchingWater() && !mc.player.hasStatusEffect(StatusEffects.BLINDNESS) && !mc.player.hasVehicle() && event.getTarget() instanceof LivingEntity;

        if (!willCritLegit) {
            mc.player.addCritParticles(event.getTarget());
        }
    };
}
