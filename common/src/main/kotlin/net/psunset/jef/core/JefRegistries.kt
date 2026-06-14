package net.psunset.jef.core

import net.psunset.jef.api.IFilterProxy
import net.psunset.jef.api.IItemTypeFilter
import net.psunset.jef.api.IToggledFilter
import net.psunset.jef.config.ConfigManager

object JefRegistries {
    /**
     * Only save the ones written in the code.
     * Custom ones by configuration should be saved into [ConfigManager.customFilters].
     */
    @JvmField
    internal val TOGGLED_FILTERS = mutableMapOf<String, IToggledFilter>()

    private var deferredToggledFilters: MutableList<() -> IToggledFilter>? = mutableListOf()

    @JvmField
    internal val ITEM_TYPE_FILTERS = mutableListOf<Pair<String, IItemTypeFilter>>()

    private var deferredItemTypeFilters: MutableList<() -> IItemTypeFilter>? = mutableListOf()

    @JvmField
    internal val PROXIES = mutableListOf<IFilterProxy>()

    @JvmStatic
    fun registerToggledFilter(filter: IToggledFilter) {
        if (TOGGLED_FILTERS.containsKey(filter.id.toString())) {
            throw IllegalStateException("ToggledFilter with id ${filter.id} is already registered.")
        }
        TOGGLED_FILTERS[filter.id.toString()] = filter
    }

    @JvmStatic
    fun registerDeferredToggledFilter(supplier: () -> IToggledFilter) {
        if (deferredToggledFilters == null) {
            throw IllegalStateException("Registry is already closed.")
        }
        deferredToggledFilters!!.add(supplier)
    }

    @JvmStatic
    internal fun registerItemTypeFilter(index: Int, filter: IItemTypeFilter) {
        ITEM_TYPE_FILTERS.add(index, filter.id.toString() to filter)
    }

    @JvmStatic
    fun registerItemTypeFilter(filter: IItemTypeFilter) {
        if (ITEM_TYPE_FILTERS.any { it.first == (filter.id.toString()) } ) {
            throw IllegalStateException("ItemTypeFilter with id ${filter.id} is already registered.")
        }
        ITEM_TYPE_FILTERS.add(filter.id.toString() to filter)
    }

    @JvmStatic
    fun registerDeferredItemTypeFilter(supplier: () -> IItemTypeFilter) {
        if (deferredItemTypeFilters == null) {
            throw IllegalStateException("Registry is already closed.")
        }
        deferredItemTypeFilters!!.add(supplier)
    }

    @JvmStatic
    fun registerProxy(proxy: IFilterProxy) {
        PROXIES.add(proxy)
    }

    @JvmStatic
    internal fun registerDeferredElements() {
        deferredToggledFilters!!.forEach { registerToggledFilter(it()) }
        deferredItemTypeFilters!!.forEach { registerItemTypeFilter(it()) }
        deferredToggledFilters = null
        deferredItemTypeFilters = null
    }
}