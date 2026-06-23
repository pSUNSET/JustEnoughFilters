package net.psunset.jef.config

import com.google.gson.TypeAdapter
import com.google.gson.stream.JsonReader
import com.google.gson.stream.JsonWriter
import net.minecraft.network.chat.Component
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.item.ItemStack
import net.psunset.jef.api.IToggledFilter
import net.psunset.jef.item.DummyItem
import net.psunset.jef.item.FoilItemStack
import net.psunset.jef.item.NotFoilItemStack
import net.psunset.jef.tool.ItemLikeUtl
import net.psunset.jef.tool.RLUtl

data class CustomFilter(
    val name: String,
    val icon: String,
    val ops: List<OpCombination>
) : IToggledFilter {

    override val id: ResourceLocation = genRL(name)

    val iconItem = ItemLikeUtl.tryParse(icon) ?: DummyItem.INSTANCE
    override val activeIcon: ItemStack = FoilItemStack(iconItem)
    override val inactiveIcon: ItemStack = NotFoilItemStack(iconItem)
    override val tooltip: Component = Component.literal(name)

    override fun matches(stack: ItemStack): Boolean {
        val iter = ops.listIterator()
        var op = iter.next()
        var temp: Boolean  // each op's result
        var andGroup: Boolean  // ops combined with AND operation
        var result = op.filter.matches(stack)  // final result
        if (op.unary == LogicOp.Unary.not) result = !result
        while (iter.hasNext()) {
            op = iter.next()
            if (op.bin == LogicOp.Binary.or) {
                temp = op.filter.matches(stack)
                if (op.unary == LogicOp.Unary.not) temp = !temp
                andGroup = temp
                while (iter.hasNext()) {
                    op = iter.next()
                    if (op.bin != LogicOp.Binary.and) {
                        iter.previous()
                        break
                    }
                    temp = op.filter.matches(stack)
                    if (op.unary == LogicOp.Unary.not) temp = !temp
                    andGroup = andGroup && temp
                }
                result = result || andGroup
            } else {
                temp = op.filter.matches(stack)
                if (op.unary == LogicOp.Unary.not) temp = !temp
                result = result && temp
            }
        }
        return result
    }

    override fun matchesNonItem(obj: Any): Boolean {
        val iter = ops.listIterator()
        var op = iter.next()
        var temp: Boolean
        var andGroup: Boolean
        var result = op.filter.matchesNonItem(obj)
        if (op.unary == LogicOp.Unary.not) result = !result
        while (iter.hasNext()) {
            op = iter.next()
            if (op.bin == LogicOp.Binary.or) {
                temp = op.filter.matchesNonItem(obj)
                if (op.unary == LogicOp.Unary.not) temp = !temp
                andGroup = temp
                while (iter.hasNext()) {
                    op = iter.next()
                    if (op.bin != LogicOp.Binary.and) {
                        iter.previous()
                        break
                    }
                    temp = op.filter.matchesNonItem(obj)
                    if (op.unary == LogicOp.Unary.not) temp = !temp
                    andGroup = andGroup && temp
                }
                result = result || andGroup
            } else {
                temp = op.filter.matchesNonItem(obj)
                if (op.unary == LogicOp.Unary.not) temp = !temp
                result = result && temp
            }
        }
        return result
    }

    companion object {

        @JvmStatic
        fun createDefault(filters: Collection<CustomFilter>): CustomFilter {
            var name = "New Filter"
            if (filters.any { it.id.toString() == "jef_custom:new_filter" }) {
                var i = 1
                while (filters.any { it.id.toString() == "jef_custom:new_filter__${i}_" }) {
                    i++
                }
                name = "New Filter ($i)"
            }
            return CustomFilter(
                name,
                "minecraft:grass_block",
                listOf(
                    OpCombination(
                        LogicOp.Binary.first,
                        LogicOp.Unary.so,
                        FilterOpGenerator("name_contains", "")
                    )
                )
            )
        }

        const val CUSTOM_FILTER_NAMESPACE = "jef_custom"

        @JvmStatic
        fun genRL(name: String): ResourceLocation {
            return RLUtl.of(CUSTOM_FILTER_NAMESPACE, RLUtl.toValidPath(name))
        }
    }

    internal object Adapter : TypeAdapter<CustomFilter>() {
        override fun write(writer: JsonWriter, obj: CustomFilter) {
            writer.beginObject()
                .name("name").value(obj.name)
                .name("icon").value(obj.icon)
                .name("ops")
                .beginArray()
            for (op in obj.ops) {
                writer.beginObject()
                    .name("bin").value(op.bin.name)
                    .name("unary").value(op.unary.name)
                    .name("filter")
                    .beginObject()
                    .name("provider").value(op.filter.provider)
                    .name("input").value(op.filter.input)
                    .endObject()
                    .endObject()
            }
            writer.endArray().endObject()
        }

        override fun read(reader: JsonReader): CustomFilter {
            var name: String? = null
            var icon: String? = null
            val ops = mutableListOf<OpCombination>()
            var bin: String? = null
            var unary: String? = null
            var provider: String? = null
            var input: String? = null

            reader.beginObject()
            while (reader.hasNext()) {
                when (reader.nextName()) {
                    "name" -> name = reader.nextString()
                    "icon" -> icon = reader.nextString()
                    "ops" -> {
                        reader.beginArray()
                        while (reader.hasNext()) {
                            reader.beginObject()
                            while (reader.hasNext()) {
                                when (reader.nextName()) {
                                    "bin" -> bin = reader.nextString()
                                    "unary" -> unary = reader.nextString()
                                    "filter" -> {
                                        reader.beginObject()
                                        while (reader.hasNext()) {
                                            when (reader.nextName()) {
                                                "provider" -> provider = reader.nextString()
                                                "input" -> input = reader.nextString()
                                            }
                                        }
                                        reader.endObject()
                                    }
                                }
                            }
                            reader.endObject()
                            ops.add(
                                OpCombination(
                                    LogicOp.Binary.valueOf(bin!!),
                                    LogicOp.Unary.valueOf(unary!!),
                                    FilterOpGenerator(provider!!, input!!),
                                )
                            )
                        }
                        reader.endArray()
                    }
                }
            }
            reader.endObject()
            return CustomFilter(name!!, icon!!, ops)
        }
    }
}

data class OpCombination(
    val bin: LogicOp.Binary,
    val unary: LogicOp.Unary,
    val filter: FilterOpGenerator
)