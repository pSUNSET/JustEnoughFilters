package net.psunset.jef.gui.config.widget

import net.minecraft.client.Minecraft
import net.minecraft.client.gui.GuiGraphics
import net.minecraft.client.gui.components.Button
import net.minecraft.client.gui.components.ContainerObjectSelectionList
import net.minecraft.client.gui.components.Tooltip
import net.minecraft.client.gui.components.events.GuiEventListener
import net.minecraft.client.gui.narration.NarratableEntry
import net.minecraft.network.chat.Component
import net.psunset.jef.builtin.FilterManager
import net.psunset.jef.gui.config.ActiveFilterListScreen
import net.psunset.jef.util.JefConstants

internal class ActiveFilterListWidget(
    minecraft: Minecraft,
    width: Int,
    private val screen: ActiveFilterListScreen
) : ContainerObjectSelectionList<ActiveFilterListWidget.Entry>(
    minecraft,
    width,
    screen.height,
    32,
    screen.height - 32,
    25
) {

    var tempFilters: MutableList<String> =
        FilterManager.activeToggledFilters.map { it.id.toString() }.toMutableList()
        internal set

    private var suggestionsList: MutableList<DropDownEditBox.Suggestions> = ArrayList(tempFilters.size)

    init {
        this.centerListVertically = false

        for (i in tempFilters.indices) {
            this.addEntry(FilterEntry(i, tempFilters, minecraft, screen, this))
        }

        this.addEntry(LastLine(screen, this))
    }

    override fun getRowWidth(): Int {
        return ROW_WIDTH
    }

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
        suggestionsList = ArrayList(tempFilters.size)
        for (i in tempFilters.indices) {
            this.addEntry(FilterEntry(i, tempFilters, minecraft, screen, this))
        }
        this.addEntry(LastLine(screen, this))
    }

    private fun add(filter: String) {
        tempFilters.add(filter)
        this.refresh()
    }

    private fun remove(filter: String) {
        tempFilters.remove(filter)
        this.refresh()
    }

    internal abstract class Entry : ContainerObjectSelectionList.Entry<Entry>()

    internal class FilterEntry(
        private val i: Int,
        private val ids: MutableList<String>,
        minecraft: Minecraft,
        private val screen: ActiveFilterListScreen,
        widget: ActiveFilterListWidget,
    ) : Entry() {

        private var id: String = ids[i]
            set(value) {
                ids[i] = value
                field = value
            }

        private val idField = FilterIdConfigField(
            minecraft,
            Button.SMALL_WIDTH * 2,
            Button.DEFAULT_HEIGHT,
        ) { id = it }.apply {
            setMaxLength(256)
            value = id
            tooltip = Tooltip.create(Component.translatable("gui.justenoughfilters.config.active_filters.id.tooltip"))
            widget.suggestionsList.add(suggestions)
        }

        private val removeBtn = RemoveButton { widget.remove(id) }

        private val children = listOf(idField, removeBtn)

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
            val x = (screen.width - ROW_WIDTH) / 2
            idField.setPosition(x, top)
            idField.render(guiGraphics, mouseX, mouseY, partialTick)
            removeBtn.setPosition(x + idField.width + JefConstants.BUTTON_SPACING, top)
            removeBtn.render(guiGraphics, mouseX, mouseY, partialTick)
        }

        override fun children(): List<GuiEventListener> {
            return this.children
        }

        override fun narratables(): List<NarratableEntry> {
            return this.children
        }
    }

    internal class LastLine(
        private val screen: ActiveFilterListScreen,
        widget: ActiveFilterListWidget,
    ) : Entry() {

        private val addBtn = AddButton { widget.add("") }

        private val children = listOf(addBtn)

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
            val x = (screen.width - ROW_WIDTH) / 2
            addBtn.setPosition(x + Button.SMALL_WIDTH * 2 + Button.DEFAULT_SPACING, top)
            addBtn.render(guiGraphics, mouseX, mouseY, partialTick)
        }

        override fun children(): List<GuiEventListener> {
            return this.children
        }

        override fun narratables(): List<NarratableEntry> {
            return this.children
        }
    }

    companion object {
        const val ROW_WIDTH = Button.SMALL_WIDTH * 2 + Button.DEFAULT_SPACING + Button.DEFAULT_HEIGHT
    }
}