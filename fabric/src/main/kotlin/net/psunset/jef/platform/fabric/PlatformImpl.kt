package net.psunset.jef.platform.fabric

import net.fabricmc.loader.api.FabricLoader
import net.psunset.jef.platform.IPlatform
import net.psunset.jef.platform.Platform
import java.nio.file.Path
import kotlin.jvm.optionals.getOrNull

object PlatformImpl : IPlatform {
    init {
        Platform.innerImpl = this
    }

    override fun isNeoForge(): Boolean {
        return false
    }

    override fun isFabric(): Boolean {
        return true
    }

    override fun isLoaded(modId: String): Boolean {
        return FabricLoader.getInstance().isModLoaded(modId)
    }

    override fun getModIds(): List<String> {
        return FabricLoader.getInstance().allMods.map { it.metadata.id }
    }

    override fun configDir(): Path {
        return FabricLoader.getInstance().configDir
    }

    override fun getModName(modId: String): String? {
        return FabricLoader.getInstance().getModContainer(modId).getOrNull()?.metadata?.name
    }

    override fun getModNames(): List<String> {
        return FabricLoader.getInstance().allMods.map { it.metadata.name }
    }
}