package net.psunset.jef.registry

import net.psunset.jef.api.IFilterProxy
import net.psunset.jef.api.IItemTypeFilter
import net.psunset.jef.api.IToggledFilter
import net.psunset.jef.config.ConfigManager
import net.psunset.jef.config.FilterOpProvider

object JefRegistries {
    /**
     * Only save the ones written in the code.
     * Custom ones by configuration should be saved into [ConfigManager.customFilters].
     */
    @JvmField
    val TOGGLED_FILTERS = JefDeferredRegistry<IToggledFilter>()

    @JvmField
    val ITEM_TYPE_FILTERS = JefDeferredRegistry<IItemTypeFilter>()

    @JvmField
    val PROXIES = JefNoKeyRegistry<IFilterProxy>()

    @JvmField
    val FILTER_OP_PROVIDERS = JefRegistry<FilterOpProvider>()

    @JvmStatic
    internal fun closeAll() {
        TOGGLED_FILTERS.close()
        ITEM_TYPE_FILTERS.close()
        FILTER_OP_PROVIDERS.close()
    }
}