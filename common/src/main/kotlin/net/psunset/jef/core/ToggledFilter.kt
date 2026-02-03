package net.psunset.jef.core

import net.minecraft.network.chat.Component
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.ItemLike
import net.psunset.jef.api.IToggledFilter
import net.psunset.jef.item.FoilItemStack
import net.psunset.jef.item.NotFoilItemStack

abstract class ToggledFilter(
    override val id: ResourceLocation,
    iconItem: ItemLike,
    override val tooltip: Component,
) : IToggledFilter {

    override val inactiveIcon: ItemStack = NotFoilItemStack(iconItem)
    override val activeIcon: ItemStack = FoilItemStack(iconItem)

    override fun matchesNonItem(obj: Any): Boolean = false
    
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is ToggledFilter) return false
        return id == other.id
    }

    override fun hashCode(): Int {
        return id.hashCode()
    }
}
