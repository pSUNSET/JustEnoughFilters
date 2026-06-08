package net.psunset.jef.config.element

import net.minecraft.ChatFormatting
import net.minecraft.network.chat.Component
import net.minecraft.resources.Identifier
import net.psunset.jef.tool.IdUtl

// It should be a sealed class, but enum cannot inherit from abstract class.
interface LogicOp {

    val sprite: Identifier?

    enum class Binary(override val sprite: Identifier?) : LogicOp {
        first(null),
        and(IdUtl.ofJef("textures/gui/and.png")),
        or(IdUtl.ofJef("textures/gui/or.png"));

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

    enum class Unary(override val sprite: Identifier?) : LogicOp {
        so(null),
        not(IdUtl.ofJef("textures/gui/not.png"));

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