package net.psunset.jef.platform

object Platform {
    @JvmStatic
    fun isForge(): Boolean = throw AssertionError()

    @JvmStatic
    fun isFabric(): Boolean = throw AssertionError()

    @JvmStatic
    fun isLoaded(modId: String): Boolean = throw AssertionError()
}