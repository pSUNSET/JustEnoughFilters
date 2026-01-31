package net.psunset.jef.neoforge

import net.neoforged.api.distmarker.Dist
import net.neoforged.fml.common.Mod
import net.neoforged.neoforge.client.event.ScreenEvent
import net.neoforged.neoforge.common.NeoForge
import net.psunset.jef.JefCore
import net.psunset.jef.JustEnoughFilters

@Mod(JustEnoughFilters.ID, dist = [Dist.CLIENT])
object JefImpl {

    init {
        if (JustEnoughFilters.init()) {
            NeoForge.EVENT_BUS.addListener(ScreenEvent.MouseButtonPressed.Pre::class.java, ::onMouseClick)
        }
    }

    fun onMouseClick(event: ScreenEvent.MouseButtonPressed.Pre) {
        if (JefCore.mouseClicked(event.mouseX, event.mouseY, event.button)) {
            event.isCanceled = true
        }
    }
}