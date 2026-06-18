package net.psunset.jef.builtin

import net.minecraft.ChatFormatting
import net.minecraft.client.resources.language.I18n
import net.minecraft.network.chat.Component
import net.minecraft.resources.Identifier
import net.psunset.jef.tool.IdUtl

enum class LogicMode(
    val icon: Identifier,
    val combineFactory: (List<Boolean>) -> Boolean
) {
    OR(
        IdUtl.ofJef("textures/gui/or.png"),
        { results -> results.any { it } }
    ),
    AND(
        IdUtl.ofJef("textures/gui/and.png"),
        { results -> results.all { it } }
    );

    companion object {
        @JvmStatic
        val TITLE: Component = Component.translatable("gui.justenoughfilters.logic_mode.title")

        @JvmStatic
        fun genTooltip(currentMode: LogicMode): Component {
            val list = TITLE.copy()
            for (mode in entries) {
                if (currentMode == mode) {
                    list.append(Component.literal("\n> ${I18n.get(mode.name)}").withStyle(ChatFormatting.AQUA))
                } else {
                    list.append(Component.literal("\n  ${I18n.get(mode.name)}").withStyle(ChatFormatting.GRAY))
                }
            }
            return list
        }

        @JvmStatic
        fun genTooltip(): Component {
            return genTooltip(FilterManager.logicMode)
        }
    }
}
