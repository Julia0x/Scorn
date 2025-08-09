package com.scorn.module.modules.player;

import com.scorn.event.bus.Listener;
import com.scorn.event.bus.annotations.EventLink;
import com.scorn.event.impl.player.EventTickPre;
import com.scorn.module.ModuleCategory;
import com.scorn.module.Module;
import com.scorn.module.modules.player.antivoid.BlinkAntiVoid;
import com.scorn.module.modules.player.antivoid.MospixelAntiVoid;
import com.scorn.module.modules.player.antivoid.MotionFlagAntiVoid;
import com.scorn.module.modules.player.antivoid.VanillaAntiVoid;
import com.scorn.module.setting.impl.NumberSetting;
import com.scorn.module.setting.impl.newmodesetting.NewModeSetting;

public class AntiVoid extends Module {
    public NewModeSetting mode = new NewModeSetting("AntiVoid Mode", "Vanilla", new VanillaAntiVoid("Vanilla", this), new MotionFlagAntiVoid("MotionFlag", this), new MospixelAntiVoid("Mospixel", this), new BlinkAntiVoid("Blink", this));
    public final NumberSetting minFallDistance = new NumberSetting("Min AntiVoid Distance", 2, 30, 8, 1);

    public AntiVoid() {
        super("AntiVoid", "Prevents you from falling in the void", 0, ModuleCategory.PLAYER);
        this.addSettings(mode, minFallDistance);
    }

    @EventLink
    public final Listener<EventTickPre> eventTickPreListener = event -> {
        this.setSuffix(mode.getCurrentMode().getName());
    };
}
