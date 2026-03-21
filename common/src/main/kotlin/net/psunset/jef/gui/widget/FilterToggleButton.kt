package net.psunset.jef.gui.widget

import net.minecraft.ChatFormatting
import net.minecraft.client.gui.GuiGraphics
import net.minecraft.client.gui.components.Button
import net.minecraft.network.chat.Component
import net.psunset.jef.api.IToggledFilter
import net.psunset.jef.core.FilterManager
import net.psunset.jef.tool.renderScaledItem
import kotlin.math.min

class FilterToggleButton(
    val filter: IToggledFilter,
    x: Int,
    y: Int,
    width: Int,
    height: Int
) : Button(x, y, width, height, Component.empty(), { FilterManager.toggleFilter(filter) }, DEFAULT_NARRATION) {

    override fun renderWidget(guiGraphics: GuiGraphics, mouseX: Int, mouseY: Int, partialTick: Float) {
        val active = FilterManager.isFilterActive(filter)

        // Render background
        val color = (if (active) 0xFF33CC33 else 0xFF444444).toInt()
        guiGraphics.fill(x, y, x + width, y + height, color)

        // Render icon
        val icon = if (active) filter.activeIcon else filter.inactiveIcon

        val size = min(16, min(width, height) - 2)
        val iconX = x + (width - size) / 2
        val iconY = y + (height - size) / 2
        guiGraphics.renderScaledItem(icon, iconX, iconY, size.toFloat())

        if (isHovered) {
            val filterTooltip = filter.tooltip.copy()
            if (active) {
                filterTooltip.withStyle(ChatFormatting.AQUA)
            } else {
                filterTooltip.withStyle(ChatFormatting.GRAY)
            }
            guiGraphics.setTooltipForNextFrame(filterTooltip, mouseX, mouseY)
        }
    }
}
