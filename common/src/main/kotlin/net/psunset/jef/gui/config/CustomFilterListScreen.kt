package net.psunset.jef.gui.config

import net.minecraft.client.gui.GuiGraphics
import net.minecraft.client.gui.screens.Screen
import net.minecraft.network.chat.Component
import net.psunset.jef.config.ConfigManager
import net.psunset.jef.gui.config.widget.CustomFilterListWidget

class CustomFilterListScreen(
    lastScreen: Screen?,
) : AbstractJefOptionScreen(lastScreen, Component.translatable("gui.justenoughfilters.config.custom_filters.title")) {

    @Suppress("PROPERTY_HIDES_JAVA_FIELD")
    private var list: CustomFilterListWidget? = null

    /**
     * If the screen is removed because enter a sub screen.
     * When it is true, changes should not be saved
     */
    var isSubScreenNext = false
        internal set

    override fun init() {
        super.init()
        isSubScreenNext = false
    }

    override fun repositionElements() {
        super.repositionElements()
        list?.updateSize(width, height, 32, height - 32)
        isSubScreenNext = false
    }

    override fun addContents() {
        list = addWidget(CustomFilterListWidget(minecraft!!, width, this))
    }

    override fun render(guiGraphics: GuiGraphics, mouseX: Int, mouseY: Int, partialTick: Float) {
        this.renderBackground(guiGraphics)
        if (list != null) list!!.render(guiGraphics, mouseX, mouseY, partialTick)
        guiGraphics.drawCenteredString(this.font, this.title, this.width / 2, 20, 16777215)
        super.render(guiGraphics, mouseX, mouseY, partialTick)
    }

    override fun removed() {
        if (list != null && !isSubScreenNext) {
            ConfigManager.saveCustomFilters(list!!.tempFilters)
        }
    }

    override fun onUndo() {
        if (list != null) {
            list!!.tempFilters = ConfigManager.customFilters.toMutableList()
            list!!.refresh()
        }
    }
}