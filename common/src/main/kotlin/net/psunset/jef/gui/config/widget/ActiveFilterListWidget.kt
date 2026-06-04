package net.psunset.jef.gui.config.widget

import net.minecraft.client.Minecraft
import net.minecraft.client.gui.GuiGraphics
import net.minecraft.client.gui.components.Button
import net.minecraft.client.gui.components.ContainerObjectSelectionList
import net.minecraft.client.gui.components.Tooltip
import net.minecraft.client.gui.components.events.GuiEventListener
import net.minecraft.client.gui.narration.NarratableEntry
import net.minecraft.network.chat.Component
import net.psunset.jef.core.FilterManager
import net.psunset.jef.gui.config.ActiveFilterListScreen

internal class ActiveFilterListWidget(
    minecraft: Minecraft,
    width: Int,
    private val screen: ActiveFilterListScreen
) : ContainerObjectSelectionList<ActiveFilterListWidget.Entry>(
    minecraft,
    width,
    screen.layout.contentHeight,
    screen.layout.headerHeight,
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

    override fun getRowWidth(): Int = 310

    override fun mouseClicked(mouseX: Double, mouseY: Double, button: Int): Boolean {
        return suggestionsList.any { it.mouseClicked(mouseX, mouseY, button) } ||
                super.mouseClicked(mouseX, mouseY, button)
    }

    override fun mouseScrolled(mouseX: Double, mouseY: Double, scrollX: Double, scrollY: Double): Boolean {
        return suggestionsList.any { it.mouseScrolled(mouseX, mouseY, scrollX, scrollY) } ||
                super.mouseScrolled(mouseX, mouseY, scrollX, scrollY)
    }

    override fun renderWidget(guiGraphics: GuiGraphics, mouseX: Int, mouseY: Int, partialTick: Float) {
        // If any suggestions shown, hide all FilterOpConfigField's tooltip
        if (suggestionsList.any { it.visible }) {
            suggestionsList.forEach { it.parent.tooltipVisible = false }
        } else {
            suggestionsList.forEach { it.parent.tooltipVisible = true }
        }

        super.renderWidget(guiGraphics, mouseX, mouseY, partialTick)
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
            256,
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
            val x = (screen.width - idField.width - Button.DEFAULT_SPACING - removeBtn.width) / 2
            idField.setPosition(x, top)
            idField.render(guiGraphics, mouseX, mouseY, partialTick)
            removeBtn.setPosition(x + idField.width + Button.DEFAULT_SPACING, top)
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
            val x = (screen.width - 256 - Button.DEFAULT_SPACING - addBtn.width) / 2
            addBtn.setPosition(x + 256 + Button.DEFAULT_SPACING, top)
            addBtn.render(guiGraphics, mouseX, mouseY, partialTick)
        }

        override fun children(): List<GuiEventListener> {
            return this.children
        }

        override fun narratables(): List<NarratableEntry> {
            return this.children
        }
    }
}