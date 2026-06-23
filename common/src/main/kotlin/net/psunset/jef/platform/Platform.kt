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
    fun name(): String {
        return if (isForge()) "Forge" else "Fabric"
    }

    @JvmStatic
    fun lowercaseName(): String {
        return if (isForge()) "forge" else "fabric"
    }

    @JvmStatic
    fun isLoaded(modId: String): Boolean = innerImpl.isLoaded(modId)

    @JvmStatic
    fun modIdList(): List<String> = innerImpl.getModIds()

    @JvmStatic
    fun configDir(): Path = innerImpl.configDir()

    @JvmStatic
    fun getModName(modId: String): String = innerImpl.getModName(modId) ?: "[unknown]"

    @JvmStatic
    fun modNameList(): List<String> = innerImpl.getModNames()
}