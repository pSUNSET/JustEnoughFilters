package net.psunset.jef.config

import net.minecraft.ChatFormatting
import net.minecraft.network.chat.Component
import net.minecraft.resources.ResourceLocation
import net.psunset.jef.tool.RLUtl

// It should be a sealed class, but enum cannot inherit from abstract class.
interface LogicOp {

    val sprite: ResourceLocation?

    enum class Binary(override val sprite: ResourceLocation?) : LogicOp {
        first(null),
        and(RLUtl.ofJef("textures/gui/and.png")),
        or(RLUtl.ofJef("textures/gui/or.png"));

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

    enum class Unary(override val sprite: ResourceLocation?) : LogicOp {
        so(null),
        not(RLUtl.ofJef("textures/gui/not.png"));

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