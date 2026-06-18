package net.psunset.jef.builtin

import net.minecraft.core.component.DataComponents
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.entity.EquipmentSlot
import net.minecraft.world.item.*
import net.minecraft.world.level.block.EntityBlock
import net.minecraft.world.level.block.entity.AbstractFurnaceBlockEntity
import net.psunset.jef.registry.DeferredElement
import net.psunset.jef.registry.JefRegistries
import net.psunset.jef.tool.RLUtl
import net.psunset.jef.util.CTags

object ToggledFilters {

    @JvmStatic
    val ENTITY_BLOCK_FILTER by register(RLUtl.ofJef("entity_blocks")) {
        object : ToggledFilter(it, Items.FURNACE) {
            override fun matches(stack: ItemStack): Boolean {
                val item = stack.item
                if (item is BlockItem) {
                    return item.block is EntityBlock
                }
                return false
            }
        }
    }

    @JvmStatic
    val ENCHANTED_BOOK_FILTER by register(RLUtl.ofJef("enchanted_books")) {
        object : ToggledFilter(it, Items.ENCHANTED_BOOK) {
            override fun matches(stack: ItemStack): Boolean {
                return stack.item is EnchantedBookItem
            }
        }
    }

    @JvmStatic
    val FOOD_FILTER by register(RLUtl.ofJef("food")) {
        object : ToggledFilter(it, Items.APPLE) {
            override fun matches(stack: ItemStack): Boolean {
                return stack.`is`(CTags.Items.FOODS) ||
                        stack.has(DataComponents.FOOD)
            }
        }
    }

    @JvmStatic
    val FUEL_FILTER by register(RLUtl.ofJef("fuels")) {
        object : ToggledFilter(it, Items.COAL) {
            override fun matches(stack: ItemStack): Boolean {
                return AbstractFurnaceBlockEntity.isFuel(stack)
            }
        }
    }

    @JvmStatic
    val TOOL_FILTER by register(RLUtl.ofJef("tools")) {
        object : ToggledFilter(it, Items.DIAMOND_PICKAXE) {
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
    }

    @JvmStatic
    val ARMOR_FILTER by register(RLUtl.ofJef("armor")) {
        object : ToggledFilter(it, Items.DIAMOND_CHESTPLATE) {
            override fun matches(stack: ItemStack): Boolean {
                val item = stack.item
                val b0 = item is Equipable && item.equipmentSlot.type == EquipmentSlot.Type.HUMANOID_ARMOR
                val b1 = stack.`is`(CTags.Items.ARMORS)
                return b0 || b1
            }
        }
    }

    @JvmStatic
    private fun register(
        id: ResourceLocation,
        func: (ResourceLocation) -> ToggledFilter
    ): DeferredElement<ToggledFilter> {
        return JefRegistries.TOGGLED_FILTERS.register(id, func)
    }

    /**
     * Do nothing but simply run static body
     */
    @JvmStatic
    fun init() {
    }
}