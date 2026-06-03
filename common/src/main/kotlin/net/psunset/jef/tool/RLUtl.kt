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

    @JvmStatic
    fun auto(rl: String): ResourceLocation? {
        return ResourceLocation.tryParse(rl)
    }

    @JvmStatic
    fun validate(rl: String): Boolean {
        val idx = rl.indexOf(':')
        if (idx > 0) {
            if (!ResourceLocation.isValidPath(rl.substring(idx + 1))) {
                return false
            } else {
                return ResourceLocation.isValidNamespace(rl.substring(0, idx))
            }
        }
        return false
    }

    @JvmStatic
    fun toValidPath(path: String): String {
        return path.lowercase().replace(Regex("[^a-z0-9._-]"), "_")
    }
}