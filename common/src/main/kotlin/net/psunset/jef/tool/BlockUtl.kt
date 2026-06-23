package net.psunset.jef.tool

import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.level.block.Block
import kotlin.jvm.optionals.getOrDefault

object BlockUtl {
    @JvmStatic
    fun of(id: String): Block {
        return of(RLUtl.auto(id)!!)
    }

    @JvmStatic
    fun of(id: ResourceLocation): Block {
        return BuiltInRegistries.BLOCK.get(id)
    }

    @JvmStatic
    fun tryParse(id: String): Block? {
        return if (validate(id)) of(id) else null
    }

    @JvmStatic
    fun tryParse(id: ResourceLocation): Block? {
        return if (validate(id)) of(id) else null
    }

    @JvmStatic
    fun validate(id: String): Boolean {
        val _id = RLUtl.auto(id)
        return if (_id == null) false else validate(_id)
    }

    @JvmStatic
    fun validate(id: ResourceLocation): Boolean {
        return BuiltInRegistries.BLOCK.containsKey(id)
    }
}

fun Block.toId(): ResourceLocation {
    return BuiltInRegistries.BLOCK.wrapAsHolder(this).unwrapKey().map { it.location() }
        .getOrDefault(RLUtl.UNKNOWN)
}