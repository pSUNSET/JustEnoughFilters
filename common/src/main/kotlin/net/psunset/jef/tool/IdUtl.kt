package net.psunset.jef.tool

import net.minecraft.resources.Identifier
import net.psunset.jef.JustEnoughFilters

object IdUtl {
    @JvmField
    val UNKNOWN = of("unknown", "unknown")

    @JvmStatic
    fun of(namespace: String, path: String): Identifier {
        return Identifier.fromNamespaceAndPath(namespace, path)
    }

    @JvmStatic
    fun ofJef(path: String): Identifier {
        return Identifier.fromNamespaceAndPath(JustEnoughFilters.ID, path)
    }

    @JvmStatic
    fun ofC(path: String): Identifier {
        return Identifier.fromNamespaceAndPath("c", path)
    }

    @JvmStatic
    fun ofVanilla(path: String): Identifier {
        return Identifier.withDefaultNamespace(path)
    }

    /**
     * Only full location is allowed.
     * Empty namespace standing for [Identifier.DEFAULT_NAMESPACE] is not allowed.
     *
     * @return Parsed `id` if it is available; otherwise, `null`
     */
    @JvmStatic
    fun auto(id: String): Identifier? {
        val idx = id.indexOf(':')
        if (idx > 0) {
            val path = id.substring(idx + 1)
            if (Identifier.isValidPath(path)) {
                val namespace = id.substring(0, idx)
                if (Identifier.isValidNamespace(namespace)) {
                    return of(namespace, path)
                }
            }
        }
        return null
    }

    /**
     * Only full location is allowed.
     * Empty namespace standing for [Identifier.DEFAULT_NAMESPACE] is not allowed.
     *
     * @return `true` if `rl` is a valid [Identifier] and can be parsed successfully
     */
    @JvmStatic
    fun validate(id: String): Boolean {
        val idx = id.indexOf(':')
        if (idx > 0) {
            if (!Identifier.isValidPath(id.substring(idx + 1))) {
                return false
            } else {
                return Identifier.isValidNamespace(id.substring(0, idx))
            }
        }
        return false
    }

    /**
     * Returns true if every single char in `partial` is allowed in [Identifier]
     */
    @JvmStatic
    fun validatePartial(partial: String): Boolean {
        return partial.all { Identifier.isAllowedInIdentifier(it) }
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
                if (!Identifier.validPathChar(this[i])) {
                    this[i] = '_'
                }
            }
        }.concatToString()
    }
}