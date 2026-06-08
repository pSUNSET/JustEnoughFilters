package net.psunset.jef.gui.config.widget

import net.minecraft.client.gui.components.Button
import net.minecraft.client.gui.components.Tooltip
import net.minecraft.network.chat.Component

class AddButton(
    x: Int,
    y: Int,
    onPress: OnPress,
) : Button.Plain(
    x,
    y,
    DEFAULT_HEIGHT,
    DEFAULT_HEIGHT,
    Component.literal("+"),
    onPress,
    DEFAULT_NARRATION
) {
    constructor(onPress: OnPress) : this(0, 0, onPress)

    init {
        setTooltip(Tooltip.create(Component.translatable("gui.button.justenoughfilters.add.tooltip")))
    }
}