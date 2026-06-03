package net.psunset.jef.config.element

import net.minecraft.client.resources.language.I18n
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.network.chat.Component
import net.minecraft.world.item.BlockItem
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.block.entity.AbstractFurnaceBlockEntity
import net.psunset.jef.api.IFilter
import net.psunset.jef.config.element.FilterOp.ItemOnly
import net.psunset.jef.config.element.FilterOp.WithName
import net.psunset.jef.tool.RLUtl
import net.psunset.jef.util.JefConstants

abstract class FilterOp(
    val tooltip: Component,
) : IFilter {

    class ItemOnly(
        tooltip: Component,
        private val factory: (ItemStack) -> Boolean
    ) : FilterOp(tooltip) {
        override fun matches(stack: ItemStack): Boolean {
            return factory(stack)
        }

        override fun matchesNonItem(obj: Any): Boolean {
            return false
        }
    }

    class WithName(
        tooltip: Component,
        private val input: String,
        private val factory: (String, String) -> Boolean
    ) : FilterOp(tooltip) {
        override fun matches(stack: ItemStack): Boolean {
            return factory(I18n.get(stack.descriptionId), input)
        }

        override fun matchesNonItem(obj: Any): Boolean {
            return false
            TODO()
        }
    }
}

/**
 * To create a [FilterOp]
 */
fun interface FilterOpFactory : Function<FilterOp> {
    fun create(input: String): FilterOp
}

/**
 * To create a [FilterOp] without any `input`
 */
fun interface FilterOpFactory0 : Function0<FilterOp>, FilterOpFactory {
    override fun create(input: String): FilterOp {
        require(input.isEmpty()) { "This factory does not accept input" }
        return this.invoke()
    }
}

/**
 * To create a [FilterOp] with an `input`
 */
fun interface FilterOpFactory1 : Function1<String, FilterOp>, FilterOpFactory {
    override fun create(input: String): FilterOp {
        return this.invoke(input)
    }
}

enum class FilterOpProvider(val validator: ((String) -> Boolean)?, val factory: FilterOpFactory) : FilterOpFactory {
    name_is(FilterOpFactory1 { input ->
        WithName(Component.translatable("jef.filter_op.name_is"), input) { a, b ->
            a.equals(b, true)
        }
    }),

    name_startswith(FilterOpFactory1 { input ->
        WithName(Component.translatable("jef.filter_op.name_startswith"), input) { a, b ->
            a.startsWith(b, true)
        }
    }),

    name_endswith(FilterOpFactory1 { input ->
        WithName(Component.translatable("jef.filter_op.name_endswith"), input) { a, b ->
            a.endsWith(b, true)
        }
    }),

    name_contains(FilterOpFactory1 { input ->
        WithName(Component.translatable("jef.filter_op.name_contains"), input) { a, b ->
            a.contains(b, true)
        }
    }),

    name_matches(FilterOpFactory1 { input ->
        WithName(Component.translatable("jef.filter_op.name_matches"), input) { a, b ->
            a.matches(Regex(b))
        }
    }),

    id_is(JefConstants.ITEM_IDS, FilterOpFactory1 { input ->
        object : FilterOp(Component.translatable("jef.filter_op.id_is")) {
            private val item = BuiltInRegistries.ITEM.get(RLUtl.auto(input))

            override fun matches(stack: ItemStack): Boolean {
                return stack.`is`(item)
            }

            override fun matchesNonItem(obj: Any): Boolean {
                return false
                TODO()
            }
        }
    }),

    has_tag({ RLUtl.validate(it) }, FilterOpFactory1 { input ->
        object : FilterOp(Component.translatable("jef.filter_op.has_tag")) {
            override fun matches(stack: ItemStack): Boolean {
                return stack.tags.anyMatch { it.location == RLUtl.auto(input) }
            }

            override fun matchesNonItem(obj: Any): Boolean {
                return false
                TODO()
            }
        }
    }),

    has_data(JefConstants.DATA_COMPONENT_IDS, FilterOpFactory1 { input ->
        object : FilterOp(Component.translatable("jef.filter_op.has_data")) {
            private val data = BuiltInRegistries.DATA_COMPONENT_TYPE.get(RLUtl.auto(input))

            override fun matches(stack: ItemStack): Boolean {
                if (data == null) return false
                return stack.components.keySet().any { data == it }
            }

            override fun matchesNonItem(obj: Any): Boolean {
                return false
                TODO()
            }
        }
    }),

    is_fuel(FilterOpFactory0 {
        ItemOnly(Component.translatable("jef.filter_op.is_fuel")) {
            AbstractFurnaceBlockEntity.isFuel(it)
        }
    }),

    is_itemlike(FilterOpFactory0 {
        ItemOnly(Component.translatable("jef.filter_op.is_itemlike")) { true }
    }),

    is_item(FilterOpFactory0 {
        ItemOnly(Component.translatable("jef.filter_op.is_item")) { it.item !is BlockItem }
    }),

    is_block(FilterOpFactory0 {
        ItemOnly(Component.translatable("jef.filter_op.is_block")) { it.item is BlockItem }
    }),

    is_instanceof(
        {
            try {
                Class.forName(it)
                true
            } catch (_: ClassNotFoundException) {
                false
            }
        },
        FilterOpFactory1 {
            try {
                object : FilterOp(Component.translatable("jef.filter_op.is_instanceof")) {
                    private val clazz = Class.forName(it)

                    override fun matches(stack: ItemStack): Boolean {
                        return clazz.isInstance(stack.item)
                    }

                    override fun matchesNonItem(obj: Any): Boolean {
                        return clazz.isInstance(obj)
                    }
                }
            } catch (_: ClassNotFoundException) {
                object : FilterOp(Component.translatable("jef.filter_op.is_instanceof")) {
                    override fun matches(stack: ItemStack): Boolean {
                        return false
                    }

                    override fun matchesNonItem(obj: Any): Boolean {
                        return false
                    }
                }
            }
        }
    );

    constructor(factory: FilterOpFactory) : this(null, factory)
    constructor(selections: Collection<String>, factory: FilterOpFactory) : this({ selections.contains(it) }, factory)

    override fun create(input: String): FilterOp {
        return factory.create(input)
    }

    fun validate(input: String): Boolean {
        return validator?.invoke(input) ?: true
    }

    companion object {
        @JvmField
        val NAMES: Set<String> = entries.map { it.name }.sorted().toSet()
    }
}

data class FilterOpGenerator(val provider: FilterOpProvider, val input: String) : IFilter {
    fun generate(): FilterOp {
        return provider.factory.create(input)
    }

    override fun matches(stack: ItemStack): Boolean {
        return generate().matches(stack)
    }

    override fun matchesNonItem(obj: Any): Boolean {
        return generate().matchesNonItem(obj)
    }
}