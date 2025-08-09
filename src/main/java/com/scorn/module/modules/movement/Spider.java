package com.scorn.module.modules.movement;

import com.scorn.event.bus.Listener;
import com.scorn.event.bus.annotations.EventLink;
import com.scorn.event.impl.player.EventTickPre;
import com.scorn.module.ModuleCategory;
import com.scorn.module.Module;
import com.scorn.module.modules.movement.spider.VanillaSpider;
import com.scorn.module.modules.movement.spider.VerusSpider;
import com.scorn.module.modules.movement.spider.VulcanSpider;
import com.scorn.module.setting.impl.NumberSetting;
import com.scorn.module.setting.impl.newmodesetting.NewModeSetting;

public class Spider extends Module {

    public final NewModeSetting spiderMode = new NewModeSetting("Mode", "Vanilla",
            new VanillaSpider("Vanilla", this),
            new VerusSpider("Verus", this),
            new VulcanSpider("Vulcan", this));

    public final NumberSetting verticalMotion = new NumberSetting("Vertical Motion", 0.1, 1, 0.42, 0.01);

    public Spider() {
        super("Spider", "Allows you to climb walls", 0, ModuleCategory.MOVEMENT);
        this.addSettings(spiderMode, verticalMotion);

        verticalMotion.addDependency(spiderMode, "Vanilla");
    }

    @EventLink
    public final Listener<EventTickPre> eventTickListener = event -> {
        this.setSuffix(spiderMode.getCurrentMode().getName());
    };
}
