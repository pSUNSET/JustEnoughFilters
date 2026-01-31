package net.psunset.jef.util

import net.minecraft.core.registries.Registries
import net.minecraft.tags.TagKey
import net.minecraft.world.item.Item
import net.psunset.jef.tool.RLUtl

object CTags {
    object Items {
        @JvmStatic
        val FOODS: TagKey<Item> = TagKey.create(Registries.ITEM, RLUtl.ofC("foods"))

        @JvmStatic
        val TOOLS: TagKey<Item> = TagKey.create(Registries.ITEM, RLUtl.ofC("tools"))

        @JvmStatic
        val ARMORS: TagKey<Item> = TagKey.create(Registries.ITEM, RLUtl.ofC("armors"))
    }
}