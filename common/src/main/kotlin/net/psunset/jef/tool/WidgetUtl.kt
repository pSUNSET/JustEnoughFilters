package net.psunset.jef.tool

import net.minecraft.client.gui.components.AbstractWidget

object WidgetUtl {
}

val AbstractWidget.right: Int get() = x + width
val AbstractWidget.bottom: Int get() = y + height