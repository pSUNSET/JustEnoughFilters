package net.psunset.jef.config

import net.minecraft.client.resources.language.I18n
import net.minecraft.world.item.ItemStack
import net.psunset.jef.api.IFilter
import net.psunset.jef.tool.idToString
import net.psunset.jef.tool.toId

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
    ): FilterOp() {
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
            return factory(I18n.get(stack.descriptionId), input)
        }

        override fun matchesNonItem(obj: Any): Boolean {
            return false
            TODO()
        }
    }

    class WithId(
        private val input: String,
        private val factory: (String, String) -> Boolean
    ) : FilterOp() {
        override fun matches(stack: ItemStack): Boolean {
            return factory(stack.item.idToString(), input)
        }

        override fun matchesNonItem(obj: Any): Boolean {
            return false
            TODO()
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
            return false
            TODO()
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
            return false
            TODO()
        }
    }

    object None : FilterOp() {
        override fun matches(stack: ItemStack): Boolean = false
        override fun matchesNonItem(obj: Any): Boolean = false
    }
}