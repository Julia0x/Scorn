package com.scorn.mixin.keyboard;

import com.scorn.Client;
import com.scorn.gui.FriendsGui;
import com.scorn.module.Module;
import net.minecraft.client.Keyboard;
import net.minecraft.client.MinecraftClient;
import org.lwjgl.glfw.GLFW;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Keyboard.class)
public class MixinKeyboard {
    @Shadow @Final private MinecraftClient client;

    @Inject(method = "onKey", at = @At("HEAD"))
    private void onKeyInject(long window, int key, int scancode, int action, int modifiers, CallbackInfo ci) {
        if(window == this.client.getWindow().getHandle()) {
            if(this.client.currentScreen == null) {
                // Handle apostrophe key for Friends GUI
                if(key == GLFW.GLFW_KEY_APOSTROPHE && action == GLFW.GLFW_PRESS) {
                    this.client.setScreen(new FriendsGui());
                    return;
                }
                
                // Handle module keybinds
                for(Module module : Client.INSTANCE.getModuleManager().getModules()) {
                    if(key == module.getKey() && action == GLFW.GLFW_PRESS) {
                        module.toggle();
                    }
                }
            }
        }
    }

}
