package net.psunset.jef.gui.config.widget

import net.minecraft.client.gui.Font
import net.psunset.jef.config.FilterOpProvider

class FilterOpArgsConfigField(
    font: Font,
    x: Int,
    y: Int,
    width: Int,
    height: Int,
    val providerSupplier: () -> String,
    saveConsumer: (String) -> Unit,
) : ValidateEditBox(
    font,
    x,
    y,
    width,
    height,
    saveConsumer
) {
    constructor(
        font: Font,
        width: Int,
        height: Int,
        providerSupplier: () -> String,
        saveConsumer: (String) -> Unit
    ) : this(
        font,
        0,
        0,
        width,
        height,
        providerSupplier,
        saveConsumer
    )

    override fun validate(newValue: String): Boolean {
        return FilterOpProvider.valueOfOrUnknown(
            providerSupplier.invoke()
        ).validate(newValue)
    }
}