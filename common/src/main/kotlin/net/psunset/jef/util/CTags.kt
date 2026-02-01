package net.psunset.jef.util

import net.minecraft.core.registries.Registries
import net.minecraft.resources.ResourceLocation
import net.minecraft.tags.TagKey
import net.minecraft.world.item.Item
import net.psunset.jef.platform.Platform
import net.psunset.jef.tool.RLUtl

object CTags {
    object Items {
        @JvmStatic
        val FOODS: TagKey<Item> = TagKey.create(Registries.ITEM, tagRL("foods"))

        @JvmStatic
        val TOOLS: TagKey<Item> = TagKey.create(Registries.ITEM, tagRL("tools"))

        @JvmStatic
        val ARMORS: TagKey<Item> = TagKey.create(Registries.ITEM, tagRL("armors"))
    }

    @JvmStatic
    fun tagRL(path: String): ResourceLocation {
        return if (Platform.isForge())
            RLUtl.ofForge(path)
        else
            RLUtl.ofC(path)
    }
}