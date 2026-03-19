package net.psunset.jef.gui.widget

import net.minecraft.client.Minecraft
import net.minecraft.client.gui.GuiGraphics
import net.minecraft.client.gui.components.Button
import net.minecraft.network.chat.Component
import net.psunset.jef.core.FilterManager
import net.psunset.jef.core.ItemTypeFilter
import net.psunset.jef.tool.renderScaledItem
import kotlin.math.min

class ItemTypeCycleButton(
    x: Int,
    y: Int,
    width: Int,
    height: Int
) : Button(x, y, width, height, Component.empty(), { FilterManager.stepItemTypeFilter() }, DEFAULT_NARRATION) {

    override fun renderWidget(guiGraphics: GuiGraphics, mouseX: Int, mouseY: Int, partialTick: Float) {
        val filter = FilterManager.itemTypeFilter

        // Draw background
        guiGraphics.fill(x, y, x + width, y + height, 0xFF666666.toInt())

        // Render Icon
        val size = min(16, min(width, height) - 2)
        val iconX = x + (width - size) / 2
        val iconY = y + (height - size) / 2
        guiGraphics.renderScaledItem(filter.icon, iconX, iconY, size.toFloat())

        if (isHovered) {
            guiGraphics.renderComponentTooltip(
                Minecraft.getInstance().font,
                ItemTypeFilter.genTooltip(filter),
                mouseX,
                mouseY
            )
        }
    }
}
