package net.psunset.jef.gui.inventory

import net.minecraft.client.gui.GuiGraphicsExtractor
import net.minecraft.client.gui.screens.Screen
import net.minecraft.client.input.MouseButtonEvent
import net.minecraft.client.renderer.Rect2i
import net.psunset.jef.builtin.FilterManager
import net.psunset.jef.tool.valEq

object InventoryOverlayManager {
    private val overlay = FilterBarOverlay()
    private var lastScreenWidth = -1
    private var lastScreenHeight = -1

    /**
     * Includes margin padding space!
     */
    var overlayBounds: Rect2i? = null
        private set
    private var lastOverlayBounds: Rect2i? = null

    /**
     * Type + Logic + ...Toggled + Clear
     */
    fun allButtonsCount(): Int {
        return FilterManager.activeToggledFilters.size + 3
    }

    fun updateBounds(rect2i: Rect2i) {
        overlayBounds = rect2i
    }

    fun updateBounds(x: Int, y: Int, width: Int, height: Int) {
        overlayBounds = Rect2i(x, y, width, height)
    }

    fun drawFilterBar(
        screen: Screen,
        graphics: GuiGraphicsExtractor,
        mouseX: Int,
        mouseY: Int
    ) {
        if (overlayBounds == null) return  // No bounds known yet

        if (screen.width != lastScreenWidth || screen.height != lastScreenHeight || !(overlayBounds!! valEq lastOverlayBounds)) {
            lastScreenWidth = screen.width
            lastScreenHeight = screen.height

            overlay.init(overlayBounds!!.x, overlayBounds!!.y, overlayBounds!!.width)
        }
        lastOverlayBounds = overlayBounds
        overlay.extractRenderState(graphics, mouseX, mouseY, 0f)
    }

    fun refresh() {
        lastScreenWidth = 0  // Force reinit on next frame
    }

    fun mouseClicked(event: MouseButtonEvent, isDoubleClick: Boolean): Boolean {
        return overlay.mouseClicked(event, isDoubleClick)
    }
}