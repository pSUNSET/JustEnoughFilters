package net.psunset.jef.fabric

import net.fabricmc.api.ClientModInitializer
import net.fabricmc.fabric.api.client.screen.v1.ScreenEvents
import net.fabricmc.fabric.api.client.screen.v1.ScreenMouseEvents
import net.psunset.jef.gui.JefOverlayManager
import net.psunset.jef.JustEnoughFilters
import net.psunset.jef.platform.fabric.PlatformImpl

object JefImpl : ClientModInitializer {

    init {
        PlatformImpl // init it
    }

    override fun onInitializeClient() {
        if (!JustEnoughFilters.init()) return
        ScreenEvents.AFTER_INIT.register { client, screen, scaledWidth, scaledHeight ->
            ScreenMouseEvents.allowMouseClick(screen).register { _, context ->
                !JefOverlayManager.mouseClicked(context, false)
            }
        }
    }
}