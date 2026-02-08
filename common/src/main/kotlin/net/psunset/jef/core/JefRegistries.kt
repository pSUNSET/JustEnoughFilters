package net.psunset.jef.core

import net.psunset.jef.api.IFilterProxy
import net.psunset.jef.api.IItemTypeFilter
import net.psunset.jef.api.IToggledFilter

object JefRegistries {
    @JvmField
    internal val TOGGLED_FILTERS = mutableMapOf<String, IToggledFilter>()

    @JvmField
    internal val ITEM_TYPE_FILTERS = mutableListOf<Pair<String, IItemTypeFilter>>()

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
    fun registerProxy(proxy: IFilterProxy) {
        PROXIES.add(proxy)
    }
}