package net.psunset.jef.gui.config

import net.minecraft.client.gui.GuiGraphics
import net.minecraft.client.gui.screens.Screen
import net.minecraft.network.chat.Component
import net.psunset.jef.config.ConfigManager
import net.psunset.jef.core.FilterManager
import net.psunset.jef.gui.config.widget.ActiveFilterListWidget

class ActiveFilterListScreen(
    lastScreen: Screen?,
) : AbstractJefOptionScreen(lastScreen, Component.translatable("gui.justenoughfilters.config.active_filters.title")) {

    @Suppress("PROPERTY_HIDES_JAVA_FIELD")
    private var list: ActiveFilterListWidget? = null

    override fun repositionElements() {
        super.repositionElements()
        list?.updateSize(width, height, 32, height - 32)
    }

    override fun addContents() {
        list = addWidget(ActiveFilterListWidget(minecraft!!, width, this))
    }

    override fun render(guiGraphics: GuiGraphics, mouseX: Int, mouseY: Int, partialTick: Float) {
        this.renderBackground(guiGraphics)
        if (list != null) list!!.render(guiGraphics, mouseX, mouseY, partialTick)
        guiGraphics.drawCenteredString(this.font, this.title, this.width / 2, 20, 16777215)
        super.render(guiGraphics, mouseX, mouseY, partialTick)
    }

    override fun removed() {
        if (list != null) {
            ConfigManager.saveActiveFilters(
                list!!.tempFilters.filter { it.isNotBlank() }.toSet()
            )
        }
    }

    override fun onUndo() {
        if (list != null) {
            list!!.tempFilters = FilterManager.activeToggledFilters.map { it.id.toString() }.toMutableList()
            list!!.refresh()
        }
    }
}