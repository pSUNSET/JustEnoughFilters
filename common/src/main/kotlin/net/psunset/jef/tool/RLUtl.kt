package net.psunset.jef.tool

import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.item.Item
import net.psunset.jef.JustEnoughFilters
import kotlin.jvm.optionals.getOrDefault

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

    @JvmStatic
    fun auto(rl: String): ResourceLocation? {
        return ResourceLocation.tryParse(rl)
    }

    /**
     * Returns true if `rl` is a valid [ResourceLocation] and can be parsed successfully
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
     * Returns true if every single char in `partial` is allowed in [ResourceLocation]
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

fun Item.toId(): ResourceLocation {
    return BuiltInRegistries.ITEM.wrapAsHolder(this).unwrapKey().map { it.location() }
        .getOrDefault(RLUtl.UNKNOWN)
}