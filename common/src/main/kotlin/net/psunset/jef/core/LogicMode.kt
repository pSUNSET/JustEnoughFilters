package net.psunset.jef.core

import net.minecraft.ChatFormatting
import net.minecraft.client.resources.language.I18n
import net.minecraft.network.chat.Component

enum class LogicMode(
    val translationKey: String,
    val icon: Component,
    val combineFactory: (List<Boolean>) -> Boolean
) {
    OR(
        "jef.logic_mode.justenoughfilters.or",
        Component.literal("|"),
        { results -> results.any { it } }
    ),
    AND(
        "jef.logic_mode.justenoughfilters.and",
        Component.literal("&"),
        { results -> results.all { it } }
    );

    companion object {
        @JvmStatic
        val TITLE: Component = Component.translatable("jef.logic_mode.title")

        @JvmStatic
        fun genTooltip(currentMode: LogicMode): List<Component> {
            val list = mutableListOf<Component>(TITLE)
            for (mode in entries) {
                if (currentMode == mode) {
                    list.add(Component.literal("> ${I18n.get(mode.translationKey)}").withStyle(ChatFormatting.AQUA))
                } else {
                    list.add(Component.literal("  ${I18n.get(mode.translationKey)}").withStyle(ChatFormatting.GRAY))
                }
            }
            return list
        }
    }
}
