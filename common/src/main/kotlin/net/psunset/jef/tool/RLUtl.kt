package net.psunset.jef.tool

import net.minecraft.resources.ResourceLocation
import net.psunset.jef.JustEnoughFilters

object RLUtl {
    @JvmField
    val UNKNOWN = of("unknown", "unknown")

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

    /**
     * Only full location is allowed.
     * Empty namespace standing for [ResourceLocation.DEFAULT_NAMESPACE] is not allowed.
     *
     * @return Parsed `rl` if it is available; otherwise, `null`
     */
    @JvmStatic
    fun auto(rl: String): ResourceLocation? {
        val idx = rl.indexOf(':')
        if (idx > 0) {
            val path = rl.substring(idx + 1)
            if (ResourceLocation.isValidPath(path)) {
                val namespace = rl.substring(0, idx)
                if (ResourceLocation.isValidNamespace(namespace)) {
                    return of(namespace, path)
                }
            }
        }
        return null
    }

    /**
     * Only full location is allowed.
     * Empty namespace standing for [ResourceLocation.DEFAULT_NAMESPACE] is not allowed.
     *
     * @return `true` if `rl` is a valid [ResourceLocation] and can be parsed successfully
     */
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

    /**
     * @return `true` if every single char in `partial` is allowed in [ResourceLocation]
     */
    @JvmStatic
    fun validatePartial(partial: String): Boolean {
        return partial.all { ResourceLocation.isAllowedInResourceLocation(it) }
    }

    @JvmStatic
    fun toValidPath(path: String): String {
        return path.lowercase().replace(Regex("[^a-z0-9/._-]"), "_")
    }
}