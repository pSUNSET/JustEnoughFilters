package net.psunset.jef.platform.neoforge

import net.neoforged.fml.ModList
import net.neoforged.fml.loading.FMLLoader
import net.neoforged.fml.loading.FMLPaths
import net.neoforged.fml.loading.LoadingModList
import net.psunset.jef.platform.IPlatform
import net.psunset.jef.platform.Platform
import java.nio.file.Path

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
        return ModList.get()?.isLoaded(modId) ?: FMLLoader.getCurrent().loadingModList.mods.any { it.modId == modId }
    }

    override fun configDir(): Path {
        return FMLPaths.CONFIGDIR.get()
    }
}