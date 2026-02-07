package net.psunset.jef.item

import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.ItemLike

class FoilItemStack(itemLike: ItemLike) : ItemStack(itemLike) {
    override fun hasFoil(): Boolean {
        return true
    }
}