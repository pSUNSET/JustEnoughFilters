package net.psunset.jef.core

import net.minecraft.network.chat.Component
import net.minecraft.world.entity.EquipmentSlot
import net.minecraft.world.item.*
import net.minecraft.world.level.block.EntityBlock
import net.minecraft.world.level.block.entity.AbstractFurnaceBlockEntity
import net.psunset.jef.tool.RLUtl
import net.psunset.jef.util.CTags

object BuiltinToggledFilters {

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

    val ENCHANTED_BOOK_FILTER = object : ToggledFilter(
        RLUtl.of("enchanted_books"),
        Items.ENCHANTED_BOOK,
        Component.translatable("jef.toggled_filter.justenoughfilters.enchanted_books")
    ) {
        override fun matches(stack: ItemStack): Boolean {
            return stack.item is EnchantedBookItem
        }
    }

    val FOOD_FILTER = object : ToggledFilter(
        RLUtl.of("food"),
        Items.APPLE,
        Component.translatable("jef.toggled_filter.justenoughfilters.food")
    ) {
        override fun matches(stack: ItemStack): Boolean {
            return stack.`is`(CTags.Items.FOODS) || stack.isEdible
        }
    }

    val FUEL_FILTER = object : ToggledFilter(
        RLUtl.of("fuels"),
        Items.COAL,
        Component.translatable("jef.toggled_filter.justenoughfilters.fuels")
    ) {
        override fun matches(stack: ItemStack): Boolean {
            return AbstractFurnaceBlockEntity.isFuel(stack)
        }
    }

    val TOOL_FILTER = object : ToggledFilter(
        RLUtl.of("tools"),
        Items.DIAMOND_PICKAXE,
        Component.translatable("jef.toggled_filter.justenoughfilters.tools")
    ) {
        override fun matches(stack: ItemStack): Boolean {
            val item = stack.item
            val b0 = !stack.getAttributeModifiers(EquipmentSlot.MAINHAND).isEmpty
            val b1 = stack.`is`(CTags.Items.TOOLS)
            val b2 = stack.maxDamage > 0 &&
                    stack.maxStackSize == 1 &&
                    stack.isEnchantable &&
                    (item !is Equipable || !item.equipmentSlot.isArmor)
            return b0 || b1 || b2
        }
    }

    val ARMOR_FILTER = object : ToggledFilter(
        RLUtl.of("armor"),
        Items.DIAMOND_CHESTPLATE,
        Component.translatable("jef.toggled_filter.justenoughfilters.armor")
    ) {
        override fun matches(stack: ItemStack): Boolean {
            val item = stack.item
            val b0 = item is Equipable && item.equipmentSlot.type == EquipmentSlot.Type.ARMOR
            val b1 = stack.`is`(CTags.Items.ARMORS)
            return b0 || b1
        }
    }

    fun init() {
        FilterManager.registerToggledFilter(ENTITY_BLOCK_FILTER)
        FilterManager.registerToggledFilter(FOOD_FILTER)
        FilterManager.registerToggledFilter(FUEL_FILTER)
        FilterManager.registerToggledFilter(TOOL_FILTER)
        FilterManager.registerToggledFilter(ARMOR_FILTER)
        FilterManager.registerToggledFilter(ENCHANTED_BOOK_FILTER)
    }
}
