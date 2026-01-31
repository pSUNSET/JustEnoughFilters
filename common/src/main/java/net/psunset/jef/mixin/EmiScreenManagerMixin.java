package net.psunset.jef.mixin;

import dev.emi.emi.api.widget.Bounds;
import dev.emi.emi.runtime.EmiDrawContext;
import dev.emi.emi.screen.EmiScreenManager;
import net.minecraft.client.Minecraft;
import net.psunset.jef.JefCore;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(EmiScreenManager.class)
public class EmiScreenManagerMixin {
    @Unique
    private static final int jef$BUTTON_SIZE = 20;
    @Unique
    private static final int jef$INNER_PADDING = 2;
    @Unique
    private static final int jef$RESERVED_HEIGHT = jef$BUTTON_SIZE + jef$INNER_PADDING;

    @Unique
    private static Bounds jef$reservedArea;

    @ModifyArg(
            method = "recalculate",
            at = @At(
                    value = "INVOKE",
                    target = "Ldev/emi/emi/screen/EmiScreenManager;createScreenSpace(Ldev/emi/emi/screen/EmiScreenManager$SidebarPanel;Lnet/minecraft/client/gui/screens/Screen;Ljava/util/List;ZLdev/emi/emi/api/widget/Bounds;Ldev/emi/emi/config/SidebarSettings;)V",
                    ordinal = 1
            ),
            index = 4
    )
    private static Bounds jef$reserveSpace(Bounds bounds) {
        jef$reservedArea = new Bounds(
                bounds.x(),
                bounds.y() + bounds.height() - jef$RESERVED_HEIGHT,
                bounds.width(),
                jef$RESERVED_HEIGHT
        );
        return new Bounds(
                bounds.x(),
                bounds.y(),
                bounds.width(),
                bounds.height() - jef$RESERVED_HEIGHT
        );
    }

    @Inject(method = "recalculate", at = @At("TAIL"))
    private static void jef$updateJefBounds(CallbackInfo ci) {
        if (jef$reservedArea!=null) {
            JefCore.INSTANCE.updateBounds(
                    jef$reservedArea.x(),
                    jef$reservedArea.y(),
                    jef$reservedArea.width(),
                    jef$reservedArea.height()
            );
        }
    }

    @Inject(method = "render", at = @At("TAIL"))
    private static void jef$drawFilterBar(EmiDrawContext context, int mouseX, int mouseY, float delta, CallbackInfo ci) {
        JefCore.INSTANCE.drawFilterBar(Minecraft.getInstance().screen, context.raw(), mouseX, mouseY);
    }
}
