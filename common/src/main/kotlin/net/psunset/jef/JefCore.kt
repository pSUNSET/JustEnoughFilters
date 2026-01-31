package net.psunset.jef

import net.minecraft.client.gui.GuiGraphics
import net.minecraft.client.gui.screens.Screen
import net.minecraft.client.renderer.Rect2i
import net.psunset.jef.core.FilterManager
import net.psunset.jef.gui.FilterBarOverlay

object JefCore {
    private val overlay = FilterBarOverlay()
    private var lastScreenWidth = -1
    private var lastScreenHeight = -1
    private var overlayBounds: Rect2i? = null

    /**
     * Type + Logic + ...Toggled + Clear
     */
    fun allButtonsCount(): Int {
        return FilterManager.allToggledFilters.size + 3
    }

    fun updateBounds(rect2i: Rect2i) {
        overlayBounds = rect2i
    }

    fun updateBounds(x: Int, y: Int, width: Int, height: Int) {
        overlayBounds = Rect2i(x, y, width, height)
    }

    fun drawFilterBar(
        screen: Screen,
        guiGraphics: GuiGraphics,
        mouseX: Int,
        mouseY: Int
    ) {
        if (overlayBounds == null) return  // No bounds known yet

        if (screen.width != lastScreenWidth || screen.height != lastScreenHeight) {
            lastScreenWidth = screen.width
            lastScreenHeight = screen.height

            // bounds is the reserved area from the mixin
            // Just use it directly
            overlay.init(overlayBounds!!.x, overlayBounds!!.y, overlayBounds!!.width)
        }
        overlay.render(guiGraphics, mouseX, mouseY, 0f)
    }

    fun mouseClicked(mouseX: Double, mouseY: Double, button: Int): Boolean {
        return overlay.mouseClicked(mouseX, mouseY, button)
    }
}