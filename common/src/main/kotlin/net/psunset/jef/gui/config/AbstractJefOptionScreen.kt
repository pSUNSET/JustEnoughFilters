package net.psunset.jef.gui.config

import net.minecraft.client.gui.components.Button
import net.minecraft.client.gui.screens.OptionsSubScreen
import net.minecraft.client.gui.screens.Screen
import net.minecraft.network.chat.CommonComponents
import net.minecraft.network.chat.Component
import net.psunset.jef.gui.config.widget.UndoButton
import net.psunset.jef.util.JefConstants

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
        .width(JefConstants.BIG_BUTTON_WIDTH)
        .build()

    protected val undoBtn = UndoButton { onUndo() }

    override fun init() {
        addContents()
        doneBtn.setPosition((width - doneBtn.width) / 2, height - 27)
        addRenderableWidget(doneBtn)
        undoBtn.setPosition(width - 27, height - 27)
        addRenderableWidget(undoBtn)
    }

    override fun repositionElements() {
        doneBtn.setPosition((width - doneBtn.width) / 2, height - 27)
        undoBtn.setPosition(width - 27, height - 27)
    }

    abstract fun addContents()

    open fun onDone() {
        this.onClose()
    }

    open fun onUndo() {
    }

    override fun onClose() {
        minecraft?.setScreen(lastScreen)
    }

    override fun removed() {
    }
}