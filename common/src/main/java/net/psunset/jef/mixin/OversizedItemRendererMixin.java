package net.psunset.jef.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.client.gui.render.pip.OversizedItemRenderer;
import net.minecraft.client.gui.render.pip.PictureInPictureRenderer;
import net.minecraft.client.renderer.state.gui.pip.OversizedItemRenderState;
import net.psunset.jef.gui.render.state.ScaledGuiItemRenderState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

@Mixin(OversizedItemRenderer.class)
public abstract class OversizedItemRendererMixin extends PictureInPictureRenderer<OversizedItemRenderState> {
    protected OversizedItemRendererMixin() {
        super();
    }

    @ModifyConstant(method = "renderToTexture(Lnet/minecraft/client/renderer/state/gui/pip/OversizedItemRenderState;Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/SubmitNodeCollector;)V", constant = @Constant(floatValue = 8.0f))
    private float jef$editBias(float raw, @Local(argsOnly = true) OversizedItemRenderState renderState) {
        return renderState.guiItemRenderState() instanceof ScaledGuiItemRenderState state ? state.scale / 2.0f : raw;
    }
}
