package net.psunset.jef.gui.inventory.widget

import net.minecraft.ChatFormatting
import net.minecraft.client.gui.GuiGraphics
import net.minecraft.client.gui.components.Button
import net.minecraft.client.gui.components.Tooltip
import net.minecraft.network.chat.CommonComponents
import net.minecraft.network.chat.Component
import net.psunset.jef.builtin.FilterManager
import net.psunset.jef.tool.RLUtl
import net.psunset.jef.gui.inventory.FilterBarOverlay

class ClearButton(
    overlay: FilterBarOverlay,
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
    {
        FilterManager.disableAllFilters()
        for (btn in overlay.toggledButtons) btn.refreshTooltip()
    },
    DEFAULT_NARRATION
) {

    init {
        tooltip = Tooltip.create(
            Component.translatable("gui.button.justenoughfilters.clear.tooltip")
                .withStyle(ChatFormatting.RED)
        )
    }

    override fun renderContents(guiGraphics: GuiGraphics, mouseX: Int, mouseY: Int, partialTick: Float) {
//        guiGraphics.fill(x, y, x + width, y + height, 0xFFAA0000.toInt())

        guiGraphics.blit(
            RenderPipelines.GUI_TEXTURED,
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
        val ICON = RLUtl.ofJef("textures/gui/clear_button.png")
    }
}
