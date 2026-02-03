package net.psunset.jef.gui.widget

import net.minecraft.ChatFormatting
import net.minecraft.client.Minecraft
import net.minecraft.client.gui.GuiGraphics
import net.minecraft.client.gui.components.Button
import net.minecraft.network.chat.Component
import net.psunset.jef.core.FilterManager

class ClearButton(
    x: Int,
    y: Int,
    width: Int,
    height: Int
) : Button(x, y, width, height, Component.empty(), {
    FilterManager.clearFilters()
}, DEFAULT_NARRATION) {

    override fun renderWidget(guiGraphics: GuiGraphics, mouseX: Int, mouseY: Int, partialTick: Float) {
        // Draw background (Reddish to indicate clear)
        guiGraphics.fill(x, y, x + width, y + height, 0xFFAA0000.toInt())

        // Draw X or icon
        guiGraphics.drawCenteredString(
            Minecraft.getInstance().font,
            "X",
            x + width / 2,
            y + (height - 8) / 2,
            0xFFFFFFFF.toInt()
        )

        if (isHovered) {
            guiGraphics.renderTooltip(
                Minecraft.getInstance().font,
                Component.literal("Clear Filters").withStyle(ChatFormatting.RED),
                mouseX,
                mouseY
            )
        }
    }
}
