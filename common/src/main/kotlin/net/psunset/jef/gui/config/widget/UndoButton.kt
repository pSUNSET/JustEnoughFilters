package net.psunset.jef.gui.config.widget

import net.minecraft.client.gui.GuiGraphics
import net.minecraft.client.gui.components.Button
import net.minecraft.client.gui.components.Tooltip
import net.minecraft.client.renderer.RenderPipelines
import net.minecraft.network.chat.CommonComponents
import net.minecraft.network.chat.Component
import net.psunset.jef.tool.IdUtl

class UndoButton(
    x: Int,
    y: Int,
    onPress: OnPress,
) : Button.Plain(
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
        setTooltip(Tooltip.create(Component.translatable("gui.button.justenoughfilters.undo.tooltip")))
    }

    override fun renderContents(guiGraphics: GuiGraphics, mouseX: Int, mouseY: Int, partialTick: Float) {
        guiGraphics.blit(
            RenderPipelines.GUI_TEXTURED,
            UNDO_SPRITE_LOCATION,
            x + 1,
            y + 1,
            0.0f,
            0.0f,
            width - 2,
            height - 2,
            16,
            16,
            16,
            16
        )
    }

    companion object {
        @JvmStatic
        private val UNDO_SPRITE_LOCATION = IdUtl.ofJef("textures/gui/undo.png")
    }
}