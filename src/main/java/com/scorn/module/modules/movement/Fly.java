package com.scorn.module.modules.movement;

import com.scorn.event.bus.Listener;
import com.scorn.event.bus.annotations.EventLink;
import com.scorn.event.impl.player.EventTickPre;
import com.scorn.module.ModuleCategory;
import com.scorn.module.Module;
import com.scorn.module.modules.movement.fly.AirWalkFly;
import com.scorn.module.modules.movement.fly.GrimBoatFly;
import com.scorn.module.modules.movement.fly.VanillaFly;
import com.scorn.module.modules.movement.fly.VulcanGlideFly;
import com.scorn.module.setting.impl.NumberSetting;
import com.scorn.module.setting.impl.newmodesetting.NewModeSetting;

public class Fly extends Module {
    public final NewModeSetting flyMode = new NewModeSetting("Fly Mode", "Vanilla",
            new VanillaFly("Vanilla", this),
            new AirWalkFly("Air Walk", this),
            new GrimBoatFly("Grim Boat", this),
            new VulcanGlideFly("Vulcan Glide", this));

    public final NumberSetting speed = new NumberSetting("Speed", 0, 5, 1, 0.001);


    public Fly() {
        super("Fly", "Lets you fly in vanilla", 0, ModuleCategory.MOVEMENT);
        addSettings(flyMode, speed);
        speed.addDependency(flyMode, "Vanilla");
    }

    @Override
    public void onEnable() {
        if (isNull()) {
            toggle();
            return;
        }
        super.onEnable();
    }

    @EventLink
    public final Listener<EventTickPre> eventTickPreListener = event -> {
        this.setSuffix(flyMode.getCurrentMode().getName());
    };
}