package net.psunset.jef.tool

import net.psunset.jef.platform.Platform

object CompatUtl {

    object JEI {
        @JvmStatic
        fun isLoaded(): Boolean = Platform.isLoaded("jei")
    }

    object REI {
        @JvmStatic
        fun isLoaded(): Boolean = Platform.isLoaded("roughlyenoughitems")
    }

    object EMI {
        @JvmStatic
        fun isLoaded(): Boolean = Platform.isLoaded("emi")
    }
}