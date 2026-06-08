package net.psunset.jef.tool

import net.minecraft.core.component.DataComponentType
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.resources.Identifier

object DataComponentUtl {

    @JvmStatic
    fun of(id: String): DataComponentType<*> {
        return of(IdUtl.auto(id)!!)
    }

    @JvmStatic
    fun of(id: Identifier): DataComponentType<*> {
        return BuiltInRegistries.DATA_COMPONENT_TYPE.get(id).get().value()
    }

    @JvmStatic
    fun tryParse(id: String): DataComponentType<*>? {
        return if (validate(id)) of(id) else null
    }

    @JvmStatic
    fun tryParse(id: Identifier): DataComponentType<*>? {
        return if (validate(id)) of(id) else null
    }

    @JvmStatic
    fun validate(id: String): Boolean {
        val _id = IdUtl.auto(id)
        return if (_id == null) false else validate(_id)
    }

    @JvmStatic
    fun validate(id: Identifier): Boolean {
        return BuiltInRegistries.DATA_COMPONENT_TYPE.containsKey(id)
    }
}