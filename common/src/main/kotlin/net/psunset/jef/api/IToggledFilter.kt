package net.psunset.jef.api

import net.minecraft.network.chat.Component
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.item.ItemStack

interface IToggledFilter : IFilter {
    val id: ResourceLocation
    val tooltip: Component

    val activeIcon: ItemStack
    val inactiveIcon: ItemStack
}