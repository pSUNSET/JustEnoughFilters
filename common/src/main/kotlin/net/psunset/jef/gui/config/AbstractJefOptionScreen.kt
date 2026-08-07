package net.psunset.jef.gui.config

import net.minecraft.client.Minecraft
import net.minecraft.client.gui.components.Button
import net.minecraft.client.gui.screens.Screen
import net.minecraft.client.gui.screens.options.OptionsSubScreen
import net.minecraft.network.chat.CommonComponents
import net.minecraft.network.chat.Component
import net.psunset.jef.gui.config.widget.UndoButton

abstract class AbstractJefOptionScreen(
    lastScreen: Screen,
    title: Component
) : OptionsSubScreen(
    lastScreen,
    Minecraft.getInstance().options,
    title
) {

    protected val doneBtn: Button = Button.builder(CommonComponents.GUI_DONE) {
        onDone()
    }
        .width(Button.BIG_WIDTH)
        .build()

    protected val undoBtn = UndoButton { onUndo() }

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
        minecraft.setScreenAndShow(lastScreen)
    }

    override fun removed() {
    }
}