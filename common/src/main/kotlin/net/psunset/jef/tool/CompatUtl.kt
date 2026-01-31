package net.psunset.jef.tool

import net.psunset.jef.platform.Platform

object CompatUtl {

    object JEI {
        fun isLoaded(): Boolean = Platform.isLoaded("jei")
    }

    object REI {
        fun isLoaded(): Boolean = Platform.isLoaded("roughlyenoughitems")
    }

    object EMI {
        fun isLoaded(): Boolean = Platform.isLoaded("emi")
    }
}