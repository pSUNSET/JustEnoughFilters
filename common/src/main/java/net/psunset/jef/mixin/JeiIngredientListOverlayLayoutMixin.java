package net.psunset.jef.mixin;

import mezz.jei.common.util.ImmutableRect2i;
import net.psunset.jef.compat.jei.JeiJefAreaReserver;
import net.psunset.jef.gui.inventory.FilterBarOverlay;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(targets = "mezz.jei.gui.overlay.IngredientListOverlayLayout")
public class JeiIngredientListOverlayLayoutMixin {

    @Inject(method = "getAvailableContentsArea", at = @At("RETURN"), cancellable = true)
    private static void jef$reserveSpace(ImmutableRect2i displayArea, boolean searchBarCentered, CallbackInfoReturnable<ImmutableRect2i> cir) {
        ImmutableRect2i availableContentsArea = cir.getReturnValue();

        ImmutableRect2i cropped = availableContentsArea.cropBottom(FilterBarOverlay.REVERSED_HEIGHT + 4);

        JeiJefAreaReserver.INSTANCE.setSpace(availableContentsArea.keepBottom(FilterBarOverlay.REVERSED_HEIGHT + 4));

        cir.setReturnValue(cropped);
    }
}
