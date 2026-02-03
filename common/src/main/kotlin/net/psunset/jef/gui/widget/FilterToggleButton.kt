package net.psunset.jef.gui.widget

import net.minecraft.ChatFormatting
import net.minecraft.client.Minecraft
import net.minecraft.client.gui.GuiGraphics
import net.minecraft.client.gui.components.Button
import net.minecraft.network.chat.Component
import net.psunset.jef.core.FilterManager
import net.psunset.jef.core.ToggledFilter

class FilterToggleButton(
    val filter: ToggledFilter,
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

        // Center the icon
        val icon = if (active) filter.activeIcon else filter.inactiveIcon
        val iconX = x + (width - 16) / 2
        val iconY = y + (height - 16) / 2
        guiGraphics.renderItem(icon, iconX, iconY)

        if (isHovered) {
            val filterTooltip = filter.tooltip.copy()
            if (active) {
                filterTooltip.withStyle(ChatFormatting.AQUA)
            } else {
                filterTooltip.withStyle(ChatFormatting.GRAY)
            }
            guiGraphics.renderTooltip(Minecraft.getInstance().font, filterTooltip, mouseX, mouseY)
        }
    }
}
