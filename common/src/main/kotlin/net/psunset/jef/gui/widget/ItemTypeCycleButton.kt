package net.psunset.jef.gui.widget

import net.minecraft.client.Minecraft
import net.minecraft.client.gui.GuiGraphics
import net.minecraft.client.gui.components.Button
import net.minecraft.network.chat.Component
import net.psunset.jef.core.FilterManager
import net.psunset.jef.core.ItemTypeFilter

class ItemTypeCycleButton(
    x: Int,
    y: Int,
    width: Int,
    height: Int
) : Button(x, y, width, height, Component.empty(), { FilterManager.stepItemTypeFilter() }, DEFAULT_NARRATION) {

    override fun renderWidget(guiGraphics: GuiGraphics, mouseX: Int, mouseY: Int, partialTick: Float) {
        // Draw background
        guiGraphics.fill(x, y, x + width, y + height, 0xFF666666.toInt())

        // Draw text or icon representing current mode
        val filter = FilterManager.itemTypeFilter

        // Render Icon
        val icon = filter.icon
        // Center the icon
        val iconX = x + (width - 16) / 2
        val iconY = y + (height - 16) / 2
        guiGraphics.renderItem(icon, iconX, iconY)

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
