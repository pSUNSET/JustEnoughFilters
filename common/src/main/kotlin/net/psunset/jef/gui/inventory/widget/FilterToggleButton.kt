package net.psunset.jef.gui.inventory.widget

import net.minecraft.ChatFormatting
import net.minecraft.client.gui.GuiGraphics
import net.minecraft.client.gui.components.Button
import net.minecraft.client.gui.components.Tooltip
import net.minecraft.client.renderer.RenderPipelines
import net.minecraft.network.chat.CommonComponents
import net.psunset.jef.api.IToggledFilter
import net.psunset.jef.builtin.FilterManager
import net.psunset.jef.tool.IdUtl
import net.psunset.jef.tool.renderScaledItem
import kotlin.math.min

class FilterToggleButton(
    val filter: IToggledFilter,
    x: Int,
    y: Int,
    width: Int,
    height: Int
) : Button.Plain(
    x,
    y,
    width,
    height,
    CommonComponents.EMPTY,
    {
        FilterManager.toggleFilter(filter)
        (it as FilterToggleButton).refreshTooltip()
    },
    DEFAULT_NARRATION
) {

    init {
        refreshTooltip()
    }

    override fun renderContents(guiGraphics: GuiGraphics, mouseX: Int, mouseY: Int, partialTick: Float) {
        val active = FilterManager.isFilterEnabled(filter)

//        val color = (if (active) 0xFF33CC33 else 0xFF444444).toInt()
//        guiGraphics.fill(x, y, x + width, y + height, color)

        val bg = if (active) ACTIVE_BG else INACTIVE_BG

        guiGraphics.blit(
            RenderPipelines.GUI_TEXTURED,
            bg,
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

        val icon = if (active) filter.activeIcon else filter.inactiveIcon

        val size = min(16, min(width, height) - 2)
        val iconX = x + (width - size) / 2
        val iconY = y + (height - size) / 2
        guiGraphics.renderScaledItem(icon, iconX, iconY, size.toFloat())
    }

    internal fun refreshTooltip() {
        setTooltip(
            Tooltip.create(
                filter.tooltip.copy().withStyle(
                    if (FilterManager.isFilterEnabled(filter)) ChatFormatting.AQUA
                    else ChatFormatting.GRAY
                )
            )
        )
    }

    companion object {
        @JvmField
        val ACTIVE_BG = IdUtl.ofJef("textures/gui/filter_button/active_bg.png")

        @JvmField
        val INACTIVE_BG = IdUtl.ofJef("textures/gui/filter_button/inactive_bg.png")
    }
}
