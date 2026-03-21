package net.psunset.jef.gui.widget

import net.minecraft.ChatFormatting
import net.minecraft.client.Minecraft
import net.minecraft.client.gui.GuiGraphics
import net.minecraft.client.gui.components.Button
import net.minecraft.client.renderer.RenderPipelines
import net.minecraft.network.chat.Component
import net.psunset.jef.core.FilterManager
import net.psunset.jef.tool.RLUtl

class ClearButton(
    x: Int,
    y: Int,
    width: Int,
    height: Int
) : Button(x, y, width, height, Component.empty(), {
    FilterManager.clearFilters()
}, DEFAULT_NARRATION) {

    override fun renderWidget(guiGraphics: GuiGraphics, mouseX: Int, mouseY: Int, partialTick: Float) {
        // Draw background
        guiGraphics.fill(x, y, x + width, y + height, 0xFFAA0000.toInt())

        // Draw Icon
        guiGraphics.blit(
            RenderPipelines.GUI_TEXTURED,
            ICON,
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

        if (isHovered) {
            guiGraphics.setTooltipForNextFrame(
                Minecraft.getInstance().font,
                Component.translatable("gui.button.justenoughfilters.clear_button").withStyle(ChatFormatting.RED),
                mouseX,
                mouseY
            )
        }
    }

    companion object {
        @JvmField
        val ICON = RLUtl.of("textures/gui/trash_can.png")
    }
}
