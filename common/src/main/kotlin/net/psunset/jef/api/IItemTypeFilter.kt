package net.psunset.jef.api

import net.minecraft.resources.ResourceLocation
import net.minecraft.world.item.ItemStack

interface IItemTypeFilter : IFilter {
    val id: ResourceLocation
    val translationKey: String
    val icon: ItemStack
}