package net.psunset.jef.platform.fabric

import net.fabricmc.loader.api.FabricLoader
import net.psunset.jef.platform.IPlatform
import net.psunset.jef.platform.Platform

object PlatformImpl : IPlatform {
    init {
        Platform.innerImpl = this
    }

    override fun isForge(): Boolean {
        return false
    }

    override fun isFabric(): Boolean {
        return true
    }

    override fun isLoaded(modId: String): Boolean {
        return FabricLoader.getInstance().isModLoaded(modId)
    }
}