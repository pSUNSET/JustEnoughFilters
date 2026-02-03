package net.psunset.jef.core

import net.minecraft.world.item.BlockItem
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.Items
import net.minecraft.world.level.block.Blocks
import net.psunset.jef.tool.RLUtl

object ItemTypeFilters {

    @JvmField
    val OFF = object : ItemTypeFilter(
        RLUtl.of("off"),
        "",  // No Usage
        Items.BARRIER,
    ) {
        override fun matches(stack: ItemStack) = true

        override fun matchesNonItem(obj: Any) = true
    }

    @JvmField
    val ITEM = object : ItemTypeFilter(
        RLUtl.of("item"),
        "jef.item_type_filter.justenoughfilters.item",
        Items.IRON_INGOT
    ) {
        override fun matches(stack: ItemStack): Boolean {
            return stack.item !is BlockItem
        }

        override fun matchesNonItem(obj: Any) = false
    }

    @JvmField
    val BLOCK = object : ItemTypeFilter(
        RLUtl.of("block"),
        "jef.item_type_filter.justenoughfilters.block",
        Blocks.GRASS_BLOCK
    ) {
        override fun matches(stack: ItemStack): Boolean {
            return stack.item is BlockItem
        }

        override fun matchesNonItem(obj: Any) = false
    }

    @JvmField
    val NON_ITEM = object : ItemTypeFilter(
        RLUtl.of("non_item"),
        "jef.item_type_filter.justenoughfilters.non_item",
        Items.WATER_BUCKET
    ) {
        override fun matches(stack: ItemStack) = false

        override fun matchesNonItem(obj: Any) = true
    }

    @JvmStatic
    fun init() {
        JefRegistries.registerItemTypeFilter(0, OFF)
        JefRegistries.registerItemTypeFilter(1, ITEM)
        JefRegistries.registerItemTypeFilter(2, BLOCK)
        JefRegistries.registerItemTypeFilter(3, NON_ITEM)
    }
}