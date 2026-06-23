package net.psunset.jef.builtin

import net.minecraft.ChatFormatting
import net.minecraft.client.resources.language.I18n
import net.minecraft.network.chat.Component
import net.minecraft.resources.Identifier
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.ItemLike
import net.psunset.jef.api.IItemTypeFilter
import net.psunset.jef.registry.JefRegistries

abstract class ItemTypeFilter(
    override val id: Identifier,
    override val translationKey: String,
    iconItem: ItemLike,
) : IItemTypeFilter {

    constructor(id: Identifier, iconItem: ItemLike) : this(
        id,
        "jef.item_type_filter.${id.namespace}.${id.path}",
        iconItem
    )

    override val icon = ItemStack(iconItem)

    companion object {

        @JvmField
        val TITLE: Component = Component.translatable("gui.justenoughfilters.item_type_filter.title")

        @JvmStatic
        fun genTooltip(currentFilter: IItemTypeFilter): Component {
            val list = TITLE.copy()
            for (filter in JefRegistries.ITEM_TYPE_FILTERS.values) {
                if (filter == ItemTypeFilters.OFF) continue
                if (currentFilter == filter) {
                    list.append(
                        Component.literal("\n> ${I18n.get(filter.translationKey)}")
                            .withStyle(ChatFormatting.AQUA)
                    )
                } else {
                    list.append(
                        Component.literal("\n  ${I18n.get(filter.translationKey)}")
                            .withStyle(ChatFormatting.GRAY)
                    )
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