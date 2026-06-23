package net.psunset.jef

import net.psunset.jef.builtin.ItemTypeFilters
import net.psunset.jef.builtin.ToggledFilters
import net.psunset.jef.compat.emi.EmiFilterProxy
import net.psunset.jef.compat.emi.EmiNonItemHelper
import net.psunset.jef.compat.jei.JeiNonItemHelper
import net.psunset.jef.compat.rei.ReiFilterProxy
import net.psunset.jef.compat.rei.ReiNonItemHelper
import net.psunset.jef.config.ConfigManager
import net.psunset.jef.config.FilterOpProviders
import net.psunset.jef.registry.JefRegistries
import net.psunset.jef.tool.CompatUtl
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger

object JustEnoughFilters {
    const val ID = "justenoughfilters"
    const val NAME = "JEF"

    @JvmField
    val LOGGER: Logger = LogManager.getLogger(NAME)

    /**
     * `null` if the mod has not been init yet;
     * `true` if the mod should be activated;
     * `false` if the mod should be deactivated.
     */
    @JvmStatic
    var isActive: Boolean? = null
        private set

    /**
     * @return should this mod be loaded
     */
    @JvmStatic
    fun preInit(): Boolean {
        val j = CompatUtl.JEI.isLoaded()
        val r = CompatUtl.REI.isLoaded()

        fun status(loaded: Boolean): String = if (loaded) "DETECTED" else "MISSING "

        isActive = j || r

        val result = if (isActive!!) {
            "$NAME Status -> INITIALIZING"
        } else {
            "$NAME Status -> DISABLED"
        }

        val output =
            """
                PreInit Result:
                ------------------------------
                | JEI: ${status(j)}              |
                | REI: ${status(r)}              |
                ------------------------------
                $result
            """.trimIndent()

        for (line in output.lineSequence()) LOGGER.info(line)

        return isActive!!
    }

    /**
     * @return should this mod be loaded
     */
    @JvmStatic
    fun init() {
        ToggledFilters.init()
        ItemTypeFilters.init()
        FilterOpProviders.init()

        if (CompatUtl.REI.isLoaded()) {
            JefRegistries.PROXIES.register(ReiFilterProxy)
            ReiNonItemHelper.init()

        } else if (CompatUtl.JEI.isLoaded()) {
            JeiNonItemHelper.init()

        }
    }

    @JvmStatic
    fun postInit() {
        JefRegistries.closeAll()
        ConfigManager.onLoading()
    }
}
