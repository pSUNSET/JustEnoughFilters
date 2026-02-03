package net.psunset.jef.core

import net.minecraft.world.item.ItemStack
import net.psunset.jef.api.IFilterProxy

object FilterManager {
    private val toggledFilterRegistry = mutableMapOf<String, ToggledFilter>()
    val allToggledFilters: Set<ToggledFilter>
        get() = toggledFilterRegistry.values.toSet()

    private val _activeToggledFilters = mutableSetOf<ToggledFilter>()
    val activeToggledFilters: Set<ToggledFilter>
        get() = _activeToggledFilters

    private var logicModeIdx = 0
    val logicMode: LogicMode
        get() = LogicMode.entries[logicModeIdx]

    private var itemTypeFilterIdx = 0
    val itemTypeFilter: ItemTypeFilter
        get() = ItemTypeFilter.entries[itemTypeFilterIdx]

    private val proxies = mutableListOf<IFilterProxy>()

    fun registerProxy(proxy: IFilterProxy) {
        proxies.add(proxy)
    }

    fun registerToggledFilter(filter: ToggledFilter) {
        if (toggledFilterRegistry.containsKey(filter.id.toString())) {
            throw IllegalArgumentException("Filter with id ${filter.id} already registered")
        }
        toggledFilterRegistry[filter.id.toString()] = filter
    }

    fun isFilterActive(filter: ToggledFilter): Boolean {
        return _activeToggledFilters.contains(filter)
    }

    fun areAllFiltersDisabled(): Boolean {
        return _activeToggledFilters.isEmpty() && itemTypeFilter == ItemTypeFilter.OFF
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
        proxies.forEach { it.`jef$refresh`() }
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
        if (itemTypeFilterIdx >= ItemTypeFilter.entries.size) {
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
