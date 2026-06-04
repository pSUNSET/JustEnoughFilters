package net.psunset.jef.mixin;

import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.WidgetTooltipHolder;
import net.psunset.jef.api.AbstractWidgetAccessor;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;

@Mixin(AbstractWidget.class)
public class AbstractWidgetMixin implements AbstractWidgetAccessor {

    @Shadow
    @Final
    private WidgetTooltipHolder tooltip;

    @Unique
    @Override
    public WidgetTooltipHolder jef$getTooltipHolder() {
        return tooltip;
    }
}
