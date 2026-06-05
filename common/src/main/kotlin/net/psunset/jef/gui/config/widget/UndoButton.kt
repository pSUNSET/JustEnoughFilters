package net.psunset.jef.gui.config.widget

import net.minecraft.client.gui.GuiGraphics
import net.minecraft.client.gui.components.Button
import net.minecraft.client.gui.components.Tooltip
import net.minecraft.network.chat.CommonComponents
import net.minecraft.network.chat.Component
import net.psunset.jef.tool.RLUtl

class UndoButton(
    x: Int,
    y: Int,
    onPress: OnPress,
) : Button(
    x,
    y,
    DEFAULT_HEIGHT,
    DEFAULT_HEIGHT,
    CommonComponents.EMPTY,
    onPress,
    DEFAULT_NARRATION
) {
    constructor(onPress: OnPress) : this(0, 0, onPress)

    init {
        tooltip = Tooltip.create(Component.translatable("gui.button.justenoughfilters.undo.tooltip"))
    }

    override fun renderWidget(guiGraphics: GuiGraphics, mouseX: Int, mouseY: Int, partialTick: Float) {
        super.renderWidget(guiGraphics, mouseX, mouseY, partialTick)
        guiGraphics.blit(
            UNDO_SPRITE_LOCATION,
            x + 1,
            y + 1,
            width - 2,
            height - 2,
            0.0f,
            0.0f,
            16,
            16,
            16,
            16
        )
    }

    fun setHeight(height: Int) {
        this.height = height
    }

    companion object {
        @JvmStatic
        private val UNDO_SPRITE_LOCATION = RLUtl.ofJef("textures/gui/undo.png")
    }
}