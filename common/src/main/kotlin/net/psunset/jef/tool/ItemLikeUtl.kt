package net.psunset.jef.tool

import net.minecraft.resources.Identifier
import net.minecraft.world.level.ItemLike

object ItemLikeUtl {
    @JvmStatic
    fun of(id: String): ItemLike {
        return of(IdUtl.auto(id)!!)
    }

    @JvmStatic
    fun of(id: Identifier): ItemLike {
        return ItemUtl.tryParse(id) ?: BlockUtl.of(id)
    }

    @JvmStatic
    fun tryParse(id: String): ItemLike? {
        return ItemUtl.tryParse(id) ?: BlockUtl.tryParse(id)
    }

    @JvmStatic
    fun tryParse(id: Identifier): ItemLike? {
        return ItemUtl.tryParse(id) ?: BlockUtl.tryParse(id)
    }

    @JvmStatic
    fun validate(id: String): Boolean {
        return ItemUtl.validate(id) || BlockUtl.validate(id)
    }

    @JvmStatic
    fun validate(id: Identifier): Boolean {
        return ItemUtl.validate(id) || BlockUtl.validate(id)
    }
}