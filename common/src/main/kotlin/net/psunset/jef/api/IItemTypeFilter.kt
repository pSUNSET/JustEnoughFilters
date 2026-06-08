package net.psunset.jef.api

import net.minecraft.resources.Identifier
import net.minecraft.world.item.ItemStack

interface IItemTypeFilter : IFilter {
    val id: Identifier
    val translationKey: String
    val icon: ItemStack
}