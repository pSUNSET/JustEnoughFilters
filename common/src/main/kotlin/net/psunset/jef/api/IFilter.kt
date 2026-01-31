package net.psunset.jef.api

import net.minecraft.world.item.ItemStack

interface IFilter {
    fun matches(stack: ItemStack): Boolean

    /**
     * obj must be not an [ItemStack]
     */
    fun matchesNonItem(obj: Any): Boolean
}