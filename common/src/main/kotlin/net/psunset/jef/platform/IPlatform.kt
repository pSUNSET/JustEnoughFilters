package net.psunset.jef.platform

interface IPlatform {
    fun isNeoForge(): Boolean
    fun isFabric(): Boolean
    fun isLoaded(modId: String): Boolean
}