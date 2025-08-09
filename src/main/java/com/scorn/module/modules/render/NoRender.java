package com.scorn.module.modules.render;

import com.scorn.event.bus.Listener;
import com.scorn.event.bus.annotations.EventLink;
import com.scorn.event.impl.player.EventTickPre;
import com.scorn.module.ModuleCategory;
import com.scorn.module.Module;
import com.scorn.module.setting.impl.BooleanSetting;
import net.minecraft.entity.effect.StatusEffects;

public class NoRender extends Module {
    public static BooleanSetting hurtCam = new BooleanSetting("HurtCam", false);
    public static BooleanSetting blindness = new BooleanSetting("Blindness", false);
    public static BooleanSetting darkness = new BooleanSetting("Darkness", false);

    public NoRender() {
        super("NoRender", "Removes the hurt tilt and blindness", 0, ModuleCategory.RENDER);
        this.addSettings(hurtCam, blindness, darkness);
    }

    @EventLink
    public final Listener<EventTickPre> eventTickPreListener = event -> {
        if (isNull()) {
            return;
        }
        if (blindness.getValue() && mc.player.hasStatusEffect(StatusEffects.BLINDNESS))
            mc.player.removeStatusEffect(StatusEffects.BLINDNESS);
        if (darkness.getValue() && mc.player.hasStatusEffect(StatusEffects.DARKNESS))
            mc.player.removeStatusEffect(StatusEffects.DARKNESS);
    };
}
