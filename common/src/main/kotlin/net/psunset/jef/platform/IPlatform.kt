package net.psunset.jef.platform

import java.nio.file.Path

interface IPlatform {
    fun isNeoForge(): Boolean
    fun isFabric(): Boolean
    fun isLoaded(modId: String): Boolean
    fun getModIds(): List<String>
    fun configDir(): Path
    fun getModName(modId: String): String?
    fun getModNames(): List<String>
}