package net.psunset.jef

//import net.psunset.jef.compat.rei.ReiFilterProxyImpl
import net.psunset.jef.config.ConfigManager
import net.psunset.jef.core.ItemTypeFilters
import net.psunset.jef.core.ToggledFilters
import net.psunset.jef.tool.CompatUtl
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger

object JustEnoughFilters {
    const val ID = "justenoughfilters"
    const val NAME = "JEF"

    @JvmField
    val LOGGER: Logger = LogManager.getLogger(NAME)

    var isActive: Boolean? = null
        private set

    /**
     * @return should this mod be loaded
     */
    @JvmStatic
    internal fun preInit(): Boolean {
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

        for (line in output.lines()) LOGGER.info(line)

        return isActive!!
    }

    /**
     * @return should this mod be loaded
     */
    @JvmStatic
    fun init(): Boolean {
        preInit()

        if (isActive!!) {
            ToggledFilters.init()
            ItemTypeFilters.init()
            ConfigManager.onLoading()

//            if (CompatUtl.REI.isLoaded()) {
//                JefRegistries.registerProxy(ReiFilterProxyImpl)
//            }
        }

        return isActive!!
    }
}
