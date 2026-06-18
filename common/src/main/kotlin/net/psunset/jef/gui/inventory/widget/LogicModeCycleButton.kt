package net.psunset.jef.gui.inventory.widget

import net.minecraft.client.gui.GuiGraphics
import net.minecraft.client.gui.components.Tooltip
import net.minecraft.network.chat.CommonComponents
import net.psunset.jef.builtin.FilterManager
import net.psunset.jef.builtin.LogicMode
import net.psunset.jef.gui.widget.AbstractLeftRightClickButton

class LogicModeCycleButton(
    x: Int,
    y: Int,
    width: Int,
    height: Int
) : AbstractLeftRightClickButton(x, y, width, height, CommonComponents.EMPTY) {

    init {
        tooltip = Tooltip.create(LogicMode.genTooltip())
    }

    override fun onPress() {
        FilterManager.stepLogicMode()
        tooltip = Tooltip.create(LogicMode.genTooltip())
    }

    override fun onRightPress() {
        FilterManager.reverseLogicMode()
        tooltip = Tooltip.create(LogicMode.genTooltip())
    }

    override fun renderWidget(guiGraphics: GuiGraphics, mouseX: Int, mouseY: Int, partialTick: Float) {
        guiGraphics.fill(x, y, x + width, y + height, 0xFF666666.toInt())

        guiGraphics.blit(
            FilterManager.logicMode.icon,
            x,
            y,
            width,
            height,
            0.0f,
            0.0f,
            32,
            32,
            32,
            32
        )
    }
}
