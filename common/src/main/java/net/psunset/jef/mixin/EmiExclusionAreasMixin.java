package net.psunset.jef.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import dev.emi.emi.api.widget.Bounds;
import dev.emi.emi.registry.EmiExclusionAreas;
import dev.emi.emi.screen.EmiScreenBase;
import net.minecraft.client.renderer.Rect2i;
import net.psunset.jef.gui.JefOverlayManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;

@Mixin(EmiExclusionAreas.class)
public class EmiExclusionAreasMixin {

    @Inject(method = "getExclusion", at = @At("RETURN"))
    private static void jef$reserveSpace(EmiScreenBase base, CallbackInfoReturnable<List<Bounds>> cir, @Local(name = "list") List<Bounds> list) {
        Rect2i overlayBounds = JefOverlayManager.INSTANCE.getOverlayBounds();
        if (overlayBounds != null) {
            list.add(new Bounds(overlayBounds.getX(), overlayBounds.getY(), overlayBounds.getWidth(), overlayBounds.getHeight()));
        }
    }
}
