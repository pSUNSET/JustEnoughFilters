package net.psunset.jef.gui.config

import net.minecraft.client.gui.GuiGraphics
import net.minecraft.client.gui.screens.Screen
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.network.chat.Component
import net.psunset.jef.config.ConfigManager
import net.psunset.jef.config.element.CustomFilter
import net.psunset.jef.gui.config.widget.CustomFilterConfigWidget
import net.psunset.jef.gui.config.widget.CustomFilterListWidget
import net.psunset.jef.tool.RLUtl
import net.psunset.jef.tool.idToString

class CustomFilterConfigScreen internal constructor(
    private val i: Int,
    private val filter: CustomFilter,
    lastScreen: Screen?,
    @Suppress("PROPERTY_HIDES_JAVA_FIELD")
    private val list: CustomFilterListWidget,
) : AbstractJefOptionScreen(
    lastScreen,
    Component.translatable("gui.justenoughfilters.config.custom_filter.title", filter.name)
) {

    private var widget: CustomFilterConfigWidget? = null

    override fun repositionElements() {
        super.repositionElements()
        widget?.updateSize(width, height, 32, height - 32)
    }

    override fun addContents() {
        widget = addWidget(CustomFilterConfigWidget(minecraft!!, width, this, filter))
    }

    fun updateTitle(newName: String) {
        TODO()
    }

    override fun render(guiGraphics: GuiGraphics, mouseX: Int, mouseY: Int, partialTick: Float) {
        this.renderBackground(guiGraphics)
        if (widget != null) widget!!.render(guiGraphics, mouseX, mouseY, partialTick)
        guiGraphics.drawCenteredString(this.font, this.title, this.width / 2, 20, 16777215)
        super.render(guiGraphics, mouseX, mouseY, partialTick)
    }

    override fun removed() {
        list.tempFilters[i] = CustomFilter(
            widget!!.tempName,
            widget!!.tempIcon,
            widget!!.tempOps
        )
        list.refresh()

        if (RLUtl.toValidPath(widget!!.tempName) != RLUtl.toValidPath(filter.name)) {
            val filtersInFile = ConfigManager.readActiveFilters().toMutableList()
            val idx = filtersInFile.indexOf(filter.id.toString())
            if (idx != -1) {
                ConfigManager.saveActiveFilters(
                    filtersInFile.also {
                        it[idx] = CustomFilter.genRL(widget!!.tempName).toString()
                    }
                )
            }
        }
    }

    override fun onUndo() {
        if (widget != null) {
            widget!!.tempName = filter.name
            widget!!.tempIcon = filter.icon.idToString()
            widget!!.tempOps = filter.ops.toMutableList()
            widget!!.refresh()
        }
    }
}