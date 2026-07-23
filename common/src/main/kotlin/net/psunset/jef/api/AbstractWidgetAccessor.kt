package net.psunset.jef.api

import net.minecraft.client.gui.components.WidgetTooltipHolder

interface AbstractWidgetAccessor {
    fun `jef$getTooltipHolder`(): WidgetTooltipHolder

    val tooltipHolder: WidgetTooltipHolder
        get() = `jef$getTooltipHolder`()
}
