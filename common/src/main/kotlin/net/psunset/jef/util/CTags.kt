package net.psunset.jef.util

import net.minecraft.core.registries.Registries
import net.minecraft.tags.TagKey
import net.minecraft.world.item.Item
import net.psunset.jef.tool.IdUtl

object CTags {
    object Items {
        @JvmField
        val FOODS: TagKey<Item> = TagKey.create(Registries.ITEM, IdUtl.ofC("foods"))

        @JvmField
        val TOOLS: TagKey<Item> = TagKey.create(Registries.ITEM, IdUtl.ofC("tools"))

        @JvmField
        val HUMANOID_ARMORS: TagKey<Item> = TagKey.create(Registries.ITEM, IdUtl.ofC("armors/humanoid"))
    }
}