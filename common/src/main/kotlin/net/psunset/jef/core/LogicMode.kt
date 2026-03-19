package net.psunset.jef.core

import net.minecraft.ChatFormatting
import net.minecraft.client.resources.language.I18n
import net.minecraft.network.chat.Component
import net.minecraft.resources.ResourceLocation
import net.psunset.jef.tool.RLUtl

enum class LogicMode(
    val translationKey: String,
    val icon: ResourceLocation,
    val combineFactory: (List<Boolean>) -> Boolean
) {
    OR(
        "jef.logic_mode.justenoughfilters.or",
        RLUtl.of("textures/gui/logic_mode/or.png"),
        { results -> results.any { it } }
    ),
    AND(
        "jef.logic_mode.justenoughfilters.and",
        RLUtl.of("textures/gui/logic_mode/and.png"),
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
