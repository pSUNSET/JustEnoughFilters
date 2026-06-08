package net.psunset.jef.tool

import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.resources.Identifier
import net.minecraft.world.item.Item

object ItemUtl {

    @JvmStatic
    fun of(id: String): Item {
        return of(IdUtl.auto(id)!!)
    }

    @JvmStatic
    fun of(id: Identifier): Item {
        return BuiltInRegistries.ITEM.get(id).get().value()
    }

    @JvmStatic
    fun tryParse(id: String): Item? {
        return if (validate(id)) of(id) else null
    }

    @JvmStatic
    fun tryParse(id: Identifier): Item? {
        return if (validate(id)) of(id) else null
    }

    @JvmStatic
    fun validate(id: String): Boolean {
        val _id = IdUtl.auto(id)
        return if (_id == null) false else validate(_id)
    }

    @JvmStatic
    fun validate(id: Identifier): Boolean {
        return BuiltInRegistries.ITEM.containsKey(id)
    }
}