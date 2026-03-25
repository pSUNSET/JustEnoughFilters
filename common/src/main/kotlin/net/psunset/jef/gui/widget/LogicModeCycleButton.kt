package net.psunset.jef.gui.widget

import net.minecraft.client.Minecraft
import net.minecraft.client.gui.GuiGraphics
import net.minecraft.client.gui.components.AbstractButton
import net.minecraft.client.gui.components.Button
import net.minecraft.client.gui.narration.NarrationElementOutput
import net.minecraft.client.input.InputWithModifiers
import net.minecraft.client.input.MouseButtonInfo
import net.minecraft.client.renderer.RenderPipelines
import net.minecraft.network.chat.Component
import net.psunset.jef.core.FilterManager
import net.psunset.jef.core.LogicMode

class LogicModeCycleButton(
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
            FilterManager.reverseLogicMode()

        } else {  // left or keybinds
            FilterManager.stepLogicMode()
        }
    }

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

    /**
     * Vanilla copy: [Button.defaultButtonNarrationText]
     */
    override fun updateWidgetNarration(narrationElementOutput: NarrationElementOutput) {
        this.defaultButtonNarrationText(narrationElementOutput)
    }
}
