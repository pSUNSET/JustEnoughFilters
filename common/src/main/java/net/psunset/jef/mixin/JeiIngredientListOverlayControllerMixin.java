package net.psunset.jef.mixin;

import mezz.jei.api.gui.handlers.IGuiProperties;
import mezz.jei.common.util.ImmutableRect2i;
import mezz.jei.gui.overlay.ingredients.IIngredientGridView;
import net.psunset.jef.compat.jei.JeiJefAreaReserver;
import net.psunset.jef.gui.inventory.InventoryOverlayManager;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Set;

@Mixin(targets = "mezz.jei.gui.overlay.IngredientListOverlayController")
public class JeiIngredientListOverlayControllerMixin {

    @Shadow
    @Final
    private IIngredientGridView contentsView;

    @Inject(method = "updateBounds", at = @At("TAIL"))
    private void jef$updateJefBounds(IGuiProperties guiProperties, Set<ImmutableRect2i> guiExclusionAreas, CallbackInfo ci) {
        InventoryOverlayManager.INSTANCE.updateBounds(JeiJefAreaReserver.INSTANCE.getSpace().matchWidthAndX(this.contentsView.getBackgroundArea()).toMutable());
    }
}
