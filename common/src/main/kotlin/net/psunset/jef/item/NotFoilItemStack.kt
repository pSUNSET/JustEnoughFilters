package net.psunset.jef.item

import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.ItemLike

class NotFoilItemStack(itemLike: ItemLike) : ItemStack(itemLike) {
    override fun hasFoil(): Boolean {
        return false
    }
}