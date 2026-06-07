package net.psunset.jef.util

import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.tags.ItemTags
import net.psunset.jef.platform.Platform

object JefConstants {
    @JvmField
    val ITEM_IDS: Set<String> = BuiltInRegistries.ITEM.keySet().map { it.toString() }.toSet()

    // Data Component doesn't exist on 1.21.1
//    @JvmField
//    val DATA_COMPONENT_IDS: Set<String> = BuiltInRegistries.DATA_COMPONENT_TYPE.keySet().map { it.toString() }.toSet()

    @JvmStatic
    val MOD_ID_LIST: List<String> by lazy { Platform.modIdList() }

    @JvmStatic
    val MOD_NAME_LIST: List<String> by lazy { Platform.modNameList() }

    /**
     * Equals to `Button.BIG_WIDTH` since 1.21.1
     */
    const val BIG_BUTTON_WIDTH = 200

    /**
     * Equals to `Button.DEFAULT_SPACING` since 1.21.1
     */
    const val BUTTON_SPACING = 8
}