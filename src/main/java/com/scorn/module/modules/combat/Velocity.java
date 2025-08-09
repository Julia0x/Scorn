package com.scorn.module.modules.combat;

import com.scorn.Client;
import com.scorn.event.bus.Listener;
import com.scorn.event.bus.annotations.EventLink;
import com.scorn.event.impl.network.EventPacket;
import com.scorn.event.impl.player.EventTickPre;
import com.scorn.module.Module;
import com.scorn.module.ModuleCategory;
import com.scorn.module.modules.combat.velocity.*;
import com.scorn.module.modules.movement.Speed;
import com.scorn.module.modules.other.Disabler;
import com.scorn.module.modules.player.BedAura;
import com.scorn.module.setting.impl.NumberSetting;
import com.scorn.module.setting.impl.newmodesetting.NewModeSetting;
import com.scorn.utils.mc.MoveUtils;

public class Velocity extends Module {
    public final NewModeSetting velocityMode = new NewModeSetting("Velocity Mode", "Simple",
            new SimpleVelocity("Simple", this),
            new WatchdogAirVelocity("Watchdog Air", this),
            new IntaveVelocity("Intave", this),
            new BlocksMCVelocity("BlocksMC", this),
            new LegitVelocity("Legit", this));

    public final NumberSetting verticalSimple = new NumberSetting("Vertical", -100, 100, 85, 1);
    public final NumberSetting horizontalSimple = new NumberSetting("Horizontal", -100, 100, 85, 1);

    public final NumberSetting verticalBlocksMC = new NumberSetting("Vertical BMC", -100, 100, 85, 1);
    public final NumberSetting horizontalBlocksMC = new NumberSetting("Horizontal BMC", -100, 100, 85, 1);

    public final NumberSetting airTicks = new NumberSetting("Air Ticks", 0, 10, 5, 1);

    public Velocity() {
        super("Velocity", "Changes knockback", 0, ModuleCategory.COMBAT);
        addSettings(velocityMode, verticalSimple, horizontalSimple, verticalBlocksMC, horizontalBlocksMC, airTicks);
        verticalSimple.addDependency(velocityMode, "Simple");
        horizontalSimple.addDependency(velocityMode, "Simple");

        verticalBlocksMC.addDependency(velocityMode, "BlocksMC");
        horizontalBlocksMC.addDependency(velocityMode, "BlocksMC");

        airTicks.addDependency(velocityMode, "Watchdog Air");
    }

    @EventLink
    public final Listener<EventTickPre> eventTickPreListener = event -> {
        if (velocityMode.isMode("Simple")) {
            this.setSuffix(verticalSimple.getValue() + "%" + " " + horizontalSimple.getValue() + "%");
        } else if (velocityMode.isMode("BlocksMC")) {
            this.setSuffix(velocityMode.getCurrentMode().getName() + " " + verticalBlocksMC.getValue() + "%" + " " + horizontalBlocksMC.getValue() + "%");
        } else {
            this.setSuffix(velocityMode.getCurrentMode().getName());
        }
    };

    public boolean canWork(EventPacket event) {
        if (Client.INSTANCE.getModuleManager().getModule(BedAura.class).shouldCancelVelocity()) {
            event.cancel();
            return false;
        }

        if (Client.INSTANCE.getModuleManager().getModule(Speed.class).isEnabled()) {
            Speed speed = Client.INSTANCE.getModuleManager().getModule(Speed.class);
            if (speed.speedMode.isMode("Watchdog")) {
                if (speed.watchdogLowHop.getValue() && speed.watchdogShouldCancelVelocity.getValue()) {
                    boolean canLowHop = Client.INSTANCE.getModuleManager().getModule(Disabler.class).isEnabled() && Client.INSTANCE.getModuleManager().getModule(Disabler.class).watchdogMotion.getValue() && Client.INSTANCE.getModuleManager().getModule(Disabler.class).canLowHop;
                    if (canLowHop) {
                        if (MoveUtils.isMoving2()) {
                            event.cancel();
                            return false;
                        }
                    }
                }
            }
        }
        return true;
    }
}