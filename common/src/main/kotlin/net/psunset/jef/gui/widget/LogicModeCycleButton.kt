package net.psunset.jef.gui.widget

import net.minecraft.client.Minecraft
import net.minecraft.client.gui.GuiGraphics
import net.minecraft.client.gui.components.Button
import net.minecraft.client.renderer.RenderPipelines
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
        val mode = FilterManager.logicMode

        // Draw background
        guiGraphics.fill(x, y, x + width, y + height, 0xFF666666.toInt())

        // Render icon
        guiGraphics.blit(
            RenderPipelines.GUI_TEXTURED,
            mode.icon,
            x,
            y,
            0.0f,
            0.0f,
            width,
            height,
            32,
            32,
            32,
            32
        )

        if (isHovered) {
            guiGraphics.setComponentTooltipForNextFrame(Minecraft.getInstance().font, LogicMode.genTooltip(mode), mouseX, mouseY)
        }
    }
}
