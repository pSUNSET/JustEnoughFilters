package net.psunset.jef.builtin

import net.minecraft.resources.ResourceLocation
import net.minecraft.world.item.BlockItem
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.Items
import net.minecraft.world.level.block.Blocks
import net.psunset.jef.registry.DeferredElement
import net.psunset.jef.registry.JefRegistries
import net.psunset.jef.tool.RLUtl

object ItemTypeFilters {

    @JvmStatic
    val OFF by priorReg(RLUtl.ofJef("off")) {
        object : ItemTypeFilter(
            it,
            "",  // No Usage
            Items.BARRIER
        ) {
            override fun matches(stack: ItemStack) = true

            override fun matchesNonItem(obj: Any) = true
        }
    }

    @JvmStatic
    val ITEM by priorReg(RLUtl.ofJef("item")) {
        object : ItemTypeFilter(
            it,
            Items.IRON_INGOT
        ) {
            override fun matches(stack: ItemStack): Boolean {
                return stack.item !is BlockItem
            }

            override fun matchesNonItem(obj: Any) = false
        }
    }

    @JvmStatic
    val BLOCK by priorReg(RLUtl.ofJef("block")) {
        object : ItemTypeFilter(
            it,
            Blocks.GRASS_BLOCK
        ) {
            override fun matches(stack: ItemStack): Boolean {
                return stack.item is BlockItem
            }

            override fun matchesNonItem(obj: Any) = false
        }
    }

    @JvmStatic
    val NON_ITEM by priorReg(RLUtl.ofJef("non_item")) {
        object : ItemTypeFilter(
            it,
            Items.WATER_BUCKET
        ) {
            override fun matches(stack: ItemStack) = false

            override fun matchesNonItem(obj: Any) = true
        }
    }

    @JvmStatic
    private fun priorReg(
        id: ResourceLocation,
        func: (ResourceLocation) -> ItemTypeFilter
    ): DeferredElement<ItemTypeFilter> {
        return JefRegistries.ITEM_TYPE_FILTERS.priorReg(id, func)
    }

    /**
     * Do nothing but simply run static body
     */
    @JvmStatic
    fun init() {
    }
}