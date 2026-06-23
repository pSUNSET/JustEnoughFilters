package net.psunset.jef.tool

import net.minecraft.resources.ResourceLocation
import net.minecraft.world.level.ItemLike

object ItemLikeUtl {
    @JvmStatic
    fun of(id: String): ItemLike {
        return of(RLUtl.auto(id)!!)
    }

    @JvmStatic
    fun of(id: ResourceLocation): ItemLike {
        return ItemUtl.tryParse(id) ?: BlockUtl.of(id)
    }

    @JvmStatic
    fun tryParse(id: String): ItemLike? {
        return ItemUtl.tryParse(id) ?: BlockUtl.tryParse(id)
    }

    @JvmStatic
    fun tryParse(id: ResourceLocation): ItemLike? {
        return ItemUtl.tryParse(id) ?: BlockUtl.tryParse(id)
    }

    @JvmStatic
    fun validate(id: String): Boolean {
        return ItemUtl.validate(id) || BlockUtl.validate(id)
    }

    @JvmStatic
    fun validate(id: ResourceLocation): Boolean {
        return ItemUtl.validate(id) || BlockUtl.validate(id)
    }
}