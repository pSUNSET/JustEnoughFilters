package net.psunset.jef.gui.inventory.widget

import net.minecraft.client.gui.GuiGraphics
import net.minecraft.client.gui.components.AbstractButton
import net.minecraft.client.gui.components.Tooltip
import net.minecraft.client.gui.narration.NarrationElementOutput
import net.minecraft.client.input.InputWithModifiers
import net.minecraft.client.input.MouseButtonInfo
import net.minecraft.client.renderer.RenderPipelines
import net.minecraft.network.chat.CommonComponents
import net.psunset.jef.builtin.FilterManager
import net.psunset.jef.builtin.ItemTypeFilter
import net.psunset.jef.tool.IdUtl
import net.psunset.jef.tool.renderScaledItem
import kotlin.math.min

class ItemTypeCycleButton(
    x: Int,
    y: Int,
    width: Int,
    height: Int
) : AbstractButton(x, y, width, height, CommonComponents.EMPTY) {

    init {
        setTooltip(Tooltip.create(ItemTypeFilter.genTooltip()))
    }

    override fun isValidClickButton(buttonInfo: MouseButtonInfo): Boolean {
        return buttonInfo.button == 0 || buttonInfo.button == 1
    }

    override fun onPress(input: InputWithModifiers) {
        if (input.input() == 1) {  // right
            FilterManager.reverseItemTypeFilter()
        } else {  // left or keybinds
            FilterManager.stepItemTypeFilter()
        }
        setTooltip(Tooltip.create(ItemTypeFilter.genTooltip()))
    }

    override fun renderContents(guiGraphics: GuiGraphics, mouseX: Int, mouseY: Int, partialTick: Float) {
//        guiGraphics.fill(x, y, x + width, y + height, 0xFF666666.toInt())

        guiGraphics.blit(
            RenderPipelines.GUI_TEXTURED,
            BG,
            x,
            y,
            0.0f,
            0.0f,
            width,
            height,
            16,
            16,
            16,
            16
        )

        val size = min(16, min(width, height) - 2)
        val iconX = x + (width - size) / 2
        val iconY = y + (height - size) / 2
        guiGraphics.renderScaledItem(FilterManager.itemTypeFilter.icon, iconX, iconY, size.toFloat())
    }

    override fun updateWidgetNarration(narrationElementOutput: NarrationElementOutput) {
        this.defaultButtonNarrationText(narrationElementOutput)
    }

    companion object {
        @JvmField
        val BG = IdUtl.ofJef("textures/gui/type_button_bg.png")
    }
}
