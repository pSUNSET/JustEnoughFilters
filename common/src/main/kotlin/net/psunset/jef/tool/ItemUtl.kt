package net.psunset.jef.tool

import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.resources.Identifier
import net.minecraft.world.item.Item

object ItemUtl {

    @JvmStatic
    fun ofUnsafe(id: String): Item {
        return ofUnsafe(IdUtl.auto(id)!!)
    }

    @JvmStatic
    fun ofUnsafe(id: Identifier): Item {
        return BuiltInRegistries.ITEM.get(id).get().value()
    }

    @JvmStatic
    fun of(id: String): Item? {
        return if (validate(id)) ofUnsafe(id) else null
    }

    @JvmStatic
    fun of(id: Identifier): Item? {
        return if (validate(id)) ofUnsafe(id) else null
    }

    @JvmStatic
    fun validate(id: String): Boolean {
        val _id = IdUtl.auto(id)
        return _id != null && validate(_id)
    }

    @JvmStatic
    fun validate(id: Identifier): Boolean {
        return BuiltInRegistries.ITEM.containsKey(id)
    }
}