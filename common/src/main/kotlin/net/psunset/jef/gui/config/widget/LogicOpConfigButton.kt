package net.psunset.jef.gui.config.widget

import net.minecraft.client.gui.GuiGraphics
import net.minecraft.client.gui.components.Button
import net.minecraft.client.gui.components.Tooltip
import net.minecraft.client.gui.narration.NarrationElementOutput
import net.minecraft.network.chat.CommonComponents
import net.psunset.jef.config.LogicOp
import net.psunset.jef.gui.widget.AbstractLeftRightClickButton

sealed class LogicOpConfigButton(
    x: Int,
    y: Int
) : AbstractLeftRightClickButton(
    x,
    y,
    Button.DEFAULT_HEIGHT,
    Button.DEFAULT_HEIGHT,
    CommonComponents.EMPTY
) {

    abstract override fun onPress()

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
            tooltip = Tooltip.create(LogicOp.Binary.genTooltip(op))
            active = !isFirst
        }

        override fun onPress() {
            op = if (op == LogicOp.Binary.and) LogicOp.Binary.or else LogicOp.Binary.and
            tooltip = Tooltip.create(LogicOp.Binary.genTooltip(op))
            responder(op)
        }

        override fun renderWidget(guiGraphics: GuiGraphics, mouseX: Int, mouseY: Int, partialTick: Float) {
            super.renderWidget(guiGraphics, mouseX, mouseY, partialTick)
            if (op.sprite != null) {
                guiGraphics.blit(
                    op.sprite!!,
                    x + 1,
                    y + 1,
                    width - 2,
                    height - 2,
                    0.0f,
                    0.0f,
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
            tooltip = Tooltip.create(LogicOp.Unary.genTooltip(op))
        }

        override fun onPress() {
            op = if (op == LogicOp.Unary.so) LogicOp.Unary.not else LogicOp.Unary.so
            tooltip = Tooltip.create(LogicOp.Unary.genTooltip(op))
            responder(op)
        }

        override fun renderWidget(guiGraphics: GuiGraphics, mouseX: Int, mouseY: Int, partialTick: Float) {
            super.renderWidget(guiGraphics, mouseX, mouseY, partialTick)
            if (op.sprite != null) {
                guiGraphics.blit(
                    op.sprite!!,
                    x + 1,
                    y + 1,
                    width - 2,
                    height - 2,
                    0.0f,
                    0.0f,
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