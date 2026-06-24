package net.psunset.jef.mixin;

import net.minecraft.client.renderer.state.gui.GuiItemRenderState;
import net.minecraft.client.renderer.state.gui.pip.OversizedItemRenderState;
import net.minecraft.client.renderer.state.gui.pip.PictureInPictureRenderState;
import net.psunset.jef.gui.render.state.ScaledGuiItemRenderState;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(OversizedItemRenderState.class)
public abstract class OversizedItemRenderStateMixin implements PictureInPictureRenderState {
    @Shadow
    @Final
    private GuiItemRenderState guiItemRenderState;

    @Inject(method = "scale", at = @At("RETURN"), cancellable = true)
    public void jef$editScale(CallbackInfoReturnable<Float> cir) {
        cir.setReturnValue(this.guiItemRenderState instanceof ScaledGuiItemRenderState state ? state.scale : cir.getReturnValue());
    }
}
