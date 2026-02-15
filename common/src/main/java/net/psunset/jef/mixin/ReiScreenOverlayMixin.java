package net.psunset.jef.mixin;

import me.shedaniel.math.Rectangle;
import me.shedaniel.rei.api.client.REIRuntime;
import me.shedaniel.rei.api.client.gui.config.SearchFieldLocation;
import me.shedaniel.rei.impl.client.gui.ScreenOverlayImpl;
import net.minecraft.client.Minecraft;
import net.psunset.jef.gui.FilterBarOverlay;
import net.psunset.jef.gui.JefOverlayManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = ScreenOverlayImpl.class, remap = false)
public class ReiScreenOverlayMixin {

    @Inject(method = "calculateOverlayBounds", at = @At("RETURN"), cancellable = true)
    private static void jef$reserveSpace(CallbackInfoReturnable<Rectangle> cir) {
        Rectangle original = cir.getReturnValue();

        if (!original.isEmpty()) {

            if (REIRuntime.getInstance().getContextualSearchFieldLocation()==SearchFieldLocation.BOTTOM_SIDE) {
                JefOverlayManager.INSTANCE.updateBounds(
                        original.x,
                        original.y + original.height - FilterBarOverlay.REVERSED_HEIGHT - 24,  // 24 = reservedSearchFieldHeight
                        original.width,
                        FilterBarOverlay.REVERSED_HEIGHT
                );
            } else {
                JefOverlayManager.INSTANCE.updateBounds(
                        original.x,
                        original.y + original.height - FilterBarOverlay.REVERSED_HEIGHT,
                        original.width,
                        FilterBarOverlay.REVERSED_HEIGHT
                );
            }

            // Crop the original area
            Rectangle cropped = new Rectangle(
                    original.x,
                    original.y,
                    original.width,
                    original.height - FilterBarOverlay.REVERSED_HEIGHT
            );
            cir.setReturnValue(cropped);
        }
    }

    @Inject(method = "render", at = @At(value = "INVOKE", target = "Lme/shedaniel/rei/impl/client/gui/ScreenOverlayImpl;renderWidgets(Lnet/minecraft/client/gui/GuiGraphics;IIF)V", shift = At.Shift.AFTER), remap = true)
    private void jef$drawFilterBar(net.minecraft.client.gui.GuiGraphics guiGraphics, int mouseX, int mouseY, float delta, CallbackInfo ci) {
        JefOverlayManager.INSTANCE.drawFilterBar(Minecraft.getInstance().screen, guiGraphics, mouseX, mouseY);
    }
}