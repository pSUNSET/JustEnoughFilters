package net.psunset.jef.gui.config.widget

import net.minecraft.ChatFormatting
import net.minecraft.client.gui.Font
import net.minecraft.client.gui.components.EditBox
import net.minecraft.network.chat.CommonComponents

/**
 * An EditBox that validates the input and changes text color accordingly.
 *
 * [saveConsumer] is called whenever the text changes, regardless of validity.
 */
abstract class ValidateEditBox(
    font: Font,
    x: Int,
    y: Int,
    width: Int,
    height: Int,
    saveConsumer: (String) -> Unit
) : EditBox(
    font,
    x,
    y,
    width,
    height,
    CommonComponents.EMPTY
) {
    constructor(font: Font, width: Int, height: Int, saveConsumer: (String) -> Unit) : this(
        font,
        0,
        0,
        width,
        height,
        saveConsumer
    )

    init {
        setResponder { newValue ->
            if (validate(newValue)) {
                setTextColor(-2039584)
            } else {
                setTextColor(-43691)
            }
            saveConsumer(newValue)
        }
    }

    abstract fun validate(newValue: String): Boolean
}