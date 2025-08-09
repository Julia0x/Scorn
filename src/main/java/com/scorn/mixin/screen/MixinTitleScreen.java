package com.scorn.mixin.screen;

import com.scorn.gui.CustomTitleScreen;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.TitleScreen;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * Completely replaces the vanilla title screen with our custom one
 */
@Mixin(TitleScreen.class)
public class MixinTitleScreen extends Screen {

    protected MixinTitleScreen(Text title) {
        super(title);
    }

    @Inject(method = "init", at = @At("HEAD"), cancellable = true)
    private void replaceWithCustomTitleScreen(CallbackInfo ci) {
        // Completely replace vanilla title screen
        try {
            if (this.client != null) {
                this.client.setScreen(new CustomTitleScreen());
            }
            ci.cancel();
        } catch (Exception e) {
            // If custom screen fails, continue with vanilla
            // ci.cancel() is not called, so vanilla initialization continues
        }
    }
}