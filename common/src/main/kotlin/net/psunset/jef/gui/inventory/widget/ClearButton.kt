package net.psunset.jef.gui.inventory.widget

import net.minecraft.ChatFormatting
import net.minecraft.client.gui.GuiGraphics
import net.minecraft.client.gui.components.Button
import net.minecraft.client.gui.components.Tooltip
import net.minecraft.network.chat.CommonComponents
import net.minecraft.network.chat.Component
import net.psunset.jef.core.FilterManager
import net.psunset.jef.tool.RLUtl

class ClearButton(
    x: Int,
    y: Int,
    width: Int,
    height: Int
) : Button(
    x,
    y,
    width,
    height,
    CommonComponents.EMPTY,
    { FilterManager.disableAllFilters() },
    DEFAULT_NARRATION
) {

    init {
        tooltip = Tooltip.create(
            Component.translatable("gui.button.justenoughfilters.clear.tooltip")
                .withStyle(ChatFormatting.RED)
        )
    }

    override fun renderWidget(guiGraphics: GuiGraphics, mouseX: Int, mouseY: Int, partialTick: Float) {
        guiGraphics.fill(x, y, x + width, y + height, 0xFFAA0000.toInt())

        guiGraphics.blit(
            ICON,
            x,
            y,
            width,
            height,
            0.0f,
            0.0f,
            16,
            16,
            16,
            16
        )
    }

    companion object {
        @JvmField
        val ICON = RLUtl.ofJef("textures/gui/trash_can.png")
    }
}
