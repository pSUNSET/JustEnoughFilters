package net.psunset.jef.platform

import org.jetbrains.annotations.ApiStatus

object Platform {

    @ApiStatus.Internal
    @JvmStatic
    lateinit var innerImpl: IPlatform

    @JvmStatic
    fun isNeoForge(): Boolean = innerImpl.isNeoForge()

    @JvmStatic
    fun isFabric(): Boolean = innerImpl.isFabric()

    @JvmStatic
    fun isLoaded(modId: String): Boolean = innerImpl.isLoaded(modId)
}