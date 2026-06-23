package net.psunset.jef.tool

import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.item.Item
import kotlin.jvm.optionals.getOrDefault

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

fun Item.toIdString(): String {
    return toId().toString()
}

fun Item.toId(): ResourceLocation {
    return BuiltInRegistries.ITEM.wrapAsHolder(this).unwrapKey().map { it.location() }
        .getOrDefault(RLUtl.UNKNOWN)
}