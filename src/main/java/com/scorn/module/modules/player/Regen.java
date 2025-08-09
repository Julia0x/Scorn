package com.scorn.module.modules.player;

import com.scorn.event.bus.Listener;
import com.scorn.event.bus.annotations.EventLink;
import com.scorn.event.impl.player.EventTickPre;
import com.scorn.module.Module;
import com.scorn.module.ModuleCategory;
import com.scorn.module.modules.player.regen.FullRegen;
import com.scorn.module.modules.player.regen.VanillaRegen;
import com.scorn.module.setting.impl.NumberSetting;
import com.scorn.module.setting.impl.newmodesetting.NewModeSetting;

public class Regen extends Module {
    public final NewModeSetting regenMode = new NewModeSetting("Regen Mode", "Vanilla",
            new VanillaRegen("Vanilla", this),
            new FullRegen("Full", this));

    public final NumberSetting health = new NumberSetting("Health", 1, 20, 10, 1);
    public final NumberSetting packets = new NumberSetting("Packets", 1, 100, 30, 1);

    public Regen() {
        super("Regen", "Regen your health", 0, ModuleCategory.PLAYER);
        this.addSettings(regenMode, health, packets);
    }

    @EventLink
    public final Listener<EventTickPre> eventTickPreListener = event -> {
      this.setSuffix(regenMode.getCurrentMode().getName());
    };
}
