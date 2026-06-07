package net.psunset.jef.util

import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.tags.ItemTags
import net.psunset.jef.platform.Platform

object JefConstants {
    @JvmField
    val ITEM_IDS: Set<String> = BuiltInRegistries.ITEM.keySet().map { it.toString() }.toSet()

    @JvmField
    val DATA_COMPONENT_IDS: Set<String> = BuiltInRegistries.DATA_COMPONENT_TYPE.keySet().map { it.toString() }.toSet()

    @JvmStatic
    val MOD_ID_LIST: List<String> by lazy { Platform.modIdList() }

    @JvmStatic
    val MOD_NAME_LIST: List<String> by lazy { Platform.modNameList() }
}