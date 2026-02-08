package net.psunset.jef.platform

interface IPlatform {
    fun isForge(): Boolean
    fun isFabric(): Boolean
    fun isLoaded(modId: String): Boolean
}