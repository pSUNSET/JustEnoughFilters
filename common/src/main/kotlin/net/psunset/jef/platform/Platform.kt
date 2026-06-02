package net.psunset.jef.platform

import org.jetbrains.annotations.ApiStatus
import java.nio.file.Path

object Platform {

    @ApiStatus.Internal
    @JvmStatic
    lateinit var innerImpl: IPlatform

    @JvmStatic
    fun isForge(): Boolean = innerImpl.isForge()

    @JvmStatic
    fun isFabric(): Boolean = innerImpl.isFabric()

    @JvmStatic
    fun isLoaded(modId: String): Boolean = innerImpl.isLoaded(modId)

    @JvmStatic
    fun configDir(): Path = innerImpl.configDir()
}