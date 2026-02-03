package net.psunset.jef.core

import net.psunset.jef.api.IFilterProxy

object JefRegistries {
    @JvmField
    internal val TOGGLED_FILTERS = mutableMapOf<String, ToggledFilter>()

    @JvmField
    internal val ITEM_TYPE_FILTERS = mutableListOf<Pair<String, ItemTypeFilter>>()

    @JvmField
    internal val PROXIES = mutableListOf<IFilterProxy>()

    @JvmStatic
    fun registerToggledFilter(filter: ToggledFilter) {
        if (TOGGLED_FILTERS.containsKey(filter.id.toString())) {
            throw IllegalStateException("ToggledFilter with id ${filter.id} is already registered.")
        }
        TOGGLED_FILTERS[filter.id.toString()] = filter
    }

    internal fun registerItemTypeFilter(index: Int, filter: ItemTypeFilter) {
        ITEM_TYPE_FILTERS.add(index, filter.id.toString() to filter)
    }

    @JvmStatic
    fun registerItemTypeFilter(filter: ItemTypeFilter) {
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