package net.psunset.jef.platform.neoforge

import net.neoforged.fml.ModList
import net.neoforged.fml.loading.LoadingModList
import net.psunset.jef.platform.IPlatform
import net.psunset.jef.platform.Platform

object PlatformImpl : IPlatform {
    init {
        Platform.innerImpl = this
    }

    override fun isNeoForge(): Boolean {
        return true
    }

    override fun isFabric(): Boolean {
        return false
    }

    override fun isLoaded(modId: String): Boolean {
        return ModList.get()?.isLoaded(modId) ?: LoadingModList.get().mods.any { it.modId == modId }
    }
}