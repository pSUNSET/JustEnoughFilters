package net.psunset.jef.mixin;

import net.minecraft.client.gui.GuiGraphics;
import net.psunset.jef.tool.GraphicsUtl;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GuiGraphics.class)
public class GuiGraphicsMixin {
    @Inject(method = "renderDeferredElements", at = @At("HEAD"))
    private void jef$runDeferredRenderers(CallbackInfo ci) {
        GraphicsUtl.runDeferredRenderers();
    }
}
