package net.psunset.jef.neoforge

import net.neoforged.api.distmarker.Dist
import net.neoforged.fml.common.Mod
import net.neoforged.neoforge.client.event.ScreenEvent
import net.neoforged.neoforge.common.NeoForge
import net.psunset.jef.gui.JefOverlayManager
import net.psunset.jef.JustEnoughFilters
import net.psunset.jef.platform.neoforge.PlatformImpl

@Mod(JustEnoughFilters.ID, dist = [Dist.CLIENT])
object JefImpl {

    init {
        PlatformImpl  // init it
        if (JustEnoughFilters.init()) {
            NeoForge.EVENT_BUS.addListener(ScreenEvent.MouseButtonPressed.Pre::class.java, ::onMouseClick)
        }
    }

    fun onMouseClick(event: ScreenEvent.MouseButtonPressed.Pre) {
        if (JefOverlayManager.mouseClicked(event.mouseButtonEvent, event.isDoubleClick)) {
            event.isCanceled = true
        }
    }
}