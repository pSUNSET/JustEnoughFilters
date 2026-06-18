package net.psunset.jef.builtin

import net.minecraft.world.item.ItemStack
import net.psunset.jef.JustEnoughFilters
import net.psunset.jef.api.IItemTypeFilter
import net.psunset.jef.api.IToggledFilter
import net.psunset.jef.config.ConfigManager
import net.psunset.jef.gui.inventory.InventoryOverlayManager
import net.psunset.jef.registry.JefRegistries

object FilterManager {

    /**
     * All toggled filters including registered ones and custom config ones
     */
    val allToggledFilters: List<IToggledFilter>
        get() = ConfigManager.customFilters + JefRegistries.TOGGLED_FILTERS.values

    val allToggledFilterEntries: Map<String, IToggledFilter>
        get() = ConfigManager.customFilterEntries + JefRegistries.TOGGLED_FILTERS.entries

    /**
     * The filters that are active and available in filter bar
     */
    private val _activeToggledFilters = mutableListOf<IToggledFilter>()
    val activeToggledFilters: List<IToggledFilter>
        get() = _activeToggledFilters

    /**
     * The filters that are on.
     */
    private val _enabledToggledFilters = hashSetOf<IToggledFilter>()
    val enabledToggledFilters: Set<IToggledFilter>
        get() = _enabledToggledFilters

    private var logicModeIdx = 0
    val logicMode: LogicMode
        get() = LogicMode.entries[logicModeIdx]

    private var itemTypeFilterIdx = 0
    val itemTypeFilter: IItemTypeFilter
        get() = JefRegistries.ITEM_TYPE_FILTERS.values[itemTypeFilterIdx]

    fun isFilterEnabled(filter: IToggledFilter): Boolean {
        return _enabledToggledFilters.contains(filter)
    }

    fun areAllFiltersDisabled(): Boolean {
        return _enabledToggledFilters.isEmpty() && itemTypeFilter == ItemTypeFilters.OFF
    }

    fun activateToggledFilters(ids: Iterable<String>) {
        _activeToggledFilters.clear()
        _enabledToggledFilters.clear()
        for (id in ids) {
            if (allToggledFilterEntries.containsKey(id)) {
                _activeToggledFilters.add(allToggledFilterEntries[id]!!)
                continue
            }
            JustEnoughFilters.LOGGER.warn("Filter $id is not registered, skipping activation.")
        }
        InventoryOverlayManager.refresh()
    }

    fun activateToggledFilters(ids: Array<out String>) {
        _activeToggledFilters.clear()
        _enabledToggledFilters.clear()
        for (id in ids) {
            if (allToggledFilterEntries.containsKey(id)) {
                _activeToggledFilters.add(allToggledFilterEntries[id]!!)
                continue
            }
            JustEnoughFilters.LOGGER.warn("Filter $id is not registered, skipping activation.")
        }
        InventoryOverlayManager.refresh()
    }

    internal fun toggleFilter(filter: IToggledFilter) {
        if (!_enabledToggledFilters.remove(filter)) {
            _enabledToggledFilters.add(filter)
        }
        refreshProxies()
    }

    internal fun disableAllFilters() {
        _enabledToggledFilters.clear()
        itemTypeFilterIdx = 0
        refreshProxies()
    }

    fun refreshProxies() {
        JefRegistries.PROXIES.entries.forEach { it.refresh() }
    }

    internal fun stepLogicMode() {
        logicModeIdx++
        if (logicModeIdx >= LogicMode.entries.size) {
            logicModeIdx = 0
        }
        refreshProxies()
    }

    internal fun reverseLogicMode() {
        logicModeIdx--
        if (logicModeIdx < 0) {
            logicModeIdx = LogicMode.entries.lastIndex
        }
        refreshProxies()
    }

    internal fun stepItemTypeFilter() {
        itemTypeFilterIdx++
        if (itemTypeFilterIdx >= JefRegistries.ITEM_TYPE_FILTERS.size) {
            itemTypeFilterIdx = 0
        }
        refreshProxies()
    }

    internal fun reverseItemTypeFilter() {
        itemTypeFilterIdx--
        if (itemTypeFilterIdx < 0) {
            itemTypeFilterIdx = JefRegistries.ITEM_TYPE_FILTERS.size - 1
        }
        refreshProxies()
    }

    fun test(stack: ItemStack): Boolean {
        if (areAllFiltersDisabled()) return true
        if (!itemTypeFilter.matches(stack)) return false
        if (_enabledToggledFilters.isEmpty()) return true

        val results = _enabledToggledFilters.map { it.matches(stack) }
        return logicMode.combineFactory.invoke(results)
    }

    /**
     * obj must be not an [ItemStack]
     */
    fun testNonItem(obj: Any): Boolean {
        if (areAllFiltersDisabled()) return true
        if (!itemTypeFilter.matchesNonItem(obj)) return false
        if (_enabledToggledFilters.isEmpty()) return true

        val results = _enabledToggledFilters.map { it.matchesNonItem(obj) }
        return logicMode.combineFactory.invoke(results)
    }
}
