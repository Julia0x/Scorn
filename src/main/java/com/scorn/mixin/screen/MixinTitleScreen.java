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
 * Replaces the vanilla title screen with our custom animated title screen
 */
@Mixin(TitleScreen.class)
public class MixinTitleScreen extends Screen {

    protected MixinTitleScreen(Text title) {
        super(title);
    }

    @Inject(method = "init", at = @At("HEAD"), cancellable = true)
    private void replaceWithCustomTitleScreen(CallbackInfo ci) {
        // Replace vanilla title screen with our custom one
        if (this.client != null) {
            this.client.setScreen(new CustomTitleScreen());
        }
        ci.cancel();
    }
}