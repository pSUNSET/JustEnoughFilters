package net.psunset.jef.gui.config.widget

import net.minecraft.client.Minecraft
import net.minecraft.client.gui.Font
import net.minecraft.client.gui.GuiGraphicsExtractor
import net.minecraft.client.gui.components.AbstractWidget
import net.minecraft.client.gui.components.ContainerObjectSelectionList
import net.minecraft.client.gui.components.EditBox
import net.minecraft.client.gui.components.Tooltip
import net.minecraft.client.gui.components.events.GuiEventListener
import net.minecraft.client.gui.narration.NarratableEntry
import net.minecraft.client.input.MouseButtonEvent
import net.minecraft.network.chat.CommonComponents
import net.psunset.jef.api.AbstractWidgetAccessor
import net.psunset.jef.tool.GraphicsUtl
import kotlin.math.min

/**
 * An EditBox showing all selections in a drop-down list.
 * The text color will be red if the current value is not in the selections,
 * and will be normal otherwise.
 *
 * [saveConsumer] only fired when current value is valid.
 */
abstract class DropDownEditBox(
    minecraft: Minecraft,
    x: Int,
    y: Int,
    width: Int,
    height: Int,
    private val saveConsumer: ((String) -> Unit)?,
) : EditBox(
    minecraft.font,
    x,
    y,
    width,
    height,
    CommonComponents.EMPTY
) {
    constructor(minecraft: Minecraft, width: Int, height: Int, saveConsumer: ((String) -> Unit)?) : this(
        minecraft,
        0,
        0,
        width,
        height,
        saveConsumer
    )

    @JvmField
    internal val suggestions = Suggestions(this, minecraft).apply { visible = false }

    var tooltipVisible = true
        set(value) {
            if (field != value) {
                field = value
                if (value) {
                    _setTooltip(oTooltip)
                } else {
                    oTooltip = (this as AbstractWidgetAccessor).tooltipHolder.get()
                    _setTooltip(null)
                }
            }
        }
    private var oTooltip: Tooltip? = null

    init {
        setResponder { newValue ->
            if (selections.any { it.equals(newValue, ignoreCase = true) }) {
                setTextColor(-2039584)
                onSave(newValue)
            } else {
                setTextColor(-43691)
            }
            suggestions.refreshEntries(selections.sorted())
        }
    }

    override fun setX(x: Int) {
        super.setX(x)
        suggestions.x = x
    }

    override fun setY(y: Int) {
        super.setY(y)
        suggestions.y = if (suggestions.isReversed) {
            y - suggestions.height - 3
        } else {
            y + height - 3
        }
    }

    override fun setWidth(width: Int) {
        super.setWidth(width)
        suggestions.width = width
    }

    override fun setSize(width: Int, height: Int) {
        setWidth(width)
        setHeight(height)
    }

    override fun setTooltip(tooltip: Tooltip?) {
        if (tooltipVisible) {
            super.setTooltip(tooltip)
        } else {
            oTooltip = tooltip
        }
    }

    private fun _setTooltip(tooltip: Tooltip?) {
        (this as AbstractWidgetAccessor).tooltipHolder.set(tooltip)
    }

    override fun setFocused(focused: Boolean) {
        super.setFocused(focused)
        suggestions.visible = focused
        if (focused) {
            tooltipVisible = false
            suggestions.refreshEntries(selections.sorted())
            suggestions.safeReversed()
        } else {
            tooltipVisible = true
        }
    }

    override fun extractWidgetRenderState(graphics: GuiGraphicsExtractor, mouseX: Int, mouseY: Int, a: Float) {
        super.extractWidgetRenderState(graphics, mouseX, mouseY, a)
        GraphicsUtl.registerDeferredExtractor {
            suggestions.extractRenderState(graphics, mouseX, mouseY, a)
        }
    }

    open fun onSave(newValue: String) {
        saveConsumer?.invoke(newValue)
    }

    abstract val selections: Collection<String>

    internal class Suggestions(
        val parent: DropDownEditBox,
        minecraft: Minecraft,
    ) : ContainerObjectSelectionList<Suggestion>(
        minecraft,
        parent.width,
        minecraft.font.lineHeight * 10 + 34,
        parent.bottom - 3,
        minecraft.font.lineHeight + 3
    ) {
        var isReversed = false
            internal set

        init {
            x = parent.x
        }

        internal fun reversed() {
            isReversed = !isReversed
            y = if (isReversed) {
                parent.y - height - 3
            } else {
                parent.bottom - 3
            }
        }

        fun safeReversed() {
            if (parent.bottom + minecraft.font.lineHeight * 10 + 31 > minecraft.screen!!.height) {
                if (!isReversed) {
                    reversed()
                }
            } else if (isReversed) {
                reversed()
            }
        }

        override fun extractListBackground(graphics: GuiGraphicsExtractor) {
            graphics.fill(x, y + 4, right, bottom, -22016)
            graphics.fill(x + 1, y + 5, right - 1, bottom - 1, -6250336)
        }

        override fun extractListSeparators(graphics: GuiGraphicsExtractor) {
        }

        override fun enableScissor(graphics: GuiGraphicsExtractor) {
            graphics.enableScissor(x + 1, y + 4, right - 1, bottom - 1)
        }

        override fun scrollBarX(): Int {
            return right + 1
        }

        override fun isMouseOver(mouseX: Double, mouseY: Double): Boolean {
            return isActive && mouseY >= y + 4 && mouseY <= bottom && mouseX >= x && mouseX <= right + 7
        }

        override fun mouseScrolled(mouseX: Double, mouseY: Double, scrollX: Double, scrollY: Double): Boolean {
            if (isActive && isMouseOver(mouseX, mouseY)) {
                return super.mouseScrolled(mouseX, mouseY, scrollX, scrollY)
            }
            return false
        }

        override fun getRowWidth(): Int = width

        fun refreshEntries(selections: List<String>) {
            val involved = parent.value.lowercase()
            replaceEntries(
                selections
                    .filter { it.lowercase().contains(involved) }
                    .take(50)
                    .map { Suggestion(parent, this, it, minecraft.font) }
            )

            setScrollAmount(0.0)
            height = (minecraft.font.lineHeight + 3) * min(children().size, 10) + 4
        }
    }

    internal class Suggestion(
        private val box: DropDownEditBox,
        private val parent: Suggestions,
        private val text: String,
        private val font: Font,
    ) : ContainerObjectSelectionList.Entry<Suggestion>() {

        private val children = listOf<AbstractWidget>()

        override fun extractContent(
            graphics: GuiGraphicsExtractor,
            mouseX: Int,
            mouseY: Int,
            hovered: Boolean,
            a: Float
        ) {
            val _height = font.lineHeight + 3
            val _x = parent.x
            val subText = font.plainSubstrByWidth(text, parent.width - 4)

            if (parent.isReversed) {
                graphics.fill(_x + 1, contentY + 1, parent.right - 1, contentY + _height, -16777216)
                graphics.text(font, subText, _x + 2, contentY + 2, if (hovered) -22016 else -2039584)
            } else {
                graphics.fill(_x + 1, contentY, parent.right - 1, contentY + _height - 1, -16777216)
                graphics.text(font, subText, _x + 2, contentY + 1, if (hovered) -22016 else -2039584)
            }
        }

        override fun mouseClicked(event: MouseButtonEvent, isDoubleClick: Boolean): Boolean {
            if (isMouseOver(event.x, event.y)) {
                box.value = text
                box.setFocused(false)
                return true
            }
            return false
        }

        override fun children(): List<GuiEventListener> {
            return children
        }

        override fun narratables(): List<NarratableEntry> {
            return children
        }
    }
}