package net.psunset.jef.gui.widget

import net.minecraft.client.Minecraft
import net.minecraft.client.gui.GuiGraphics
import net.minecraft.client.gui.components.AbstractButton
import net.minecraft.client.gui.components.Button
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

    override fun isValidClickButton(buttonInfo: MouseButtonInfo): Boolean {
        return buttonInfo.button == 0 || buttonInfo.button == 1  // Allow left/right click
    }

    override fun onPress(input: InputWithModifiers) {
        if (input.input() == 1) {  // right
            FilterManager.reverseItemTypeFilter()

        } else {  // left or keybinds
            FilterManager.stepItemTypeFilter()
        }
    }

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
            guiGraphics.setComponentTooltipForNextFrame(
                Minecraft.getInstance().font,
                ItemTypeFilter.genTooltip(filter),
                mouseX,
                mouseY
            )
        }
    }

    /**
     * Vanilla copy: [Button.defaultButtonNarrationText]
     */
    override fun updateWidgetNarration(narrationElementOutput: NarrationElementOutput) {
        this.defaultButtonNarrationText(narrationElementOutput)
    }
}
