package net.psunset.jef.core

import net.minecraft.ChatFormatting
import net.minecraft.client.resources.language.I18n
import net.minecraft.network.chat.Component
import net.minecraft.resources.Identifier
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.ItemLike
import net.psunset.jef.api.IItemTypeFilter

abstract class ItemTypeFilter(
    override val id: Identifier,
    override val translationKey: String,
    iconItem: ItemLike,
) : IItemTypeFilter {
    override val icon by lazy { ItemStack(iconItem) }

    companion object {

        @JvmField
        val TITLE: Component = Component.translatable("gui.justenoughfilters.item_type_filter.title")

        @JvmStatic
        fun genTooltip(currentFilter: IItemTypeFilter): Component {
            val list = TITLE.copy()
            for ((_, filter) in JefRegistries.ITEM_TYPE_FILTERS) {
                if (filter == ItemTypeFilters.OFF) continue
                if (currentFilter == filter) {
                    list.append(Component.literal("\n> ${I18n.get(filter.translationKey)}").withStyle(ChatFormatting.AQUA))
                } else {
                    list.append(Component.literal("\n  ${I18n.get(filter.translationKey)}").withStyle(ChatFormatting.GRAY))
                }
            }
            return list
        }

        @JvmStatic
        fun genTooltip(): Component {
            return genTooltip(FilterManager.itemTypeFilter)
        }
    }
}