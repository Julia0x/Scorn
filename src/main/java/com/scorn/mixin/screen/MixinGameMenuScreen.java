package com.scorn.mixin.screen;

import com.scorn.gui.AltManagerGui;
import net.minecraft.client.gui.screen.GameMenuScreen;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GameMenuScreen.class)
public class MixinGameMenuScreen extends Screen {

    protected MixinGameMenuScreen(Text title) {
        super(title);
    }

    @Inject(method = "initWidgets", at = @At("TAIL"))
    private void addAltManagerButton(CallbackInfo ci) {
        // Add Alt Manager button to the pause menu
        this.addDrawableChild(ButtonWidget.builder(Text.literal("Alt Manager"), button -> {
            if (this.client != null) {
                this.client.setScreen(new AltManagerGui());
            }
        }).dimensions(this.width / 2 - 100, this.height / 4 + 96, 200, 20).build());
    }
}