package net.psunset.jef.gui.config.widget

import net.minecraft.client.Minecraft
import net.minecraft.client.gui.GuiGraphicsExtractor
import net.minecraft.client.gui.components.*
import net.minecraft.client.gui.components.events.GuiEventListener
import net.minecraft.client.gui.narration.NarratableEntry
import net.minecraft.client.input.MouseButtonEvent
import net.minecraft.network.chat.CommonComponents
import net.minecraft.network.chat.Component
import net.psunset.jef.config.*
import net.psunset.jef.gui.config.CustomFilterConfigScreen

internal class CustomFilterConfigWidget(
    minecraft: Minecraft,
    width: Int,
    private val screen: CustomFilterConfigScreen,
    filter: CustomFilter,
) : ContainerObjectSelectionList<CustomFilterConfigWidget.Entry>(
    minecraft, width, screen.layout.contentHeight, screen.layout.headerHeight, 25
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

    override fun mouseClicked(event: MouseButtonEvent, isDoubleClick: Boolean): Boolean {
        return suggestionsList.any { it.mouseClicked(event, isDoubleClick) } ||
                super.mouseClicked(event, isDoubleClick)
    }

    override fun mouseDragged(event: MouseButtonEvent, mouseX: Double, mouseY: Double): Boolean {
        return suggestionsList.any { it.mouseDragged(event, mouseX, mouseY) } ||
                super.mouseDragged(event, mouseX, mouseY)
    }

    override fun mouseScrolled(mouseX: Double, mouseY: Double, scrollX: Double, scrollY: Double): Boolean {
        return suggestionsList.any { it.mouseScrolled(mouseX, mouseY, scrollX, scrollY) } ||
                super.mouseScrolled(mouseX, mouseY, scrollX, scrollY)
    }

    override fun extractWidgetRenderState(graphics: GuiGraphicsExtractor, mouseX: Int, mouseY: Int, a: Float) {
        // If any suggestions shown, hide all FilterOpConfigField's tooltip
        if (suggestionsList.any { it.visible }) {
            suggestionsList.forEach { it.parent.tooltipVisible = false }
        } else {
            suggestionsList.forEach { it.parent.tooltipVisible = true }
        }

        super.extractWidgetRenderState(graphics, mouseX, mouseY, a)
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
            font, Button.DEFAULT_WIDTH, Button.DEFAULT_HEIGHT, CommonComponents.EMPTY
        ).apply {
            setMaxLength(64)
            setHint(Component.literal("Name..."))
            setResponder {
                if (it.isNotBlank()) {
                    widget.tempName = it
                    setTooltip(Tooltip.create(
                        Component.translatable(
                            "gui.justenoughfilters.config.custom_filter.name.tooltip",
                            CustomFilter.genId(it).toString()
                        )
                    ))
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
            setTooltip(Tooltip.create(Component.translatable("gui.justenoughfilters.config.custom_filter.icon.tooltip")))
        }

        private val children: List<AbstractWidget> = listOf(nameField, iconField)

        override fun extractContent(
            graphics: GuiGraphicsExtractor,
            mouseX: Int,
            mouseY: Int,
            hovered: Boolean,
            a: Float
        ) {
            val nameHintWidth = font.width(nameHint)
            val iconHintWidth = font.width(iconHint)
            val strY = contentY + (Button.DEFAULT_HEIGHT - font.lineHeight) / 2
            var x = (screen.width - nameHintWidth - iconHintWidth) / 2 - Button.DEFAULT_WIDTH - Button.DEFAULT_SPACING

            graphics.text(font, nameHint, x, strY, -2039584)
            x += font.width(nameHint)

            nameField.setPosition(x, contentY)
            nameField.extractRenderState(graphics, mouseX, mouseY, a)
            x += Button.DEFAULT_WIDTH + Button.DEFAULT_SPACING * 2

            graphics.text(font, iconHint, x, strY, -2039584)
            x += font.width(iconHint)

            iconField.setPosition(x, contentY)
            iconField.extractRenderState(graphics, mouseX, mouseY, a)
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
                setTooltip(Tooltip.create(Component.literal(argDesc.toString())))
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
                filterArgsField.setTooltip(Tooltip.create(Component.literal(provider.argDesc.toString())))
                op.copy(filter = op.filter.copy(provider = id))
            } else {
                op.copy(filter = FilterOpGenerator(id, ""))
            }
            filterArgsField.visible = isInputNeeded
        }.apply {
            setMaxLength(64)
            val provider = FilterOpProvider.valueOfOrUnknown(op.filter.provider)
            setTooltip(Tooltip.create(provider.tooltip))
            value = provider.name
            widget.suggestionsList.add(i, suggestions)
        }

        private val removeBtn = RemoveButton { widget.removeOp(i) }.apply { active = i != 0 }
        private val addBtn = AddButton { widget.addDefaultOp(i + 1) }

        private val children = listOf(binLogicBtn, unaryLogicBtn, filterField, filterArgsField, removeBtn, addBtn)

        override fun extractContent(
            graphics: GuiGraphicsExtractor,
            mouseX: Int,
            mouseY: Int,
            hovered: Boolean,
            a: Float
        ) {
            var x = (screen.width - children.sumOf { it.width } - Button.DEFAULT_SPACING * children.lastIndex) / 2
            for (child in children) {
                child.setPosition(x, contentY)
                child.extractRenderState(graphics, mouseX, mouseY, a)
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