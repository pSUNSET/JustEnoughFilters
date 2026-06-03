package net.psunset.jef.gui.config.widget

import net.minecraft.client.gui.Font

abstract class SelectionsEditBox(
    font: Font,
    x: Int,
    y: Int,
    width: Int,
    height: Int,
    saveConsumer: (String) -> Unit
) : ValidateEditBox(
    font,
    x,
    y,
    width,
    height,
    saveConsumer
) {
    constructor(font: Font, width: Int, height: Int, saveConsumer: (String) -> Unit) : this(
        font,
        0,
        0,
        width,
        height,
        saveConsumer
    )

    abstract val selections: Collection<String>

    override fun validate(newValue: String): Boolean {
        return selections.contains(newValue)
    }
}