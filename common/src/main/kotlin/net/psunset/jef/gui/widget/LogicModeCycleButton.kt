package net.psunset.jef.gui.widget

import net.minecraft.client.Minecraft
import net.minecraft.client.gui.GuiGraphics
import net.minecraft.client.gui.components.Button
import net.minecraft.network.chat.Component
import net.psunset.jef.core.FilterManager
import net.psunset.jef.core.LogicMode

class LogicModeCycleButton(
    x: Int,
    y: Int,
    width: Int,
    height: Int
) : Button(
    x,
    y,
    width,
    height,
    Component.empty(),
    { FilterManager.stepLogicMode() },
    DEFAULT_NARRATION
) {

    override fun renderWidget(guiGraphics: GuiGraphics, mouseX: Int, mouseY: Int, partialTick: Float) {
        // Draw background
        guiGraphics.fill(x, y, x + width, y + height, 0xFF666666.toInt())

        // Draw text or icon representing current mode
        val mode = FilterManager.logicMode

        guiGraphics.drawCenteredString(
            Minecraft.getInstance().font,
            mode.icon,
            x + width / 2,
            y + (height - 8) / 2,
            0xFFFFFFFF.toInt()
        )

        if (isHovered) {
            guiGraphics.renderComponentTooltip(Minecraft.getInstance().font, LogicMode.genTooltip(mode), mouseX, mouseY)
        }
    }
}
