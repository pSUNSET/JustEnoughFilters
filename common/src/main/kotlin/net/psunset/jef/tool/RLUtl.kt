package net.psunset.jef.tool

import net.minecraft.resources.ResourceLocation
import net.psunset.jef.JustEnoughFilters

object RLUtl {
    @JvmStatic
    fun of(path: String): ResourceLocation {
        return ResourceLocation(JustEnoughFilters.ID, path)
    }

    @JvmStatic
    fun ofC(path: String): ResourceLocation {
        return ResourceLocation("c", path)
    }

    @JvmStatic
    fun ofForge(path: String): ResourceLocation {
        return ResourceLocation("forge", path)
    }

    @JvmStatic
    fun ofVanilla(path: String): ResourceLocation {
        return ResourceLocation(path)
    }
}