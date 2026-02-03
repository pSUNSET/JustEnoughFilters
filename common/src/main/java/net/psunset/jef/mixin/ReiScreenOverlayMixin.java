package net.psunset.jef.mixin;

import me.shedaniel.math.Rectangle;
import me.shedaniel.rei.impl.client.gui.ScreenOverlayImpl;
import net.minecraft.client.Minecraft;
import net.psunset.jef.gui.JefOverlayManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ScreenOverlayImpl.class)
public class ReiScreenOverlayMixin {
    @Unique
    private static final int jef$BUTTON_SIZE = 20;
    @Unique
    private static final int jef$INNER_PADDING = 2;
    @Unique
    private static final int jef$RESERVED_HEIGHT = jef$BUTTON_SIZE + jef$INNER_PADDING;
    @Unique
    private static Rectangle jef$reservedArea;

    @Inject(method = "calculateOverlayBounds", at = @At("RETURN"), cancellable = true)
    private static void jef$reserveSpace(CallbackInfoReturnable<Rectangle> cir) {
        Rectangle original = cir.getReturnValue();
        if (original != null && !original.isEmpty()) {
            // Reserve space at the bottom
            jef$reservedArea = new Rectangle(
                    original.x,
                    original.y + original.height - jef$RESERVED_HEIGHT,
                    original.width,
                    jef$RESERVED_HEIGHT
            );

            // Crop the original area
            Rectangle cropped = new Rectangle(
                    original.x,
                    original.y,
                    original.width,
                    original.height - jef$RESERVED_HEIGHT
            );
            cir.setReturnValue(cropped);
        }
    }

    @Inject(method = "init", at = @At("TAIL"))
    private void jef$updateJefBounds(CallbackInfo ci) {
        if (jef$reservedArea != null) {
            JefOverlayManager.INSTANCE.updateBounds(
                    jef$reservedArea.x,
                    jef$reservedArea.y,
                    jef$reservedArea.width,
                    jef$reservedArea.height
            );
        }
    }

    @Inject(method = "render", at = @At(value = "INVOKE", target = "Lme/shedaniel/rei/impl/client/gui/ScreenOverlayImpl;renderWidgets(Lnet/minecraft/client/gui/GuiGraphics;IIF)V", shift = At.Shift.AFTER))
    private void jef$drawFilterBar(net.minecraft.client.gui.GuiGraphics guiGraphics, int mouseX, int mouseY, float delta, CallbackInfo ci) {
        JefOverlayManager.INSTANCE.drawFilterBar(Minecraft.getInstance().screen, guiGraphics, mouseX, mouseY);
    }
}
