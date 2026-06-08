package net.psunset.jef.tool

import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.resources.Identifier
import net.minecraft.world.item.Item
import net.psunset.jef.JustEnoughFilters
import kotlin.jvm.optionals.getOrDefault

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

    @JvmStatic
    fun auto(rl: String): Identifier? {
        return Identifier.tryParse(rl)
    }

    /**
     * Returns true if `rl` is a valid [Identifier] and can be parsed successfully
     */
    @JvmStatic
    fun validate(rl: String): Boolean {
        val idx = rl.indexOf(':')
        if (idx > 0) {
            if (!Identifier.isValidPath(rl.substring(idx + 1))) {
                return false
            } else {
                return Identifier.isValidNamespace(rl.substring(0, idx))
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

    @JvmStatic
    fun toValidPath(path: String): String {
        return path.lowercase().replace(Regex("[^a-z0-9/._-]"), "_")
    }
}

fun Item.toId(): Identifier {
    return BuiltInRegistries.ITEM.wrapAsHolder(this).unwrapKey().map { it.identifier() }
        .getOrDefault(IdUtl.UNKNOWN)
}