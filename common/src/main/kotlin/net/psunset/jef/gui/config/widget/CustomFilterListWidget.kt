package net.psunset.jef.gui.config.widget

import net.minecraft.client.Minecraft
import net.minecraft.client.gui.GuiGraphics
import net.minecraft.client.gui.components.Button
import net.minecraft.client.gui.components.ContainerObjectSelectionList
import net.minecraft.client.gui.components.Tooltip
import net.minecraft.client.gui.components.events.GuiEventListener
import net.minecraft.client.gui.narration.NarratableEntry
import net.minecraft.network.chat.Component
import net.psunset.jef.config.ConfigManager
import net.psunset.jef.config.CustomFilter
import net.psunset.jef.gui.config.CustomFilterConfigScreen
import net.psunset.jef.gui.config.CustomFilterListScreen

internal class CustomFilterListWidget(
    minecraft: Minecraft,
    width: Int,
    private val screen: CustomFilterListScreen
) : ContainerObjectSelectionList<CustomFilterListWidget.Entry>(
    minecraft,
    width,
    screen.layout.contentHeight,
    screen.layout.headerHeight,
    25
) {
    var tempFilters: MutableList<CustomFilter> = ConfigManager.customFilters.toMutableList()
        internal set

    init {
        this.centerListVertically = false

        for (i in tempFilters.indices) {
            this.addEntry(FilterEntry(i, tempFilters[i], minecraft, screen, this))
        }

        this.addEntry(LastLine(screen, this))
    }

    override fun getRowWidth(): Int = 310

    fun refresh() {
        this.clearEntries()
        for (i in tempFilters.indices) {
            this.addEntry(FilterEntry(i, tempFilters[i], minecraft, screen, this))
        }
        this.addEntry(LastLine(screen, this))
    }

    private fun add(filter: CustomFilter) {
        tempFilters.add(filter)
        this.refresh()
    }

    private fun addDefault() {
        this.add(CustomFilter.createDefault(tempFilters))
    }

    private fun remove(filter: CustomFilter) {
        tempFilters.remove(filter)
        this.refresh()
    }

    internal abstract class Entry : ContainerObjectSelectionList.Entry<Entry>()

    internal class FilterEntry(
        i: Int,
        filter: CustomFilter,
        minecraft: Minecraft,
        private val screen: CustomFilterListScreen,
        widget: CustomFilterListWidget,
    ) : Entry() {

        private val editBtn = Button.builder(Component.literal(filter.name)) {
            screen.isSubScreenNext = true
            minecraft.setScreen(
                CustomFilterConfigScreen(i, filter, screen, widget)
            )
        }
            .width(Button.SMALL_WIDTH)
            .tooltip(
                Tooltip.create(
                    Component.translatable(
                        "gui.button.justenoughfilters.config.custom_filter.tooltip",
                        filter.id.toString()
                    )
                )
            )
            .build()

        private val removeBtn = RemoveButton { widget.remove(filter) }

        private val children = listOf(editBtn, removeBtn)

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
            val x = (screen.width - editBtn.width - Button.DEFAULT_SPACING - removeBtn.width) / 2
            editBtn.setPosition(x, top)
            editBtn.render(guiGraphics, mouseX, mouseY, partialTick)
            removeBtn.setPosition(x + editBtn.width + Button.DEFAULT_SPACING, top)
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
        private val screen: CustomFilterListScreen,
        widget: CustomFilterListWidget,
    ) : Entry() {

        private val addBtn = AddButton { widget.addDefault() }

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
            val x = (screen.width - Button.SMALL_WIDTH - Button.DEFAULT_SPACING - addBtn.width) / 2
            addBtn.setPosition(x + Button.SMALL_WIDTH + Button.DEFAULT_SPACING, top)
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