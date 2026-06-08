package net.psunset.jef.config.element

import net.minecraft.client.resources.language.I18n
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.network.chat.Component
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.item.BlockItem
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.block.entity.AbstractFurnaceBlockEntity
import net.psunset.jef.api.IFilter
import net.psunset.jef.platform.Platform
import net.psunset.jef.tool.DataComponentUtl
import net.psunset.jef.tool.ItemUtl
import net.psunset.jef.tool.RLUtl
import net.psunset.jef.tool.idToString
import net.psunset.jef.tool.toId
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
        ArgDesc("name", ArgType.Str),
        { FilterOp.WithName(it) { a, b -> a.equals(b, true) } }
    ),

    name_startswith(
        ArgDesc("prefix", ArgType.Str),
        { FilterOp.WithName(it) { a, b -> a.startsWith(b, true) } }
    ),

    name_endswith(
        ArgDesc("suffix", ArgType.Str),
        { FilterOp.WithName(it) { a, b -> a.endsWith(b, true) } }
    ),

    name_contains(
        ArgDesc("partial", ArgType.Str),
        { FilterOp.WithName(it) { a, b -> a.contains(b, true) } }
    ),

    name_matches(
        ArgDesc("regex", ArgType.Reg),
        { FilterOp.WithName(it) { a, b -> a.matches(Regex(b)) } }
    ),

    id_is(
        ArgDesc("id", ArgType.ItemId),
        {
            if (ItemUtl.validate(it)) {
                object : FilterOp() {
                    private val item = ItemUtl.of(it)

                    override fun matches(stack: ItemStack): Boolean {
                        return stack.`is`(item)
                    }

                    override fun matchesNonItem(obj: Any): Boolean {
                        return false
                        TODO()
                    }
                }
            } else FilterOp.None
        }
    ),

    id_startswith(
        ArgDesc("prefix", ArgType.PartialId),
        { FilterOp.WithId(it) { a, b -> a.startsWith(b) } }
    ),

    id_endswith(
        ArgDesc("suffix", ArgType.PartialId),
        { FilterOp.WithId(it) { a, b -> a.endsWith(b) } }
    ),

    id_contains(
        ArgDesc("partial", ArgType.PartialId),
        { FilterOp.WithId(it) { a, b -> a.contains(b) } }
    ),

    id_matches(
        ArgDesc("regex", ArgType.Reg),
        { FilterOp.WithId(it) { a, b -> a.matches(Regex(b)) } }
    ),

    modid_is(
        ArgDesc("modId", ArgType.ModId),
        { FilterOp.WithNamespace(it) { a, b -> a == b } }
    ),

    modname_is(
        ArgDesc("name", ArgType.ModName),
        {
            FilterOp.WithNamespace(it) { a, b ->
                Platform.getModName(a).equals(b, true)
            }
        }
    ),

    modname_contains(
        ArgDesc("partial", ArgType.Str),
        {
            FilterOp.WithNamespace(it) { a, b ->
                Platform.getModName(a).contains(b, true)
            }
        }
    ),

    idname_is(
        ArgDesc("name", ArgType.Path),
        { FilterOp.WithPath(it) { a, b -> a == b } }
    ),

    idname_startswith(
        ArgDesc("prefix", ArgType.Path),
        { FilterOp.WithId(it) { a, b -> a.startsWith(b) } }
    ),

    idname_endswith(
        ArgDesc("suffix", ArgType.Path),
        { FilterOp.WithId(it) { a, b -> a.endsWith(b) } }
    ),

    idname_contains(
        ArgDesc("partial", ArgType.Path),
        { FilterOp.WithId(it) { a, b -> a.contains(b) } }
    ),

    idname_matches(
        ArgDesc("regex", ArgType.Reg),
        { FilterOp.WithId(it) { a, b -> a.matches(Regex(b)) } }
    ),

    has_tag(
        ArgDesc("tagId", ArgType.Id),
        {
            if (RLUtl.auto(it) == null) FilterOp.None else {
                object : FilterOp() {
                    private val rl = RLUtl.auto(it)!!
                    override fun matches(stack: ItemStack): Boolean {
                        return stack.tags.anyMatch { key -> key.location == rl }
                    }

                    override fun matchesNonItem(obj: Any): Boolean {
                        return false
                        TODO()
                    }
                }
            }
        }
    ),

    // Data Component doesn't exist in 1.20.1
//    has_data(
//        ArgDesc("dataId", ArgType.DataId),
//        {
//            if (DataComponentUtl.validate(it)) FilterOp.None
//            else {
//                object : FilterOp() {
//                    private val data = DataComponentUtl.of(it)
//
//                    override fun matches(stack: ItemStack): Boolean {
//                        return stack.components.has(data)
//                    }
//
//                    override fun matchesNonItem(obj: Any): Boolean {
//                        return false
//                        TODO()
//                    }
//                }
//            }
//        }
//    ),

    is_fuel({ FilterOp.ItemOnly { AbstractFurnaceBlockEntity.isFuel(it) } }),

    is_itemlike({ FilterOp.ItemOnly { true } }),

    is_item({ FilterOp.ItemOnly { it.item !is BlockItem } }),

    is_block({ FilterOp.ItemOnly { it.item is BlockItem } }),

    is_instanceof(
        ArgDesc("cls", ArgType.Clazz),
        {
            if (runCatching { Class.forName(it) }.isSuccess) {
                object : FilterOp() {
                    private val clazz = Class.forName(it)

                    override fun matches(stack: ItemStack): Boolean {
                        return clazz.isInstance(stack.item)
                    }

                    override fun matchesNonItem(obj: Any): Boolean {
                        return clazz.isInstance(obj)
                    }
                }
            } else FilterOp.None
        }
    );

    val argDesc: ArgDesc?
    val factory: FilterOpFactory

    private val tooltipKey = "jef.filter_op.$name"
    val tooltip: Component

    constructor(argDesc: ArgDesc, factory1: FilterOpFactory1) {
        this.argDesc = argDesc
        this.factory = factory1
        this.tooltip = Component.translatable(tooltipKey, this.argDesc.name)
    }

    constructor(factory0: FilterOpFactory0) {
        this.argDesc = null
        this.factory = factory0
        this.tooltip = Component.translatable(tooltipKey)
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
    private val instance by lazy { generate() }

    private fun generate(): FilterOp {
        return provider.factory.create(input)
    }

    override fun matches(stack: ItemStack): Boolean {
        return instance.matches(stack)
    }

    override fun matchesNonItem(obj: Any): Boolean {
        return instance.matchesNonItem(obj)
    }
}

class ArgDesc(val name: String, val type: ArgType) {

    fun validate(input: String): Boolean {
        return type.validator.invoke(input)
    }

    override fun toString(): String {
        return "$name: ${type.displayName}"
    }
}

enum class ArgType(val displayName: String, val validator: ((String) -> Boolean)) {
    Str("String", { true }),
    Id("Id", { RLUtl.validate(it) }),
    PartialId("Id.Partial", { RLUtl.validatePartial(it) }),
    ItemId("Id", JefConstants.ITEM_IDS),
//    DataId("Id", JefConstants.DATA_COMPONENT_IDS),
    Namespace("Id.Namesapce", { RLUtl.isValidNamespace(it) }),
    ModId("Id.Namespace", JefConstants.MOD_ID_LIST),
    Path("Id.Path", { RLUtl.isValidPath(it) }),
    ModName("String", JefConstants.MOD_NAME_LIST, true),
    Reg("Regex", { runCatching { Regex(it) }.isSuccess }),
    Clazz("Class", { runCatching { Class.forName(it) }.isSuccess });

    constructor(
        displayName: String,
        selections: Collection<String>,
        ignoreCase: Boolean = false
    ) : this(
        displayName,
        validator = if (ignoreCase) {
            { selections.any { _it -> _it.equals(it, ignoreCase = true) } }
        } else {
            { selections.contains(it) }
        }
    )
}