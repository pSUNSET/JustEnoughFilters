package net.psunset.jef.gui.widget

import net.minecraft.client.gui.GuiGraphicsExtractor
import net.minecraft.client.gui.components.AbstractButton
import net.minecraft.client.gui.components.Tooltip
import net.minecraft.client.gui.narration.NarrationElementOutput
import net.minecraft.client.input.InputWithModifiers
import net.minecraft.client.input.MouseButtonInfo
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
) : AbstractButton(x, y, width, height, Component.empty()) {

    init {
        setTooltip(Tooltip.create(ItemTypeFilter.genTooltip()))
    }

    override fun isValidClickButton(buttonInfo: MouseButtonInfo): Boolean {
        return buttonInfo.button == 0 || buttonInfo.button == 1  // Allow left/right click
    }

    override fun onPress(input: InputWithModifiers) {
        if (input.input() == 1) {  // right
            FilterManager.reverseItemTypeFilter()
        } else {  // left or keybinds
            FilterManager.stepItemTypeFilter()
        }
        setTooltip(Tooltip.create(ItemTypeFilter.genTooltip()))
    }

    override fun extractContents(graphics: GuiGraphicsExtractor, mouseX: Int, mouseY: Int, a: Float) {
        graphics.fill(x, y, x + width, y + height, 0xFF666666.toInt())

        val size = min(16, min(width, height) - 2)
        val iconX = x + (width - size) / 2
        val iconY = y + (height - size) / 2
        graphics.renderScaledItem(FilterManager.itemTypeFilter.icon, iconX, iconY, size.toFloat())
    }

    /**
     * Vanilla copy: [Button.defaultButtonNarrationText]
     */
    override fun updateWidgetNarration(narrationElementOutput: NarrationElementOutput) {
        this.defaultButtonNarrationText(narrationElementOutput)
    }
}
