package net.psunset.jef.mixin;

import mezz.jei.api.gui.handlers.IGuiProperties;
import mezz.jei.common.util.ImmutableRect2i;
import mezz.jei.gui.overlay.IngredientGridWithNavigation;
import mezz.jei.gui.overlay.IngredientListOverlay;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.psunset.jef.gui.FilterBarOverlay;
import net.psunset.jef.gui.JefOverlayManager;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Set;

@Mixin(IngredientListOverlay.class)
public class JeiIngredientListOverlayMixin {
    @Shadow(remap = false)
    @Final
    private IngredientGridWithNavigation contents;

    @Unique
    private ImmutableRect2i jef$reservedArea;

    @Inject(method = "getAvailableContentsArea", at = @At("RETURN"), cancellable = true, remap = false)
    private void jef$reserveSpace(ImmutableRect2i displayArea, boolean searchBarCentered, CallbackInfoReturnable<ImmutableRect2i> cir) {
        ImmutableRect2i availableContentsArea = cir.getReturnValue();

        // Crop bottom to reserve space for filter bar
        ImmutableRect2i cropped = availableContentsArea.cropBottom(FilterBarOverlay.REVERSED_HEIGHT);

        // Store the reserved area (the space we just cropped)
        this.jef$reservedArea = availableContentsArea.keepBottom(FilterBarOverlay.REVERSED_HEIGHT);

        cir.setReturnValue(cropped);
    }

    @Inject(method = "updateBounds", at = @At("TAIL"), remap = false)
    private void jef$updateJefBounds(IGuiProperties guiProperties, ImmutableRect2i displayArea, Set<ImmutableRect2i> guiExclusionAreas, CallbackInfo ci) {
        JefOverlayManager.INSTANCE.updateBounds(this.jef$reservedArea.matchWidthAndX(this.contents.getBackgroundArea()).toMutable());
    }

    @Inject(method = "drawScreen", at = @At(value = "INVOKE", target = "Lmezz/jei/gui/overlay/IngredientGridWithNavigation;draw(Lnet/minecraft/client/Minecraft;Lnet/minecraft/client/gui/GuiGraphics;IIF)V", shift = At.Shift.AFTER))
    private void jef$drawFilterBar(Minecraft minecraft, GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTicks, CallbackInfo ci) {
        JefOverlayManager.INSTANCE.drawFilterBar(minecraft.screen, guiGraphics, mouseX, mouseY);
    }
}
