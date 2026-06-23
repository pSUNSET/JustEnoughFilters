package net.psunset.jef.tool

import net.minecraft.resources.ResourceLocation
import net.psunset.jef.JustEnoughFilters

object RLUtl {
    @JvmField
    val UNKNOWN = of("unknown", "unknown")

    @JvmStatic
    fun of(namespace: String, path: String): ResourceLocation {
        return ResourceLocation(namespace, path)
    }

    @JvmStatic
    fun ofJef(path: String): ResourceLocation {
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
            if (!isValidPath(rl.substring(idx + 1))) {
                return false
            } else {
                return isValidNamespace(rl.substring(0, idx))
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

    /**
     * Replaces all uppercase char to lowercase one and all other invalid char to underscore `_`.
     *
     * @return validated path
     */
    @JvmStatic
    fun toValidPath(path: String): String {
        return path.lowercase().toCharArray().apply {
            for (i in indices) {
                if (!ResourceLocation.validPathChar(this[i])) {
                    this[i] = '_'
                }
            }
        }.concatToString()
    }

    @JvmStatic
    fun isValidNamespace(namespace: String): Boolean {
        return namespace.all { validNamespaceChar(it) }
    }

    @JvmStatic
    fun validNamespaceChar(ch: Char): Boolean {
        return ch == '_' || ch == '-' || ch in 'a'..'z' || ch in '0'..'9' || ch == '.'
    }

    @JvmStatic
    fun isValidPath(path: String): Boolean {
        return path.all { validPathChar(it) }
    }

    @JvmStatic
    fun validPathChar(ch: Char): Boolean {
        return ch == '_' || ch == '-' || ch in 'a'..'z' || ch in '0'..'9' || ch == '/' || ch == '.'
    }
}

fun Item.idToString(): String {
    return toId().toString()
}

fun Item.toId(): ResourceLocation {
    return BuiltInRegistries.ITEM.getKey(this)
}