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

enum class FilterOpProvider : FilterOpFactory {
    name_is(
        ArgDesc("name", "String"),
        {
            WithName(it) { a, b ->
                a.equals(b, true)
            }
        }),

    name_startswith(
        ArgDesc("prefix", "String"),
        {
            WithName(it) { a, b ->
                a.startsWith(b, true)
            }
        }),

    name_endswith(
        ArgDesc("suffix", "String"),
        {
            WithName(it) { a, b ->
                a.endsWith(b, true)
            }
        }),

    name_contains(
        ArgDesc("text", "String"),
        {
            WithName(it) { a, b ->
                a.contains(b, true)
            }
        }),

    name_matches(
        ArgDesc("regex", "Regex"),
        {
            WithName(it) { a, b ->
                a.matches(Regex(b))
            }
        }),

    id_is(
        ArgDesc("itemId", "Identifier", JefConstants.ITEM_IDS),
        {
            object : FilterOp() {
                private val item = BuiltInRegistries.ITEM.get(RLUtl.auto(it))

                override fun matches(stack: ItemStack): Boolean {
                    return stack.`is`(item)
                }

                override fun matchesNonItem(obj: Any): Boolean {
                    return false
                    TODO()
                }
            }
        }),

    has_tag(
        ArgDesc("tagId", "Identifier") { RLUtl.validate(it) },
        {
            object : FilterOp() {
                override fun matches(stack: ItemStack): Boolean {
                    return stack.tags.anyMatch { rl -> rl.location == RLUtl.auto(it) }
                }

                override fun matchesNonItem(obj: Any): Boolean {
                    return false
                    TODO()
                }
            }
        }),

    has_data(
        ArgDesc("dataId", "Identifier", JefConstants.DATA_COMPONENT_IDS),
        {
            object : FilterOp() {
                private val data = BuiltInRegistries.DATA_COMPONENT_TYPE.get(RLUtl.auto(it))

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

    is_fuel({
        ItemOnly { AbstractFurnaceBlockEntity.isFuel(it) }
    }),

    is_itemlike({
        ItemOnly { true }
    }),

    is_item({
        ItemOnly { it.item !is BlockItem }
    }),

    is_block({
        ItemOnly { it.item is BlockItem }
    }),

    is_instanceof(
        ArgDesc("cls", "Class") {
            try {
                Class.forName(it)
                true
            } catch (_: ClassNotFoundException) {
                false
            }
        },
        {
            try {
                object : FilterOp() {
                    private val clazz = Class.forName(it)

                    override fun matches(stack: ItemStack): Boolean {
                        return clazz.isInstance(stack.item)
                    }

                    override fun matchesNonItem(obj: Any): Boolean {
                        return clazz.isInstance(obj)
                    }
                }
            } catch (_: ClassNotFoundException) {
                object : FilterOp() {
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

    val argDesc: ArgDesc?
    val factory: FilterOpFactory

    private val tooltipKey = "jef.filter_op.$name"
    val tooltip: Component
        get() = if (argDesc != null) {
            Component.translatable(tooltipKey, argDesc.name)
        } else {
            Component.translatable(tooltipKey)
        }

    constructor(argDesc: ArgDesc, factory1: FilterOpFactory1) {
        this.argDesc = argDesc
        this.factory = factory1
    }

    constructor(factory0: FilterOpFactory0) {
        this.argDesc = null
        this.factory = factory0
    }

    override fun create(input: String): FilterOp {
        return factory.create(input)
    }

    fun validate(input: String): Boolean {
        return argDesc?.validate(input) ?: false
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

class ArgDesc(val name: String, val type: String, val validator: ((String) -> Boolean)?) {
    constructor(name: String, type: String) : this(name, type, null)
    constructor(name: String, type: String, selections: Collection<String>) :
            this(name, type, { selections.contains(it) })

    fun validate(input: String): Boolean {
        return validator?.invoke(input) ?: true
    }

    override fun toString(): String {
        return "$name: $type"
    }
}