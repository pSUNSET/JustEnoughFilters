package net.psunset.jef.gui.config.widget

import net.minecraft.client.Minecraft
import net.minecraft.client.gui.Font
import net.minecraft.client.gui.GuiGraphics
import net.minecraft.client.gui.components.AbstractWidget
import net.minecraft.client.gui.components.ContainerObjectSelectionList
import net.minecraft.client.gui.components.EditBox
import net.minecraft.client.gui.components.Tooltip
import net.minecraft.client.gui.components.events.GuiEventListener
import net.minecraft.client.gui.narration.NarratableEntry
import net.minecraft.network.chat.CommonComponents
import net.psunset.jef.api.AbstractWidgetAccessor
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
                    oTooltip = tooltip
                    _setTooltip(null)
                }
            }
        }
    private var oTooltip: Tooltip? = null

    init {
        setResponder { newValue ->
            if (selections.any { it.equals(newValue, ignoreCase = true) }) {
                setTextColor(14737632)
                onSave(newValue)
            } else {
                setTextColor(16733525)
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

    override fun renderWidget(guiGraphics: GuiGraphics, mouseX: Int, mouseY: Int, partialTick: Float) {
        super.renderWidget(guiGraphics, mouseX, mouseY, partialTick)
        guiGraphics.pose.pushPose()
        guiGraphics.pose.translate(0f, 0f, 300f)  // Above everything except for tooltip
        suggestions.render(guiGraphics, mouseX, mouseY, partialTick)
        guiGraphics.pose.popPose()
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

        override fun renderListBackground(guiGraphics: GuiGraphics) {
            guiGraphics.fill(x, y + 4, right, bottom, -22016)
            guiGraphics.fill(x + 1, y + 5, right - 1, bottom - 1, -6250336)
        }

        override fun renderListSeparators(guiGraphics: GuiGraphics) {
        }

        override fun enableScissor(guiGraphics: GuiGraphics) {
            guiGraphics.enableScissor(x + 1, y + 4, right - 1, bottom - 1)
        }

        override fun getScrollbarPosition(): Int {
            return right + 1
        }

        override fun isMouseOver(mouseX: Double, mouseY: Double): Boolean {
            return isActive && mouseY >= y + 4 && mouseY <= bottom && mouseX >= x && mouseX <= right + 7
        }

        override fun mouseScrolled(mouseX: Double, mouseY: Double, scrollX: Double, scrollY: Double): Boolean {
            if (isActive && scrollbarVisible() && isMouseOver(mouseX, mouseY)) {
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

            scrollAmount = 0.0
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

        override fun render(
            guiGraphics: GuiGraphics,
            index: Int,
            top: Int,
            left: Int,
            width: Int,
            height: Int,
            mouseX: Int,
            mouseY: Int,
            hovering: Boolean,
            partialTick: Float
        ) {
            val _height = font.lineHeight + 3
            val _x = parent.x
            val subText = font.plainSubstrByWidth(text, parent.width - 4)

            if (parent.isReversed) {
                guiGraphics.fill(_x + 1, top + 1, parent.right - 1, top + _height, -16777216)
                guiGraphics.drawString(font, subText, _x + 2, top + 2, if (hovering) -22016 else 14737632)
            } else {
                guiGraphics.fill(_x + 1, top, parent.right - 1, top + _height - 1, -16777216)
                guiGraphics.drawString(font, subText, _x + 2, top + 1, if (hovering) -22016 else 14737632)
            }
        }

        override fun mouseClicked(mouseX: Double, mouseY: Double, button: Int): Boolean {
            if (isMouseOver(mouseX, mouseY)) {
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