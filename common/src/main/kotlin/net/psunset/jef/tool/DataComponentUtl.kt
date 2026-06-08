package net.psunset.jef.tool

import net.minecraft.core.component.DataComponentType
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.resources.ResourceLocation

object DataComponentUtl {
    @JvmStatic
    fun of(id: String): DataComponentType<*> {
        return of(RLUtl.auto(id)!!)
    }

    @JvmStatic
    fun of(id: ResourceLocation): DataComponentType<*> {
        return BuiltInRegistries.DATA_COMPONENT_TYPE.get(id)!!
    }

    @JvmStatic
    fun tryParse(id: String): DataComponentType<*>? {
        return if (validate(id)) of(id) else null
    }

    @JvmStatic
    fun tryParse(id: ResourceLocation): DataComponentType<*>? {
        return if (validate(id)) of(id) else null
    }

    @JvmStatic
    fun validate(id: String): Boolean {
        val _id = RLUtl.auto(id)
        return if (_id == null) false else validate(_id)
    }

    @JvmStatic
    fun validate(id: ResourceLocation): Boolean {
        return BuiltInRegistries.DATA_COMPONENT_TYPE.containsKey(id)
    }
}