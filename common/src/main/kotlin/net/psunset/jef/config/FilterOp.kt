package net.psunset.jef.config

import net.minecraft.client.resources.language.I18n
import net.minecraft.world.item.ItemStack
import net.psunset.jef.api.IFilter
import net.psunset.jef.tool.toId
import net.psunset.jef.util.NonItemHelper

abstract class FilterOp : IFilter {

    class ItemOnly(
        private val factory: (ItemStack) -> Boolean
    ) : FilterOp() {
        override fun matches(stack: ItemStack): Boolean {
            return factory(stack)
        }

        override fun matchesNonItem(obj: Any): Boolean {
            return false
        }
    }

    class NonItemOnly(
        private val factory: (Any) -> Boolean
    ) : FilterOp() {
        override fun matches(stack: ItemStack): Boolean {
            return false
        }

        override fun matchesNonItem(obj: Any): Boolean {
            return factory(obj)
        }
    }

    class WithName(
        private val input: String,
        private val factory: (String, String) -> Boolean
    ) : FilterOp() {
        override fun matches(stack: ItemStack): Boolean {
            return factory(I18n.get(stack.itemName.string), input)
        }

        override fun matchesNonItem(obj: Any): Boolean {
            return factory(NonItemHelper.getName(obj), input)
        }
    }

    class WithId(
        private val input: String,
        private val factory: (String, String) -> Boolean
    ) : FilterOp() {
        override fun matches(stack: ItemStack): Boolean {
            return factory(stack.item.toString(), input)
        }

        override fun matchesNonItem(obj: Any): Boolean {
            return factory(NonItemHelper.getId(obj).toString(), input)
        }
    }

    class WithNamespace(
        private val input: String,
        private val factory: (String, String) -> Boolean
    ) : FilterOp() {
        override fun matches(stack: ItemStack): Boolean {
            return factory(stack.item.toId().namespace, input)
        }

        override fun matchesNonItem(obj: Any): Boolean {
            return factory(NonItemHelper.getId(obj).namespace, input)
        }
    }

    class WithPath(
        private val input: String,
        private val factory: (String, String) -> Boolean
    ) : FilterOp() {
        override fun matches(stack: ItemStack): Boolean {
            return factory(stack.item.toId().path, input)
        }

        override fun matchesNonItem(obj: Any): Boolean {
            return factory(NonItemHelper.getId(obj).path, input)
        }
    }

    object None : FilterOp() {
        override fun matches(stack: ItemStack): Boolean = false
        override fun matchesNonItem(obj: Any): Boolean = false
    }
}