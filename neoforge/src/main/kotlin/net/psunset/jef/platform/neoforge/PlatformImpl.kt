package net.psunset.jef.platform.neoforge

import net.neoforged.fml.ModList
import net.neoforged.fml.loading.FMLPaths
import net.neoforged.fml.loading.LoadingModList
import net.psunset.jef.platform.IPlatform
import net.psunset.jef.platform.Platform
import java.nio.file.Path
import kotlin.jvm.optionals.getOrNull

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

    override fun getModIds(): List<String> {
        return (ModList.get()?.mods ?: LoadingModList.get().mods).map { it.modId }
    }

    override fun configDir(): Path {
        return FMLPaths.CONFIGDIR.get()
    }

    override fun getModName(modId: String): String? {
        return if (ModList.get() == null) {
            LoadingModList.get().mods.find { it.modId == modId }?.displayName
        } else {
            ModList.get().getModContainerById(modId).getOrNull()?.modId
        }
    }

    override fun getModNames(): List<String> {
        return (ModList.get()?.mods ?: LoadingModList.get().mods).map { it.displayName }
    }
}