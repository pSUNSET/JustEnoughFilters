package net.psunset.jef

import net.psunset.jef.compat.emi.EmiFilterProxyImpl
import net.psunset.jef.compat.rei.ReiFilterProxyImpl
import net.psunset.jef.core.BuiltinToggledFilters
import net.psunset.jef.core.FilterManager
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
        return CompatUtl.JEI.isLoaded() || CompatUtl.REI.isLoaded() || CompatUtl.EMI.isLoaded()
    }

    /**
     * @return should this mod be loaded
     */
    @JvmStatic
    fun init(): Boolean {
        return if (shouldLoad()) {

            LOGGER.info("$NAME initializing...")

            BuiltinToggledFilters.init()

            if (CompatUtl.EMI.isLoaded()) {
                FilterManager.registerProxy(EmiFilterProxyImpl)
            }

            if (CompatUtl.REI.isLoaded()) {
                FilterManager.registerProxy(ReiFilterProxyImpl)
            }

            true
        } else {
            false
        }
    }
}
