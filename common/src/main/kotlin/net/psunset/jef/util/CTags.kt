package net.psunset.jef.util

import net.minecraft.core.registries.Registries
import net.minecraft.tags.TagKey
import net.minecraft.world.item.Item
import net.psunset.jef.tool.RLUtl

object CTags {
    object Items {
        @JvmField
        val FOODS: TagKey<Item> = TagKey.create(Registries.ITEM, RLUtl.ofC("foods"))

        @JvmField
        val TOOLS: TagKey<Item> = TagKey.create(Registries.ITEM, RLUtl.ofC("tools"))

        @JvmField
        val ARMORS: TagKey<Item> = TagKey.create(Registries.ITEM, RLUtl.ofC("armors"))
    }
}