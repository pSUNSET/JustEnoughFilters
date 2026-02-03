package net.psunset.jef.core

import net.minecraft.world.item.ItemStack

object FilterManager {
    val allToggledFilters: Set<ToggledFilter>
        get() = JefRegistries.TOGGLED_FILTERS.values.toSet()

    private val _activeToggledFilters = mutableSetOf<ToggledFilter>()
    val activeToggledFilters: Set<ToggledFilter>
        get() = _activeToggledFilters

    private var logicModeIdx = 0
    val logicMode: LogicMode
        get() = LogicMode.entries[logicModeIdx]

    private var itemTypeFilterIdx = 0
    val itemTypeFilter: ItemTypeFilter
        get() = JefRegistries.ITEM_TYPE_FILTERS[itemTypeFilterIdx].second

    fun isFilterActive(filter: ToggledFilter): Boolean {
        return _activeToggledFilters.contains(filter)
    }

    fun areAllFiltersDisabled(): Boolean {
        return _activeToggledFilters.isEmpty() && itemTypeFilter == ItemTypeFilters.OFF
    }

    internal fun toggleFilter(filter: ToggledFilter) {
        if (!_activeToggledFilters.remove(filter)) {
            _activeToggledFilters.add(filter)
        }
        refreshProxies()
    }

    internal fun clearFilters() {
        _activeToggledFilters.clear()
        itemTypeFilterIdx = 0
        refreshProxies()
    }

    fun refreshProxies() {
        JefRegistries.PROXIES.forEach { it.`jef$refresh`() }
    }

    internal fun stepLogicMode() {
        logicModeIdx++
        if (logicModeIdx >= LogicMode.entries.size) {
            logicModeIdx = 0
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

    fun test(stack: ItemStack): Boolean {
        if (areAllFiltersDisabled()) return true
        if (!itemTypeFilter.matches(stack)) return false
        if (_activeToggledFilters.isEmpty()) return true

        val results = _activeToggledFilters.map { it.matches(stack) }
        return logicMode.combineFactory.invoke(results)
    }

    /**
     * obj must be not an [ItemStack]
     */
    fun testNonItem(obj: Any): Boolean {
        if (areAllFiltersDisabled()) return true
        if (!itemTypeFilter.matchesNonItem(obj)) return false
        if (_activeToggledFilters.isEmpty()) return true

        val results = _activeToggledFilters.map { it.matchesNonItem(obj) }
        return logicMode.combineFactory.invoke(results)
    }
}
