package net.psunset.jef.mixin;

import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.psunset.jef.tool.GraphicsUtl;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GuiGraphicsExtractor.class)
public class GuiGraphicsExtractorMixin {
    @Inject(method = "extractDeferredElements", at = @At("HEAD"))
    private void jef$runDeferredRenderers(CallbackInfo ci) {
        GraphicsUtl.runDeferredExtractors();
    }
}
