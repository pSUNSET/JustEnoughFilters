package net.psunset.jef.tool

import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.resources.Identifier
import net.minecraft.world.level.material.Fluid
import kotlin.jvm.optionals.getOrDefault

object FluidUtl {
    @JvmStatic
    fun of(id: String): Fluid {
        return of(IdUtl.auto(id)!!)
    }

    @JvmStatic
    fun of(id: Identifier): Fluid {
        return BuiltInRegistries.FLUID.get(id).get().value()
    }

    @JvmStatic
    fun tryParse(id: String): Fluid? {
        return if (validate(id)) of(id) else null
    }

    @JvmStatic
    fun tryParse(id: Identifier): Fluid? {
        return if (validate(id)) of(id) else null
    }

    @JvmStatic
    fun validate(id: String): Boolean {
        val _id = IdUtl.auto(id)
        return if (_id == null) false else validate(_id)
    }

    @JvmStatic
    fun validate(id: Identifier): Boolean {
        return BuiltInRegistries.FLUID.containsKey(id)
    }
}

fun Fluid.toId(): Identifier {
    return BuiltInRegistries.FLUID.wrapAsHolder(this).unwrapKey().map { it.identifier() }
        .getOrDefault(IdUtl.UNKNOWN)
}