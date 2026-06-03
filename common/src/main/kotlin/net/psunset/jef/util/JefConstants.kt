package net.psunset.jef.util

import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.tags.ItemTags

object JefConstants {
    @JvmField
    val ITEM_IDS: Set<String> = BuiltInRegistries.ITEM.keySet().map { it.toString() }.toSet()

    @JvmField
    val DATA_COMPONENT_IDS: Set<String> = BuiltInRegistries.DATA_COMPONENT_TYPE.keySet().map { it.toString() }.toSet()
}