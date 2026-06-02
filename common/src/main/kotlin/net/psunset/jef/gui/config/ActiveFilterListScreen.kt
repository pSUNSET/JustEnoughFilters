package net.psunset.jef.gui.config

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
        list?.updateSize(width, layout)
    }

    override fun addContents() {
        list = layout.addToContents(ActiveFilterListWidget(minecraft!!, width, this))
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