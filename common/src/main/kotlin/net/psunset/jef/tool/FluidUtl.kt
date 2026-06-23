package net.psunset.jef.tool

import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.level.material.Fluid
import kotlin.jvm.optionals.getOrDefault

object FluidUtl {
    @JvmStatic
    fun of(id: String): Fluid {
        return of(RLUtl.auto(id)!!)
    }

    @JvmStatic
    fun of(id: ResourceLocation): Fluid {
        return BuiltInRegistries.FLUID.get(id)
    }

    @JvmStatic
    fun tryParse(id: String): Fluid? {
        return if (validate(id)) of(id) else null
    }

    @JvmStatic
    fun tryParse(id: ResourceLocation): Fluid? {
        return if (validate(id)) of(id) else null
    }

    @JvmStatic
    fun validate(id: String): Boolean {
        val _id = RLUtl.auto(id)
        return if (_id == null) false else validate(_id)
    }

    @JvmStatic
    fun validate(id: ResourceLocation): Boolean {
        return BuiltInRegistries.FLUID.containsKey(id)
    }
}

fun Fluid.toId(): ResourceLocation {
    return BuiltInRegistries.FLUID.wrapAsHolder(this).unwrapKey().map { it.location() }
        .getOrDefault(RLUtl.UNKNOWN)
}