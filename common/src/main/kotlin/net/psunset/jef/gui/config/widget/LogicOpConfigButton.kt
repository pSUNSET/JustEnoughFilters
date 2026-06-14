package net.psunset.jef.gui.config.widget

import net.minecraft.client.gui.GuiGraphicsExtractor
import net.minecraft.client.gui.components.AbstractButton
import net.minecraft.client.gui.components.Button
import net.minecraft.client.gui.components.Tooltip
import net.minecraft.client.gui.narration.NarrationElementOutput
import net.minecraft.client.input.InputWithModifiers
import net.minecraft.client.input.MouseButtonInfo
import net.minecraft.client.renderer.RenderPipelines
import net.minecraft.network.chat.CommonComponents
import net.psunset.jef.config.element.LogicOp

sealed class LogicOpConfigButton(
    x: Int,
    y: Int
) : AbstractButton(
    x,
    y,
    Button.DEFAULT_HEIGHT,
    Button.DEFAULT_HEIGHT,
    CommonComponents.EMPTY
) {

    override fun isValidClickButton(buttonInfo: MouseButtonInfo): Boolean {
        return buttonInfo.button == 0 || buttonInfo.button == 1
    }

    class Binary(
        x: Int,
        y: Int,
        op: LogicOp.Binary,
        isFirst: Boolean,
        private val responder: (LogicOp.Binary) -> Unit,
    ) : LogicOpConfigButton(
        x,
        y
    ) {
        constructor(op: LogicOp.Binary, isFirst: Boolean, responder: (LogicOp.Binary) -> Unit) : this(
            0,
            0,
            op,
            isFirst,
            responder
        )

        var isFirst = isFirst
            private set(value) {
                field = value
                active = !value
            }

        var op: LogicOp.Binary =
            if (isFirst) LogicOp.Binary.first else
                if (op == LogicOp.Binary.first) LogicOp.Binary.and else op
            private set

        init {
            setTooltip(Tooltip.create(LogicOp.Binary.genTooltip(op)))
            active = !isFirst
        }

        override fun onPress(input: InputWithModifiers) {
            op = if (op == LogicOp.Binary.and) LogicOp.Binary.or else LogicOp.Binary.and
            setTooltip(Tooltip.create(LogicOp.Binary.genTooltip(op)))
            responder(op)
        }

        override fun extractContents(graphics: GuiGraphicsExtractor, mouseX: Int, mouseY: Int, a: Float) {
            super.extractDefaultSprite(graphics)
            if (op.sprite != null) {
                graphics.blit(
                    RenderPipelines.GUI_TEXTURED,
                    op.sprite!!,
                    x + 1,
                    y + 1,
                    0.0f,
                    0.0f,
                    width - 2,
                    height - 2,
                    32,
                    32,
                    32,
                    32
                )
            }
        }

        override fun updateWidgetNarration(narrationElementOutput: NarrationElementOutput) {
            this.defaultButtonNarrationText(narrationElementOutput)
        }
    }

    class Unary(
        x: Int,
        y: Int,
        op: LogicOp.Unary,
        private val responder: (LogicOp.Unary) -> Unit
    ) : LogicOpConfigButton(
        x,
        y
    ) {
        constructor(op: LogicOp.Unary, responder: (LogicOp.Unary) -> Unit) : this(0, 0, op, responder)

        var op = op
            private set

        init {
            setTooltip(Tooltip.create(LogicOp.Unary.genTooltip(op)))
        }

        override fun onPress(input: InputWithModifiers) {
            op = if (op == LogicOp.Unary.so) LogicOp.Unary.not else LogicOp.Unary.so
            setTooltip(Tooltip.create(LogicOp.Unary.genTooltip(op)))
            responder(op)
        }

        override fun extractContents(graphics: GuiGraphicsExtractor, mouseX: Int, mouseY: Int, a: Float) {
            super.extractDefaultSprite(graphics)
            if (op.sprite != null) {
                graphics.blit(
                    RenderPipelines.GUI_TEXTURED,
                    op.sprite!!,
                    x + 1,
                    y + 1,
                    0.0f,
                    0.0f,
                    width - 2,
                    height - 2,
                    32,
                    32,
                    32,
                    32
                )
            }
        }

        override fun updateWidgetNarration(narrationElementOutput: NarrationElementOutput) {
            this.defaultButtonNarrationText(narrationElementOutput)
        }
    }
}