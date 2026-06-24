package net.psunset.jef.gui.config.widget

import net.minecraft.client.gui.Font
import net.psunset.jef.tool.ItemLikeUtl

class ItemIdField(
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

    override fun validate(newValue: String): Boolean {
        return ItemLikeUtl.validate(newValue)
    }
}