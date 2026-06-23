package net.psunset.jef.tool

import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.resources.Identifier
import net.minecraft.world.level.block.Block
import kotlin.jvm.optionals.getOrDefault

object BlockUtl {
    @JvmStatic
    fun of(id: String): Block {
        return of(IdUtl.auto(id)!!)
    }

    @JvmStatic
    fun of(id: Identifier): Block {
        return BuiltInRegistries.BLOCK.get(id).get().value()
    }

    @JvmStatic
    fun tryParse(id: String): Block? {
        return if (validate(id)) of(id) else null
    }

    @JvmStatic
    fun tryParse(id: Identifier): Block? {
        return if (validate(id)) of(id) else null
    }

    @JvmStatic
    fun validate(id: String): Boolean {
        val _id = IdUtl.auto(id)
        return if (_id == null) false else validate(_id)
    }

    @JvmStatic
    fun validate(id: Identifier): Boolean {
        return BuiltInRegistries.BLOCK.containsKey(id)
    }
}

fun Block.toId(): Identifier {
    return BuiltInRegistries.BLOCK.wrapAsHolder(this).unwrapKey().map { it.identifier() }
        .getOrDefault(IdUtl.UNKNOWN)
}