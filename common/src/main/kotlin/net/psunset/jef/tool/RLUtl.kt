package net.psunset.jef.tool

import net.minecraft.resources.ResourceLocation
import net.psunset.jef.JustEnoughFilters

object RLUtl {
    @JvmStatic
    fun of(path: String): ResourceLocation {
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
}