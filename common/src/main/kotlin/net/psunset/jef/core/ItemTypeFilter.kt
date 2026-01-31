package net.psunset.jef.core

import net.minecraft.ChatFormatting
import net.minecraft.client.resources.language.I18n
import net.minecraft.network.chat.Component
import net.minecraft.world.item.BlockItem
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.Items
import net.minecraft.world.level.ItemLike
import net.minecraft.world.level.block.Blocks
import net.psunset.jef.api.IFilter

enum class ItemTypeFilter(
    val translationKey: String,
    iconItem: ItemLike,
) : IFilter {
    OFF(
        "",  // No Usage
        Items.BARRIER,
    ) {
        override fun matches(stack: ItemStack) = true

        override fun matchesNonItem(obj: Any) = true
    },
    ITEM(
        "jef.item_type_filter.justenoughfilters.item",
        Items.IRON_INGOT
    ) {
        override fun matches(stack: ItemStack): Boolean {
            return stack.item !is BlockItem
        }

        override fun matchesNonItem(obj: Any) = false
    },
    BLOCK(
        "jef.item_type_filter.justenoughfilters.block",
        Blocks.GRASS_BLOCK
    ) {
        override fun matches(stack: ItemStack): Boolean {
            return stack.item is BlockItem
        }

        override fun matchesNonItem(obj: Any) = false
    },
    NON_ITEM(
        "jef.item_type_filter.justenoughfilters.non_item",
        Items.WATER_BUCKET
    ) {
        override fun matches(stack: ItemStack) = false

        override fun matchesNonItem(obj: Any) = true
    };

    val icon = ItemStack(iconItem)

    companion object {
        @JvmStatic
        val TITLE: Component = Component.translatable("jef.item_type_filter.title")

        @JvmStatic
        fun genTooltip(currentFilter: ItemTypeFilter): List<Component> {
            val list = mutableListOf<Component>(TITLE)
            for (filter in entries) {
                if (filter == OFF) continue
                if (currentFilter == filter) {
                    list.add(Component.literal("> ${I18n.get(filter.translationKey)}").withStyle(ChatFormatting.AQUA))
                } else {
                    list.add(Component.literal("  ${I18n.get(filter.translationKey)}").withStyle(ChatFormatting.GRAY))
                }
            }
            return list
        }
    }
}