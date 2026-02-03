package net.psunset.jef.core

import net.minecraft.core.component.DataComponents
import net.minecraft.network.chat.Component
import net.minecraft.world.entity.EquipmentSlot
import net.minecraft.world.item.BlockItem
import net.minecraft.world.item.EnchantedBookItem
import net.minecraft.world.item.Equipable
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.Items
import net.minecraft.world.level.block.EntityBlock
import net.minecraft.world.level.block.entity.AbstractFurnaceBlockEntity
import net.psunset.jef.tool.RLUtl
import net.psunset.jef.util.CTags

object ToggledFilters {

    @JvmField
    val ENTITY_BLOCK_FILTER = object : ToggledFilter(
        RLUtl.of("entity_blocks"),
        Items.FURNACE,
        Component.translatable("jef.toggled_filter.justenoughfilters.entity_blocks")
    ) {
        override fun matches(stack: ItemStack): Boolean {
            val item = stack.item
            if (item is BlockItem) {
                return item.block is EntityBlock
            }
            return false
        }
    }

    @JvmField
    val ENCHANTED_BOOK_FILTER = object : ToggledFilter(
        RLUtl.of("enchanted_books"),
        Items.ENCHANTED_BOOK,
        Component.translatable("jef.toggled_filter.justenoughfilters.enchanted_books")
    ) {
        override fun matches(stack: ItemStack): Boolean {
            return stack.item is EnchantedBookItem
        }
    }

    @JvmField
    val FOOD_FILTER = object : ToggledFilter(
        RLUtl.of("food"),
        Items.APPLE,
        Component.translatable("jef.toggled_filter.justenoughfilters.food")
    ) {
        override fun matches(stack: ItemStack): Boolean {
            return stack.`is`(CTags.Items.FOODS) ||
                    stack.has(DataComponents.FOOD)
        }
    }

    @JvmField
    val FUEL_FILTER = object : ToggledFilter(
        RLUtl.of("fuels"),
        Items.COAL,
        Component.translatable("jef.toggled_filter.justenoughfilters.fuels")
    ) {
        override fun matches(stack: ItemStack): Boolean {
            return AbstractFurnaceBlockEntity.isFuel(stack)
        }
    }

    @JvmField
    val TOOL_FILTER = object : ToggledFilter(
        RLUtl.of("tools"),
        Items.DIAMOND_PICKAXE,
        Component.translatable("jef.toggled_filter.justenoughfilters.tools")
    ) {
        override fun matches(stack: ItemStack): Boolean {
            val item = stack.item
            val b0 = stack.has(DataComponents.TOOL)
            val b1 = stack.`is`(CTags.Items.TOOLS)
            val b2 = stack.has(DataComponents.MAX_DAMAGE) &&
                    stack.maxStackSize == 1 &&
                    stack.isEnchantable &&
                    (item !is Equipable || !item.equipmentSlot.isArmor)
            return b0 || b1 || b2
        }
    }

    @JvmField
    val ARMOR_FILTER = object : ToggledFilter(
        RLUtl.of("armor"),
        Items.DIAMOND_CHESTPLATE,
        Component.translatable("jef.toggled_filter.justenoughfilters.armor")
    ) {
        override fun matches(stack: ItemStack): Boolean {
            val item = stack.item
            val b0 = item is Equipable && item.equipmentSlot.type == EquipmentSlot.Type.HUMANOID_ARMOR
            val b1 = stack.`is`(CTags.Items.ARMORS)
            return b0 || b1
        }
    }

    @JvmStatic
    fun init() {
        JefRegistries.registerToggledFilter(ENTITY_BLOCK_FILTER)
        JefRegistries.registerToggledFilter(FOOD_FILTER)
        JefRegistries.registerToggledFilter(FUEL_FILTER)
        JefRegistries.registerToggledFilter(TOOL_FILTER)
        JefRegistries.registerToggledFilter(ARMOR_FILTER)
        JefRegistries.registerToggledFilter(ENCHANTED_BOOK_FILTER)
    }
}