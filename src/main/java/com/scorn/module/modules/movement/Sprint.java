package com.scorn.module.modules.movement;

import com.scorn.Client;
import com.scorn.event.bus.Listener;
import com.scorn.event.bus.annotations.EventLink;
import com.scorn.event.impl.player.EventTickPre;
import com.scorn.mixin.accesors.GameOptionsAccessor;
import com.scorn.mixin.accesors.KeyBindingAccessor;
import com.scorn.module.Module;
import com.scorn.module.ModuleCategory;
import com.scorn.module.modules.player.Scaffold;
import com.scorn.module.setting.impl.BooleanSetting;
import net.minecraft.client.option.KeyBinding;

public class Sprint extends Module {
    public static BooleanSetting rage = new BooleanSetting("Rage", false);

    public Sprint() {
        super("Sprint", "Sprints for you", 0, ModuleCategory.MOVEMENT);
        this.addSetting(rage);
    }

    public static boolean shouldSprintDiagonally() {
        if (Client.INSTANCE.getModuleManager().getModule(Scaffold.class).isEnabled())
            return false;
        return rage.getValue();
    }

    @EventLink
    public final Listener<EventTickPre> eventTickPreListener = event -> {
        if (isNull()) {
            return;
        }
        this.setSuffix(rage.getValue() ? "Rage" : "Legit");
        if (Client.INSTANCE.getModuleManager().getModule(Scaffold.class).isEnabled())
            return;

        ((GameOptionsAccessor) mc.options).getSprintToggled().setValue(false);
        KeyBinding.setKeyPressed(((KeyBindingAccessor) mc.options.sprintKey).getBoundKey(), true);
    };
}
