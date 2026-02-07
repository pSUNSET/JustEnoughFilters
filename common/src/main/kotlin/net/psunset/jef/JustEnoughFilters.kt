package net.psunset.jef

import net.psunset.jef.compat.rei.ReiFilterProxyImpl
import net.psunset.jef.core.ItemTypeFilters
import net.psunset.jef.core.JefRegistries
import net.psunset.jef.core.ToggledFilters
import net.psunset.jef.tool.CompatUtl
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger

object JustEnoughFilters {
    const val ID = "justenoughfilters"
    const val NAME = "JEF"

    @JvmField
    val LOGGER: Logger = LogManager.getLogger(NAME)

    @JvmStatic
    fun shouldLoad(): Boolean {
        return CompatUtl.JEI.isLoaded() || CompatUtl.REI.isLoaded()
    }

    /**
     * @return should this mod be loaded
     */
    @JvmStatic
    fun init(): Boolean {
        return if (shouldLoad()) {

            LOGGER.info("$NAME initializing...")

            ToggledFilters.init()
            ItemTypeFilters.init()

            if (CompatUtl.REI.isLoaded()) {
                JefRegistries.registerProxy(ReiFilterProxyImpl)
            }

            true
        } else {
            false
        }
    }
}
