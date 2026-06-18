package net.psunset.jef.gui.config.widget

import net.minecraft.client.Minecraft
import net.minecraft.client.gui.GuiGraphics
import net.minecraft.client.gui.components.*
import net.minecraft.client.gui.components.events.GuiEventListener
import net.minecraft.client.gui.narration.NarratableEntry
import net.minecraft.network.chat.CommonComponents
import net.minecraft.network.chat.Component
import net.psunset.jef.config.*
import net.psunset.jef.gui.config.CustomFilterConfigScreen
import net.psunset.jef.util.JefConstants

internal class CustomFilterConfigWidget(
    minecraft: Minecraft,
    width: Int,
    private val screen: CustomFilterConfigScreen,
    filter: CustomFilter,
) : ContainerObjectSelectionList<CustomFilterConfigWidget.Entry>(
    minecraft,
    width,
    screen.height,
    32,
    screen.height - 32,
    25
) {

    internal var tempName = filter.name
    internal var tempIcon = filter.icon
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

    override fun mouseDragged(mouseX: Double, mouseY: Double, button: Int, dragX: Double, dragY: Double): Boolean {
        return suggestionsList.any { it.mouseDragged(mouseX, mouseY, button, dragX, dragY) } ||
                super.mouseDragged(mouseX, mouseY, button, dragX, dragY)
    }

    override fun mouseScrolled(mouseX: Double, mouseY: Double, delta: Double): Boolean {
        return suggestionsList.any { it.mouseScrolled(mouseX, mouseY, delta) } ||
                super.mouseScrolled(mouseX, mouseY, delta)
    }

    override fun render(guiGraphics: GuiGraphics, mouseX: Int, mouseY: Int, partialTick: Float) {
        // If any suggestions shown, hide all FilterOpConfigField's tooltip
        if (suggestionsList.any { it.visible }) {
            suggestionsList.forEach { it.parent.tooltipVisible = false }
        } else {
            suggestionsList.forEach { it.parent.tooltipVisible = true }
        }

        super.render(guiGraphics, mouseX, mouseY, partialTick)
    }

    override fun getScrollbarPosition(): Int {
        return rowRight + 3
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
                FilterOpGenerator("name_contains", "")
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
        private val nameHint = Component.translatable("gui.justenoughfilters.config.custom_filter.name.desc")
        private val iconHint = Component.translatable("gui.justenoughfilters.config.custom_filter.icon.desc")

        private val nameField = EditBox(
            font, 0, 0, Button.DEFAULT_WIDTH, Button.DEFAULT_HEIGHT, CommonComponents.EMPTY
        ).apply {
            setMaxLength(64)
            setHint(Component.literal("Name..."))
            setResponder {
                if (it.isNotBlank()) {
                    widget.tempName = it
                    tooltip = Tooltip.create(
                        Component.translatable(
                            "gui.justenoughfilters.config.custom_filter.name.tooltip",
                            CustomFilter.genRL(it).toString()
                        )
                    )
                }
            }
            value = widget.tempName
        }

        private val iconField = ItemIdField(
            font, Button.DEFAULT_WIDTH, Button.DEFAULT_HEIGHT
        ) {
            widget.tempIcon = it
        }.apply {
            setMaxLength(256)
            value = widget.tempIcon
            tooltip = Tooltip.create(Component.translatable("gui.justenoughfilters.config.custom_filter.icon.tooltip"))
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
            var x = (screen.width - nameHintWidth - iconHintWidth) / 2 - Button.DEFAULT_WIDTH - JefConstants.BUTTON_SPACING

            guiGraphics.drawString(font, nameHint, x, strY, 14737632)
            x += font.width(nameHint)

            nameField.setPosition(x, top)
            nameField.render(guiGraphics, mouseX, mouseY, partialTick)
            x += Button.DEFAULT_WIDTH + JefConstants.BUTTON_SPACING * 2

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
        private val filterArgsField = FilterOpArgsConfigField(
            minecraft.font,
            Button.DEFAULT_WIDTH,
            Button.DEFAULT_HEIGHT,
            { op.filter.provider }
        ) {
            op = op.copy(filter = op.filter.copy(input = it))
        }.apply {
            val argDesc = FilterOpProvider.valueOfOrUnknown(op.filter.provider).argDesc
            if (argDesc == null) {
                visible = false
            } else {
                tooltip = Tooltip.create(Component.literal(argDesc.toString()))
            }
            setMaxLength(256)
            value = op.filter.input
        }

        private val filterField: FilterOpConfigField = FilterOpConfigField(
            minecraft,
            Button.DEFAULT_WIDTH,
            Button.DEFAULT_HEIGHT,
        ) { id, provider ->
            val isInputNeeded = provider.argDesc != null
            op = if (isInputNeeded) {
                filterArgsField.tooltip = Tooltip.create(Component.literal(provider.argDesc.toString()))
                op.copy(filter = op.filter.copy(provider = id))
            } else {
                op.copy(filter = FilterOpGenerator(id, ""))
            }
            filterArgsField.visible = isInputNeeded
        }.apply {
            setMaxLength(64)
            val provider = FilterOpProvider.valueOfOrUnknown(op.filter.provider)
            tooltip = Tooltip.create(provider.tooltip)
            value = provider.name
            widget.suggestionsList.add(i, suggestions)
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
            var x = (screen.width - children.sumOf { it.width } - JefConstants.BUTTON_SPACING * children.lastIndex) / 2
            for (child in children) {
                child.setPosition(x, top)
                child.render(guiGraphics, mouseX, mouseY, partialTick)
                x += child.width + JefConstants.BUTTON_SPACING
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