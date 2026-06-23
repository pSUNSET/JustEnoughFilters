package net.psunset.jef.config

import net.minecraft.ChatFormatting
import net.minecraft.network.chat.Component
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.item.BlockItem
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.block.entity.AbstractFurnaceBlockEntity
import net.psunset.jef.api.IFilter
import net.psunset.jef.platform.Platform
import net.psunset.jef.registry.JefRegistries
import net.psunset.jef.tool.CatchingUtl
import net.psunset.jef.tool.DataComponentUtl
import net.psunset.jef.tool.RLUtl
import net.psunset.jef.util.NonItemHelper

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

/**
 * [name] will be `{id.toString()}`
 */
class FilterOpProvider : FilterOpFactory {

    val name: String
    val argDesc: ArgDesc?
    val factory: FilterOpFactory

    val tooltip: Component

    /**
     * [tooltip] defaults to `jef.filter_op.{id.namespace}.{id.path}` with an arg `argDesc.name`
     */
    constructor(id: ResourceLocation, argDesc: ArgDesc, factory1: FilterOpFactory1) : this(
        id,
        Component.translatable(
            "jef.filter_op.${id.namespace}.${id.path}",
            argDesc.name
        ),
        argDesc,
        factory1
    )

    /**
     * [tooltip] defaults to `jef.filter_op.{id.namespace}.{id.path}`
     */
    constructor(id: ResourceLocation, factory0: FilterOpFactory0) : this(
        id,
        Component.translatable("jef.filter_op.${id.namespace}.${id.path}"),
        factory0
    )

    /**
     * This constructor allows customized [tooltip]
     */
    constructor(id: ResourceLocation, tooltip: Component, argDesc: ArgDesc, factory1: FilterOpFactory1) : this(
        id.toString(),
        tooltip,
        argDesc,
        factory1
    )

    /**
     * This constructor allows customized [tooltip]
     */
    constructor(id: ResourceLocation, tooltip: Component, factory0: FilterOpFactory0) : this(
        id.toString(),
        tooltip,
        factory0
    )

    internal constructor(name: String, argDesc: ArgDesc, factory1: FilterOpFactory1) : this(
        name,
        Component.translatable("jef.filter_op.$name", argDesc.name),
        argDesc,
        factory1
    )

    internal constructor(name: String, factory0: FilterOpFactory0) : this(
        name,
        Component.translatable("jef.filter_op.$name"),
        factory0
    )

    internal constructor(name: String, tooltip: Component, argDesc: ArgDesc, factory1: FilterOpFactory1) {
        this.name = name
        this.tooltip = tooltip
        this.argDesc = argDesc
        this.factory = factory1
    }

    internal constructor(name: String, tooltip: Component, factory0: FilterOpFactory0) {
        this.name = name
        this.tooltip = tooltip
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
        /**
         * Unavailable before `ClientSetupEvent`
         */
        @JvmStatic
        val NAMES: Set<String> by lazy {
            JefRegistries.FILTER_OP_PROVIDERS.keys.sorted().toSet()
        }

        /**
         * Always available
         */
        @JvmStatic
        fun valueOf(name: String): FilterOpProvider {
            return valueOfNullable(name)!!
        }

        /**
         * Always available
         */
        @JvmStatic
        fun valueOfNullable(name: String): FilterOpProvider? {
            return JefRegistries.FILTER_OP_PROVIDERS[name]
        }

        /**
         * Always available
         */
        @JvmStatic
        fun valueOfOrUnknown(name: String): FilterOpProvider {
            return valueOfNullable(name) ?: FilterOpProvider(
                name,
                Component.translatable("jef.filter_op.unknown")
                    .withStyle(ChatFormatting.RED),
            ) { FilterOp.None }
        }
    }
}

data class FilterOpGenerator(val provider: String, val input: String) : IFilter {
    private val instance by lazy { generate() }

    private fun generate(): FilterOp {
        return FilterOpProvider.valueOfOrUnknown(provider).create(input)
    }

    override fun matches(stack: ItemStack): Boolean {
        return instance.matches(stack)
    }

    override fun matchesNonItem(obj: Any): Boolean {
        return instance.matchesNonItem(obj)
    }
}

object FilterOpProviders {

    @JvmField
    val name_is = register(
        "name_is",
        ArgDesc("name", ArgType.Str)
    ) { FilterOp.WithName(it) { a, b -> a.equals(b, true) } }

    @JvmField
    val name_startswith = register(
        "name_startswith",
        ArgDesc("prefix", ArgType.Str)
    ) { FilterOp.WithName(it) { a, b -> a.startsWith(b, true) } }

    @JvmField
    val name_endswith = register(
        "name_endswith",
        ArgDesc("suffix", ArgType.Str)
    ) { FilterOp.WithName(it) { a, b -> a.endsWith(b, true) } }

    @JvmField
    val name_contains = register(
        "name_contains",
        ArgDesc("partial", ArgType.Str)
    ) { FilterOp.WithName(it) { a, b -> a.contains(b, true) } }

    @JvmField
    val name_matches = register(
        "name_matches",
        ArgDesc("regex", ArgType.Reg)
    ) {
        if (CatchingUtl.isValidRegex(it)) {
            FilterOp.WithName(it) { a, b -> a.matches(Regex(b)) }
        } else FilterOp.None
    }

    @JvmField
    val id_is = register(
        "id_is",
        ArgDesc("id", ArgType.ItemLikeId)
    ) { FilterOp.WithId(it) { a, b -> a == b } }

    @JvmField
    val id_startswith = register(
        "id_startswith",
        ArgDesc("prefix", ArgType.PartialId)
    ) { FilterOp.WithId(it) { a, b -> a.startsWith(b) } }

    @JvmField
    val id_endswith = register(
        "id_endswith",
        ArgDesc("suffix", ArgType.PartialId)
    ) { FilterOp.WithId(it) { a, b -> a.endsWith(b) } }

    @JvmField
    val id_contains = register(
        "id_contains",
        ArgDesc("partial", ArgType.PartialId)
    ) { FilterOp.WithId(it) { a, b -> a.contains(b) } }

    @JvmField
    val id_matches = register(
        "id_matches",
        ArgDesc("regex", ArgType.Reg)
    ) {
        if (CatchingUtl.isValidRegex(it)) {
            FilterOp.WithId(it) { a, b -> a.matches(Regex(b)) }
        } else FilterOp.None
    }

    @JvmField
    val modid_is = register(
        "modid_is",
        ArgDesc("modId", ArgType.ModId)
    ) { FilterOp.WithNamespace(it) { a, b -> a == b } }

    val modname_is = register(
        "modname_is",
        ArgDesc("name", ArgType.ModName)
    ) {
        FilterOp.WithNamespace(it) { a, b ->
            Platform.getModName(a).equals(b, true)
        }
    }

    @JvmField
    val modname_contains = register(
        "modname_contains",
        ArgDesc("partial", ArgType.Str)
    ) {
        FilterOp.WithNamespace(it) { a, b ->
            Platform.getModName(a).contains(b, true)
        }
    }

    @JvmField
    val idname_is = register(
        "idname_is",
        ArgDesc("name", ArgType.Path)
    ) { FilterOp.WithPath(it) { a, b -> a == b } }

    @JvmField
    val idname_startswith = register(
        "idname_startswith",
        ArgDesc("prefix", ArgType.Path)
    ) { FilterOp.WithId(it) { a, b -> a.startsWith(b) } }

    @JvmField
    val idname_endswith = register(
        "idname_endswith",
        ArgDesc("suffix", ArgType.Path)
    ) { FilterOp.WithId(it) { a, b -> a.endsWith(b) } }

    @JvmField
    val idname_contains = register(
        "idname_contains",
        ArgDesc("partial", ArgType.Path)
    ) { FilterOp.WithId(it) { a, b -> a.contains(b) } }

    @JvmField
    val idname_matches = register(
        "idname_matches",
        ArgDesc("regex", ArgType.Reg)
    ) {
        if (CatchingUtl.isValidRegex(it)) {
            FilterOp.WithId(it) { a, b -> a.matches(Regex(b)) }
        } else FilterOp.None
    }

    @JvmField
    val has_tag = register(
        "has_tag",
        ArgDesc("tagId", ArgType.Id)
    ) {
        if (RLUtl.auto(it) == null) FilterOp.None else {
            object : FilterOp() {
                private val rl = RLUtl.auto(it)!!
                override fun matches(stack: ItemStack): Boolean {
                    return stack.tags.anyMatch { key -> key.location == rl }
                }

                override fun matchesNonItem(obj: Any): Boolean {
                    return NonItemHelper.getTags(obj)
                        .anyMatch { key -> key.location == rl }
                }
            }
        }
    }

    @JvmField
    val has_data = register(
        "has_data",
        ArgDesc("dataId", ArgType.DataId)
    ) {
        if (DataComponentUtl.validate(it)) FilterOp.None
        else {
            object : FilterOp() {
                private val data = DataComponentUtl.of(it)

                override fun matches(stack: ItemStack): Boolean {
                    return stack.components.has(data)
                }

                override fun matchesNonItem(obj: Any): Boolean {
                    return NonItemHelper.getComponents(obj).has(data)
                }
            }
        }
    }

    @JvmField
    val is_fuel = register("is_fuel") {
        FilterOp.ItemOnly { AbstractFurnaceBlockEntity.isFuel(it) }
    }

    @JvmField
    val is_itemlike = register("is_itemlike") { FilterOp.ItemOnly { true } }

    @JvmField
    val is_item = register("is_item") { FilterOp.ItemOnly { it.item !is BlockItem } }

    @JvmField
    val is_block = register("is_block") { FilterOp.ItemOnly { it.item is BlockItem } }

    @JvmField
    val is_instanceof = register(
        "is_instanceof",
        ArgDesc("cls", ArgType.Clazz)
    ) {
        if (CatchingUtl.isValidClass(it)) {
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

    @JvmStatic
    fun register(name: String, desc: ArgDesc, factory1: FilterOpFactory1): FilterOpProvider {
        return JefRegistries.FILTER_OP_PROVIDERS.register(
            name,
            FilterOpProvider(name, desc, factory1)
        )
    }

    @JvmStatic
    fun register(name: String, factory0: FilterOpFactory0): FilterOpProvider {
        return JefRegistries.FILTER_OP_PROVIDERS.register(
            name,
            FilterOpProvider(name, factory0)
        )
    }

    /**
     * Do nothing but simply run static body
     */
    @JvmStatic
    fun init() {
    }
}