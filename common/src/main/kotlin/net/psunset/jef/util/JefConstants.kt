package net.psunset.jef.util

import net.minecraft.core.registries.BuiltInRegistries

object JefConstants {
    @JvmField
    val ITEM_IDS: Set<String> = BuiltInRegistries.ITEM.keySet().map { it.toString() }.toSet()
}