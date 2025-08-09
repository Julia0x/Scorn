package com.scorn.module.modules.client;

import com.scorn.gui.clickgui.imgui.ClickGui;
import com.scorn.module.ModuleCategory;
import com.scorn.module.Module;
import com.scorn.module.setting.impl.ModeSetting;
import org.lwjgl.glfw.GLFW;

public class ClickGUI extends Module {
    private boolean didInitImgui = false;
    public static final ModeSetting clickguiMode = new ModeSetting("Mode", "ImGui", "ImGui", "Old");

    public ClickGUI() {
        super("ClickGui", "Click Gui", GLFW.GLFW_KEY_RIGHT_SHIFT, ModuleCategory.CLIENT);
        this.addSetting(clickguiMode);
    }

    @Override
    public void onEnable() {
        if (clickguiMode.isMode("ImGui")) {
            if (mc.getWindow() != null && !didInitImgui) {
                //ImGuiImpl.initialize(mc.getWindow().getHandle());
                didInitImgui = true;
            }
            mc.setScreen(new ClickGui());
            this.toggle();
        }

        if (clickguiMode.isMode("Old")) {
            mc.setScreen(new com.scorn.gui.clickgui.old.ClickGui());
            this.toggle();
        }
        super.onEnable();
    }
}
