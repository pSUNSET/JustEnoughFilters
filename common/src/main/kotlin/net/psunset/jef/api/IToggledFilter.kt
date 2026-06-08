package net.psunset.jef.api

import net.minecraft.network.chat.Component
import net.minecraft.resources.Identifier
import net.minecraft.world.item.ItemStack

interface IToggledFilter : IFilter {
    val id: Identifier
    val tooltip: Component

    val activeIcon: ItemStack
    val inactiveIcon: ItemStack
}