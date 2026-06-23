package net.psunset.jef.builtin

import net.minecraft.client.Minecraft
import net.minecraft.core.component.DataComponents
import net.minecraft.resources.Identifier
import net.minecraft.world.entity.EquipmentSlot
import net.minecraft.world.item.BlockItem
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.Items
import net.minecraft.world.level.block.EntityBlock
import net.psunset.jef.registry.DeferredElement
import net.psunset.jef.registry.JefRegistries
import net.psunset.jef.tool.IdUtl
import net.psunset.jef.util.CTags

object ToggledFilters {

    @JvmStatic
    val ENTITY_BLOCK_FILTER by register(IdUtl.ofJef("entity_blocks")) {
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
    val ENCHANTED_BOOK_FILTER by register(IdUtl.ofJef("enchanted_books")) {
        object : ToggledFilter(it, Items.ENCHANTED_BOOK) {
            override fun matches(stack: ItemStack): Boolean {
                return stack.`is`(Items.ENCHANTED_BOOK)
            }
        }
    }

    @JvmStatic
    val FOOD_FILTER by register(IdUtl.ofJef("food")) {
        object : ToggledFilter(it, Items.APPLE) {
            override fun matches(stack: ItemStack): Boolean {
                return stack.`is`(CTags.Items.FOODS) ||
                        stack.has(DataComponents.FOOD)
            }
        }
    }

    @JvmStatic
    val FUEL_FILTER by register(IdUtl.ofJef("fuels")) {
        object : ToggledFilter(it, Items.COAL) {
            override fun matches(stack: ItemStack): Boolean {
                return if (Minecraft.getInstance().level == null) {
                    false
                } else {
                    Minecraft.getInstance().level!!.fuelValues().isFuel(stack)
                }
            }
        }
    }

    @JvmStatic
    val TOOL_FILTER by register(IdUtl.ofJef("tools")) {
        object : ToggledFilter(it, Items.DIAMOND_PICKAXE) {
            override fun matches(stack: ItemStack): Boolean {
                val equipableData = stack.get(DataComponents.EQUIPPABLE)
                val b0 = stack.has(DataComponents.TOOL)
                val b1 = stack.`is`(CTags.Items.TOOLS)
                val b2 = stack.has(DataComponents.MAX_DAMAGE) &&
                        stack.maxStackSize == 1 &&
                        stack.isEnchantable &&
                        (equipableData == null || !equipableData.slot.isArmor)
                return b0 || b1 || b2
            }
        }
    }

    @JvmStatic
    val ARMOR_FILTER by register(IdUtl.ofJef("armor")) {
        object : ToggledFilter(it, Items.DIAMOND_CHESTPLATE) {
            override fun matches(stack: ItemStack): Boolean {
                val equipableData = stack.get(DataComponents.EQUIPPABLE)
                val b0 = stack.has(DataComponents.MAX_DAMAGE) &&
                        equipableData != null &&
                        equipableData.slot.type == EquipmentSlot.Type.HUMANOID_ARMOR
                val b1 = stack.`is`(CTags.Items.ARMORS)
                return b0 || b1
            }
        }
    }

    @JvmStatic
    private fun register(
        id: Identifier,
        func: (Identifier) -> ToggledFilter
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