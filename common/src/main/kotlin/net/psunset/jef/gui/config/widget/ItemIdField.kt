package net.psunset.jef.gui.config.widget

import net.minecraft.client.gui.Font
import net.psunset.jef.util.JefConstants

class ItemIdField(
    font: Font,
    x: Int,
    y: Int,
    width: Int,
    height: Int,
    saveConsumer: (String) -> Unit
) : SelectionsEditBox(
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

    override val selections: Collection<String> = JefConstants.ITEM_IDS
}