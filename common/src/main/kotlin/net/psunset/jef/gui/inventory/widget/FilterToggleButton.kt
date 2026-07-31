package net.psunset.jef.gui.inventory.widget

import net.minecraft.ChatFormatting
import net.minecraft.client.gui.GuiGraphicsExtractor
import net.minecraft.client.gui.components.Button
import net.minecraft.client.gui.components.Tooltip
import net.minecraft.network.chat.CommonComponents
import net.psunset.jef.api.IToggledFilter
import net.psunset.jef.builtin.FilterManager
import net.psunset.jef.tool.scaledItem
import kotlin.math.min

class FilterToggleButton(
    val filter: IToggledFilter,
    x: Int,
    y: Int,
    width: Int,
    height: Int
) : Button.Plain(
    x,
    y,
    width,
    height,
    CommonComponents.EMPTY,
    {
        FilterManager.toggleFilter(filter)
        (it as FilterToggleButton).refreshTooltip()
    },
    DEFAULT_NARRATION
) {

    init {
        refreshTooltip()
    }

    override fun extractContents(graphics: GuiGraphicsExtractor, mouseX: Int, mouseY: Int, a: Float) {
        val active = FilterManager.isFilterEnabled(filter)

        val color = (if (active) 0xFF33CC33 else 0xFF444444).toInt()
        graphics.fill(x, y, x + width, y + height, color)

        val icon = if (active) filter.activeIcon else filter.inactiveIcon

        val size = min(16, min(width, height) - 2)
        val iconX = x + (width - size) / 2
        val iconY = y + (height - size) / 2
        graphics.scaledItem(icon, iconX, iconY, size.toFloat())
    }

    internal fun refreshTooltip() {
        setTooltip(
            Tooltip.create(
                filter.tooltip.copy().withStyle(
                    if (FilterManager.isFilterEnabled(filter))
                        ChatFormatting.AQUA
                    else
                        ChatFormatting.GRAY
                )
            )
        )
    }
}
