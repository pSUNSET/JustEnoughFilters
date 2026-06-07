package net.psunset.jef.platform

import org.jetbrains.annotations.ApiStatus
import java.nio.file.Path
import kotlin.jvm.optionals.getOrDefault

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

    @JvmStatic
    fun modIdList(): List<String> = innerImpl.getModIds()

    @JvmStatic
    fun configDir(): Path = innerImpl.configDir()

    @JvmStatic
    fun getModName(modId: String): String = innerImpl.getModName(modId) ?: "Unknown"

    @JvmStatic
    fun modNameList(): List<String> = innerImpl.getModNames()
}