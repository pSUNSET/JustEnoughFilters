package net.psunset.jef.gui.config.widget

import net.minecraft.client.Minecraft
import net.minecraft.client.gui.components.Tooltip
import net.psunset.jef.config.FilterOpProvider

class FilterOpConfigField(
    minecraft: Minecraft,
    x: Int,
    y: Int,
    width: Int,
    height: Int,
    private val saveConsumer: (String, FilterOpProvider) -> Unit,
) : DropDownEditBox(
    minecraft,
    x,
    y,
    width,
    height,
    null
) {
    constructor(minecraft: Minecraft, width: Int, height: Int, saveConsumer: (String, FilterOpProvider) -> Unit) : this(
        minecraft,
        0,
        0,
        width,
        height,
        saveConsumer
    )

    override val selections: Collection<String> = FilterOpProvider.NAMES

    override fun onSave(newValue: String) {
        val provider = FilterOpProvider.valueOf(newValue)
        setTooltip(Tooltip.create(provider.tooltip))
        saveConsumer(newValue, provider)
    }
}