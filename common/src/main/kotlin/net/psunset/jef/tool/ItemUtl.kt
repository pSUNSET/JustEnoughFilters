package net.psunset.jef.tool

import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.item.Item

object ItemUtl {
    @JvmStatic
    fun of(id: String): Item {
        return of(RLUtl.auto(id)!!)
    }

    @JvmStatic
    fun of(id: ResourceLocation): Item {
        return BuiltInRegistries.ITEM.get(id)
    }

    @JvmStatic
    fun tryParse(id: String): Item? {
        return if (validate(id)) of(id) else null
    }

    @JvmStatic
    fun tryParse(id: ResourceLocation): Item? {
        return if (validate(id)) of(id) else null
    }

    @JvmStatic
    fun validate(id: String): Boolean {
        val _id = RLUtl.auto(id)
        return if (_id == null) false else validate(_id)
    }

    @JvmStatic
    fun validate(id: ResourceLocation): Boolean {
        return BuiltInRegistries.ITEM.containsKey(id)
    }
}