package net.psunset.jef.mixin;

import dev.emi.emi.api.widget.Bounds;
import dev.emi.emi.config.EmiConfig;
import dev.emi.emi.config.SidebarSide;
import dev.emi.emi.runtime.EmiDrawContext;
import dev.emi.emi.screen.EmiScreenManager;
import dev.emi.emi.screen.widget.EmiSearchWidget;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.psunset.jef.JustEnoughFilters;
import net.psunset.jef.gui.FilterBarOverlay;
import net.psunset.jef.gui.JefOverlayManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(value = EmiScreenManager.class, remap = false)
public class EmiScreenManagerMixin {
    @Shadow
    private static List<EmiScreenManager.SidebarPanel> panels;
    @Shadow
    public static EmiSearchWidget search;

    @Inject(method = "addWidgets", at = @At(value = "INVOKE", target = "Ldev/emi/emi/screen/widget/EmiSearchWidget;setVisible(Z)V", shift = At.Shift.AFTER), remap = true)
    private static void jef$updateJefBounds(Screen screen, CallbackInfo ci) {
        Bounds bounds;
        if (!EmiConfig.centerSearchBar &&
                EmiConfig.searchSidebar == SidebarSide.RIGHT &&
                search.isVisible()) {
            JustEnoughFilters.LOGGER.info("Set Jef Bar to above search bar!");
            bounds = new Bounds(search.getX(), search.getY() - FilterBarOverlay.REVERSED_HEIGHT, search.getWidth(), FilterBarOverlay.REVERSED_HEIGHT);
        } else {
            JustEnoughFilters.LOGGER.info("Set Jef Bar to bottom of item list!");
            Bounds right = panels.get(1).getBounds();
            bounds = new Bounds(right.x(), screen.height - FilterBarOverlay.REVERSED_HEIGHT, right.width(), FilterBarOverlay.REVERSED_HEIGHT);
        }
        JefOverlayManager.INSTANCE.updateBounds(bounds.x(), bounds.y(), bounds.width(), bounds.height());
    }

    @Inject(method = "render", at = @At("TAIL"))
    private static void jef$drawFilterBar(EmiDrawContext context, int mouseX, int mouseY, float delta, CallbackInfo ci) {
        JefOverlayManager.INSTANCE.drawFilterBar(Minecraft.getInstance().screen, context.raw(), mouseX, mouseY);
    }
}