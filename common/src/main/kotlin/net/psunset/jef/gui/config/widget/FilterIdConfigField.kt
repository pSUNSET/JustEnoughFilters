package net.psunset.jef.gui.config.widget

import net.minecraft.client.Minecraft
import net.psunset.jef.builtin.FilterManager

class FilterIdConfigField(
    minecraft: Minecraft,
    x: Int,
    y: Int,
    width: Int,
    height: Int,
    saveConsumer: (String) -> Unit,
) : DropDownEditBox(
    minecraft,
    x,
    y,
    width,
    height,
    saveConsumer
) {
    constructor(minecraft: Minecraft, width: Int, height: Int, saveConsumer: (String) -> Unit) : this(
        minecraft,
        0,
        0,
        width,
        height,
        saveConsumer
    )

    override val selections: Collection<String>
        get() = FilterManager.allToggledFilters.map { it.id.toString() }
}