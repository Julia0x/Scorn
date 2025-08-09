package com.scorn.mixin.screen;

import com.scorn.gui.AltManagerGui;
import com.scorn.gui.CustomTitleScreen;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.TitleScreen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * Adds custom functionality to the title screen while maintaining vanilla compatibility
 */
@Mixin(TitleScreen.class)
public class MixinTitleScreen extends Screen {

    protected MixinTitleScreen(Text title) {
        super(title);
    }

    @Inject(method = "initWidgetsNormal", at = @At("TAIL"))
    private void addCustomButtons(int y, int spacingY, CallbackInfo ci) {
        // Add Alt Manager button to the vanilla title screen
        this.addDrawableChild(ButtonWidget.builder(Text.literal("Alt Manager"), button -> {
            if (this.client != null) {
                this.client.setScreen(new AltManagerGui());
            }
        }).dimensions(this.width / 2 - 100, y + spacingY * 2, 200, 20).build());
        
        // Add Custom Title Screen button for optional use
        this.addDrawableChild(ButtonWidget.builder(Text.literal("Custom Theme"), button -> {
            if (this.client != null) {
                this.client.setScreen(new CustomTitleScreen());
            }
        }).dimensions(this.width / 2 - 100, y + spacingY * 3, 200, 20).build());
    }
}