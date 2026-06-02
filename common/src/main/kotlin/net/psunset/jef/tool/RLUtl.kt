package net.psunset.jef.tool

import net.minecraft.resources.ResourceLocation
import net.psunset.jef.JustEnoughFilters

object RLUtl {
    @JvmStatic
    fun of(namespace: String, path: String): ResourceLocation {
        return ResourceLocation.fromNamespaceAndPath(namespace, path)
    }

    @JvmStatic
    fun ofJef(path: String): ResourceLocation {
        return ResourceLocation.fromNamespaceAndPath(JustEnoughFilters.ID, path)
    }

    @JvmStatic
    fun ofC(path: String): ResourceLocation {
        return ResourceLocation.fromNamespaceAndPath("c", path)
    }

    @JvmStatic
    fun ofVanilla(path: String): ResourceLocation {
        return ResourceLocation.withDefaultNamespace(path)
    }

    @JvmStatic
    fun auto(rl: String): ResourceLocation? {
        return ResourceLocation.tryParse(rl)
    }

    @JvmStatic
    fun toValidPath(path: String): String {
        return path.lowercase().replace(Regex("[^a-z0-9._-]"), "_")
    }
}