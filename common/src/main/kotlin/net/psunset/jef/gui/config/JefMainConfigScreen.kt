package net.psunset.jef.gui.config

import net.minecraft.client.gui.components.Button
import net.minecraft.client.gui.components.StringWidget
import net.minecraft.client.gui.components.Tooltip
import net.minecraft.client.gui.layouts.LinearLayout
import net.minecraft.client.gui.screens.Screen
import net.minecraft.network.chat.Component

/**
 * Main configuration screen for JustEnoughFilters mod.
 */
class JefMainConfigScreen(
    lastScreen: Screen,
) : AbstractJefOptionScreen(lastScreen, Component.translatable("gui.justenoughfilters.config.title")) {

    init {
        undoBtn.visible = false
    }

    private val customFilterConfigBtn = Button.builder(Component.translatable("gui.button.justenoughfilters.config.custom_filters")) {
        minecraft.setScreen(CustomFilterListScreen(this))
    }
        .tooltip(Tooltip.create(Component.translatable("gui.button.justenoughfilters.config.custom_filters.tooltip")))
        .width(Button.BIG_WIDTH)
        .build()

    private val activeFilterConfigBtn = Button.builder(Component.translatable("gui.button.justenoughfilters.config.active_filters")) {
        minecraft.setScreen(ActiveFilterListScreen(this))
    }
        .tooltip(Tooltip.create(Component.translatable("gui.button.justenoughfilters.config.active_filters.tooltip")))
        .width(Button.BIG_WIDTH)
        .build()

    override fun addContents() {
        this.layout.addToContents(LinearLayout.vertical().spacing(8).apply {
            addChild(createString("gui.justenoughfilters.config.general_options"))
            addChild(activeFilterConfigBtn)
            addChild(createString(""))
            addChild(createString("gui.justenoughfilters.config.advanced_options"))
            addChild(customFilterConfigBtn)
        })
    }

    private fun createString(message: Component): StringWidget {
        return StringWidget(
            Button.BIG_WIDTH,
            8,
            message,
            minecraft.font
        )
//            .apply {
//                alignCenter()
//            }
    }

    private fun createString(key: String): StringWidget {
        return createString(Component.translatable(key))
    }
}