package net.psunset.jef.config.element

import net.minecraft.ChatFormatting
import net.minecraft.network.chat.Component

// It should be an abstract class, but enum cannot inherit from abstract class.
interface LogicOp {

    val pattern: String

    enum class Binary(override val pattern: String) : LogicOp {
        first("_"),
        and("&&"),
        or("||");

        companion object {
            @JvmStatic
            val TITLE: Component = Component.translatable("gui.justenoughfilters.logic_op.bin.title")

            @JvmStatic
            fun genTooltip(currentOp: Binary): Component {
                val list = TITLE.copy()
                for (op in entries) {
                    if (op == first) continue
                    if (currentOp == op) {
                        list.append(Component.literal("\n> ${op.name.uppercase()}").withStyle(ChatFormatting.AQUA))
                    } else {
                        list.append(Component.literal("\n  ${op.name.uppercase()}").withStyle(ChatFormatting.GRAY))
                    }
                }
                return list
            }
        }
    }

    enum class Unary(override val pattern: String) : LogicOp {
        so("_"),
        not("!");

        companion object {
            @JvmStatic
            val TITLE: Component = Component.translatable("gui.justenoughfilters.logic_op.unary.title")

            @JvmStatic
            fun genTooltip(currentOp: Unary): Component {
                val list = TITLE.copy()
                for (op in entries) {
                    if (currentOp == op) {
                        list.append(Component.literal("\n> ${op.name.uppercase()}").withStyle(ChatFormatting.AQUA))
                    } else {
                        list.append(Component.literal("\n  ${op.name.uppercase()}").withStyle(ChatFormatting.GRAY))
                    }
                }
                return list
            }
        }
    }
}