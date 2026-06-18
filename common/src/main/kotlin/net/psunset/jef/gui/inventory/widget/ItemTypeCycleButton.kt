package net.psunset.jef.gui.inventory.widget

import net.minecraft.client.gui.GuiGraphics
import net.minecraft.client.gui.components.Tooltip
import net.minecraft.network.chat.CommonComponents
import net.psunset.jef.builtin.FilterManager
import net.psunset.jef.builtin.ItemTypeFilter
import net.psunset.jef.gui.widget.AbstractLeftRightClickButton
import net.psunset.jef.tool.renderScaledItem
import kotlin.math.min

class ItemTypeCycleButton(
    x: Int,
    y: Int,
    width: Int,
    height: Int
) : AbstractLeftRightClickButton(x, y, width, height, CommonComponents.EMPTY) {

    init {
        tooltip = Tooltip.create(ItemTypeFilter.genTooltip())
    }

    override fun onPress() {
        FilterManager.stepItemTypeFilter()
        tooltip = Tooltip.create(ItemTypeFilter.genTooltip())
    }

    override fun onRightPress() {
        FilterManager.reverseItemTypeFilter()
        tooltip = Tooltip.create(ItemTypeFilter.genTooltip())
    }

    override fun renderWidget(guiGraphics: GuiGraphics, mouseX: Int, mouseY: Int, partialTick: Float) {
        guiGraphics.fill(x, y, x + width, y + height, 0xFF666666.toInt())

        val size = min(16, min(width, height) - 2)
        val iconX = x + (width - size) / 2
        val iconY = y + (height - size) / 2
        guiGraphics.renderScaledItem(FilterManager.itemTypeFilter.icon, iconX, iconY, size.toFloat())
    }
}
