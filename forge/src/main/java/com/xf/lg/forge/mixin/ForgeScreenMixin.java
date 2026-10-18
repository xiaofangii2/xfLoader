package com.xf.lg.forge.mixin;

import com.xf.lg.gui.ScreenHook;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.TitleScreen;
import net.minecraft.client.gui.screens.PauseScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

public class ForgeScreenMixin {
    
    @Mixin(TitleScreen.class)
    public static class TitleScreenMixin {
        @Inject(method = "init", at = @At("RETURN"))
        private void xfLoader$addButton(CallbackInfo ci) {
            ScreenHook.addButton((Screen)(Object)this);
        }
    }

    @Mixin(PauseScreen.class)
    public static class PauseScreenMixin {
        @Inject(method = "init", at = @At("RETURN"))
        private void xfLoader$addButton(CallbackInfo ci) {
            ScreenHook.addButton((Screen)(Object)this);
        }
    }
}
