package net.psunset.jef.gui.config

import net.minecraft.client.gui.GuiGraphics
import net.minecraft.client.gui.components.AbstractWidget
import net.minecraft.client.gui.components.Button
import net.minecraft.client.gui.components.StringWidget
import net.minecraft.client.gui.components.Tooltip
import net.minecraft.client.gui.screens.Screen
import net.minecraft.network.chat.CommonComponents
import net.minecraft.network.chat.Component
import net.psunset.jef.util.JefConstants

/**
 * Main configuration screen for JustEnoughFilters mod.
 */
class JefMainConfigScreen(
    lastScreen: Screen?,
) : AbstractJefOptionScreen(lastScreen, Component.translatable("gui.justenoughfilters.config.title")) {

    init {
        undoBtn.visible = false
    }

    private val customFilterConfigBtn = Button.builder(Component.translatable("gui.button.justenoughfilters.config.custom_filters")) {
        minecraft?.setScreen(CustomFilterListScreen(this))
    }
        .tooltip(Tooltip.create(Component.translatable("gui.button.justenoughfilters.config.custom_filters.tooltip")))
        .width(JefConstants.BIG_BUTTON_WIDTH)
        .build()

    private val activeFilterConfigBtn = Button.builder(Component.translatable("gui.button.justenoughfilters.config.active_filters")) {
        minecraft?.setScreen(ActiveFilterListScreen(this))
    }
        .tooltip(Tooltip.create(Component.translatable("gui.button.justenoughfilters.config.active_filters.tooltip")))
        .width(JefConstants.BIG_BUTTON_WIDTH)
        .build()

    override fun repositionElements() {
        rebuildWidgets()
    }

    override fun addContents() {
        val contents = listOf(
            createString("gui.justenoughfilters.config.general_options"),
            activeFilterConfigBtn,
            createString(CommonComponents.EMPTY),
            createString("gui.justenoughfilters.config.advanced_options"),
            customFilterConfigBtn
        )

        val contentsHeight = contents.sumOf { it.height } + 16
        var _y = (height - contentsHeight) / 2
        for (widget in contents) {
            widget.setPosition((width - widget.width) / 2, _y)
            addRenderableWidget(widget)
            _y += widget.height + 4
        }
    }

    private fun createString(message: Component): StringWidget {
        return StringWidget(
            JefConstants.BIG_BUTTON_WIDTH,
            8,
            message,
            minecraft!!.font
        ).apply {
            alignCenter()
        }
    }

    private fun createString(key: String): StringWidget {
        return createString(Component.translatable(key))
    }

    override fun render(guiGraphics: GuiGraphics, mouseX: Int, mouseY: Int, partialTick: Float) {
        this.renderBackground(guiGraphics)
        guiGraphics.drawCenteredString(this.font, this.title, this.width / 2, 20, 16777215)
        super.render(guiGraphics, mouseX, mouseY, partialTick)
    }
}