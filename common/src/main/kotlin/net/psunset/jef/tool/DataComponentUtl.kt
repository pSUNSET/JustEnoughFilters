package net.psunset.jef.tool

import net.minecraft.core.component.DataComponentType
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.resources.Identifier

object DataComponentUtl {

    @JvmStatic
    fun ofUnsafe(id: String): DataComponentType<*> {
        return ofUnsafe(IdUtl.auto(id)!!)
    }

    @JvmStatic
    fun ofUnsafe(id: Identifier): DataComponentType<*> {
        return BuiltInRegistries.DATA_COMPONENT_TYPE.get(id).get().value()
    }

    @JvmStatic
    fun of(id: String): DataComponentType<*>? {
        return if(validate(id)) ofUnsafe(id) else null
    }

    @JvmStatic
    fun of(id: Identifier): DataComponentType<*>? {
        return if (validate(id)) ofUnsafe(id) else null
    }

    @JvmStatic
    fun validate(id: String): Boolean {
        val _id = IdUtl.auto(id)
        return _id != null && validate(_id)
    }

    @JvmStatic
    fun validate(id: Identifier): Boolean {
        return BuiltInRegistries.DATA_COMPONENT_TYPE.containsKey(id)
    }
}