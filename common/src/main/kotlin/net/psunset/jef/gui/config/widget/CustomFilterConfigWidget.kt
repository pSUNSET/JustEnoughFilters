package net.psunset.jef.gui.config.widget

import net.minecraft.client.Minecraft
import net.minecraft.client.gui.GuiGraphics
import net.minecraft.client.gui.components.AbstractWidget
import net.minecraft.client.gui.components.Button
import net.minecraft.client.gui.components.ContainerObjectSelectionList
import net.minecraft.client.gui.components.EditBox
import net.minecraft.client.gui.components.events.GuiEventListener
import net.minecraft.client.gui.narration.NarratableEntry
import net.minecraft.network.chat.CommonComponents
import net.minecraft.network.chat.Component
import net.psunset.jef.config.element.*
import net.psunset.jef.gui.config.CustomFilterConfigScreen
import net.psunset.jef.util.JefConstants

internal class CustomFilterConfigWidget(
    minecraft: Minecraft,
    width: Int,
    private val screen: CustomFilterConfigScreen,
    filter: CustomFilter,
) : ContainerObjectSelectionList<CustomFilterConfigWidget.Entry>(
    minecraft, width, screen.layout.contentHeight, screen.layout.headerHeight, 25
) {

    internal var tempName = filter.name
    internal var tempIcon = filter.icon.toString()
    internal var tempOps = filter.ops.toMutableList()

    private var suggestionsList: MutableList<DropDownEditBox.Suggestions> = ArrayList(tempOps.size)

    init {
        this.centerListVertically = false

        this.addEntry(FirstLine(this.minecraft, this, screen))

        for (i in tempOps.indices) {
            this.addEntry(OpEntry(i, this.minecraft, tempOps, this, screen))
        }
    }

    override fun getRowWidth(): Int = 512

    override fun mouseClicked(mouseX: Double, mouseY: Double, button: Int): Boolean {
        return suggestionsList.any { it.mouseClicked(mouseX, mouseY, button) } ||
                super.mouseClicked(mouseX, mouseY, button)
    }

    override fun mouseScrolled(mouseX: Double, mouseY: Double, scrollX: Double, scrollY: Double): Boolean {
        return suggestionsList.any { it.mouseScrolled(mouseX, mouseY, scrollX, scrollY) } ||
                super.mouseScrolled(mouseX, mouseY, scrollX, scrollY)
    }

    override fun renderWidget(guiGraphics: GuiGraphics, mouseX: Int, mouseY: Int, partialTick: Float) {
        super.renderWidget(guiGraphics, mouseX, mouseY, partialTick)
    }

    fun refresh() {
        this.clearEntries()
        suggestionsList = ArrayList(tempOps.size)
        this.addEntry(FirstLine(this.minecraft, this, screen))
        for (i in tempOps.indices) {
            this.addEntry(OpEntry(i, this.minecraft, tempOps, this, screen))
        }
    }

    fun addOp(i: Int, op: OpCombination) {
        tempOps.add(i, op)
        this.refresh()
    }

    fun addDefaultOp(i: Int) {
        this.addOp(
            i,
            OpCombination(
                LogicOp.Binary.and,
                LogicOp.Unary.so,
                FilterOpGenerator(FilterOpProvider.name_contains, "")
            )
        )
    }

    fun removeOp(i: Int) {
        tempOps.removeAt(i)
        this.refresh()
    }

    internal abstract class Entry : ContainerObjectSelectionList.Entry<Entry>()

    internal class FirstLine(
        minecraft: Minecraft,
        widget: CustomFilterConfigWidget,
        private val screen: CustomFilterConfigScreen,
    ) : Entry() {

        private val font = minecraft.font
        private val nameHint = Component.translatable("gui.justenoughfilters.config.custom_filter.name.prefix")
        private val iconHint = Component.translatable("gui.justenoughfilters.config.custom_filter.icon.prefix")

        private val nameField: EditBox = EditBox(
            font, Button.DEFAULT_WIDTH, Button.DEFAULT_HEIGHT, CommonComponents.EMPTY
        ).apply {
            setMaxLength(64)
            value = widget.tempName
            setHint(Component.literal("..."))
            setResponder { if (it.isNotBlank()) widget.tempName = it }
        }

        private val iconField: EditBox = EditBox(
            font, Button.DEFAULT_WIDTH, Button.DEFAULT_HEIGHT, CommonComponents.EMPTY
        ).apply {
            setMaxLength(256)
            value = widget.tempIcon
            setHint(Component.literal("..."))
            setResponder {
                if (JefConstants.ITEM_IDS.contains(iconField.value)) {
                    setTextColor(14737632)
                    widget.tempIcon = it
                } else {
                    setTextColor(16733525)
                }
            }
        }

        private val children: List<AbstractWidget> = listOf(nameField, iconField)

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
            val nameHintWidth = font.width(nameHint)
            val iconHintWidth = font.width(iconHint)
            val strY = top + (Button.DEFAULT_HEIGHT - font.lineHeight) / 2
            var x = (screen.width - nameHintWidth - iconHintWidth) / 2 - Button.DEFAULT_WIDTH - Button.DEFAULT_SPACING

            guiGraphics.drawString(font, nameHint, x, strY, 14737632)
            x += font.width(nameHint)

            nameField.setPosition(x, top)
            nameField.render(guiGraphics, mouseX, mouseY, partialTick)
            x += Button.DEFAULT_WIDTH + Button.DEFAULT_SPACING * 2

            guiGraphics.drawString(font, iconHint, x, strY, 14737632)
            x += font.width(iconHint)

            iconField.setPosition(x, top)
            iconField.render(guiGraphics, mouseX, mouseY, partialTick)
        }

        override fun children(): List<GuiEventListener> {
            return this.children
        }

        override fun narratables(): List<NarratableEntry> {
            return this.children
        }
    }

    internal class OpEntry(
        private val i: Int,
        minecraft: Minecraft,
        private val ops: MutableList<OpCombination>,
        widget: CustomFilterConfigWidget,
        private val screen: CustomFilterConfigScreen,
    ) : Entry() {

        private var op: OpCombination = ops[i]
            set(value) {
                ops[i] = value
                field = value
            }

        private val binLogicBtn = LogicOpConfigButton.Binary(op.bin, i == 0) { op = op.copy(bin = it) }

        private val unaryLogicBtn = LogicOpConfigButton.Unary(op.unary) { op = op.copy(unary = it) }

        // wield arrangement but args field must init before filter field
        private val filterArgsField = EditBox(
            minecraft.font, Button.DEFAULT_WIDTH, Button.DEFAULT_HEIGHT, CommonComponents.EMPTY
        ).apply {
            if (op.filter.provider.factory is FilterOpFactory0) visible = false
            setMaxLength(256)
            value = op.filter.input
            setResponder { op = op.copy(filter = op.filter.copy(input = it)) }
        }

        private val filterField = FilterOpConfigField(
            minecraft,
            Button.DEFAULT_WIDTH,
            Button.DEFAULT_HEIGHT,
        ) {
            val provider = FilterOpProvider.valueOf(it)
            val isInputNeeded = provider.factory is FilterOpFactory1
            op = if (isInputNeeded) {
                op.copy(filter = op.filter.copy(provider = provider))
            } else {
                op.copy(filter = FilterOpGenerator(provider, ""))
            }
            filterArgsField.visible = isInputNeeded
        }.apply {
            setMaxLength(64)
            value = op.filter.provider.name
            widget.suggestionsList.add(suggestions)
        }

        private val removeBtn = RemoveButton { widget.removeOp(i) }.apply { active = i != 0 }
        private val addBtn = AddButton { widget.addDefaultOp(i + 1) }

        private val children = listOf(binLogicBtn, unaryLogicBtn, filterField, filterArgsField, removeBtn, addBtn)

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
            var x = (screen.width - children.sumOf { it.width } - Button.DEFAULT_SPACING * children.lastIndex) / 2
            for (child in children) {
                child.setPosition(x, top)
                child.render(guiGraphics, mouseX, mouseY, partialTick)
                x += child.width + Button.DEFAULT_SPACING
            }
        }

        override fun children(): List<GuiEventListener> {
            return this.children
        }

        override fun narratables(): List<NarratableEntry> {
            return this.children
        }
    }
}