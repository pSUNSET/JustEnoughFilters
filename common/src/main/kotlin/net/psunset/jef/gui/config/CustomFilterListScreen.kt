package net.psunset.jef.gui.config

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
        list?.updateSize(width, layout)
    }

    override fun addContents() {
        list = layout.addToContents(CustomFilterListWidget(minecraft!!, width, this))
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