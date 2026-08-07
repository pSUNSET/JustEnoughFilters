package net.psunset.jef.mixin;

import mezz.jei.gui.overlay.IngredientListOverlay;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.psunset.jef.gui.inventory.InventoryOverlayManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(IngredientListOverlay.class)
public abstract class JeiIngredientListOverlayMixin {

    @Inject(method = "drawForeground", at = @At(value = "INVOKE", target = "Lmezz/jei/gui/overlay/ingredients/IIngredientListOverlayContents;drawForeground(Lnet/minecraft/client/Minecraft;Lnet/minecraft/client/gui/GuiGraphicsExtractor;IIF)V", shift = At.Shift.AFTER))
    private void jef$drawFilterBar(Minecraft minecraft, GuiGraphicsExtractor guiGraphics, int mouseX, int mouseY, float partialTicks, CallbackInfo ci) {
        InventoryOverlayManager.INSTANCE.drawFilterBar(minecraft.gui.screen(), guiGraphics, mouseX, mouseY);
    }
}