package net.psunset.jef.gui.config

import net.minecraft.client.gui.components.Button
import net.minecraft.client.gui.components.SpriteIconButton
import net.minecraft.client.gui.components.Tooltip
import net.minecraft.client.gui.screens.Screen
import net.minecraft.client.gui.screens.options.OptionsSubScreen
import net.minecraft.network.chat.CommonComponents
import net.minecraft.network.chat.Component
import net.psunset.jef.tool.RLUtl

abstract class AbstractJefOptionScreen(
    lastScreen: Screen?,
    title: Component
) : OptionsSubScreen(
    lastScreen,
    null,
    title
) {

    protected val doneBtn: Button = Button.builder(CommonComponents.GUI_DONE) {
        onDone()
    }
        .width(Button.BIG_WIDTH)
        .build()

    protected val undoBtn: SpriteIconButton = SpriteIconButton.builder(
        CommonComponents.EMPTY,
        { onUndo() },
        true
    )
        .width(Button.DEFAULT_HEIGHT)
        .sprite(UNDO_ICON_LOCATION, DEFAULT_SPRITE_SIZE, DEFAULT_SPRITE_SIZE)
        .build()
        .apply {
            tooltip = Tooltip.create(Component.translatable("gui.button.justenoughfilters.undo.tooltip"))
        }

    abstract override fun addContents()

    override fun addOptions() {
        // completely unreachable
        throw AssertionError()
    }

    override fun addFooter() {
        layout.addToFooter(doneBtn)
        layout.addToFooter(undoBtn) {
            it.alignHorizontallyRight()
            it.paddingRight(5)
        }
    }

    open fun onDone() {
        this.onClose()
    }

    open fun onUndo() {
    }

    override fun repositionElements() {
        layout.arrangeElements()
    }

    override fun onClose() {
        minecraft?.setScreen(lastScreen)
    }

    override fun removed() {
    }

    companion object {
        private const val DEFAULT_SPRITE_SIZE = Button.DEFAULT_HEIGHT - 4

        @JvmStatic
        private val UNDO_ICON_LOCATION = RLUtl.ofJef("gui/undo")
    }
}