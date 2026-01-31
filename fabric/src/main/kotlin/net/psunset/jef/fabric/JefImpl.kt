package net.psunset.jef.fabric

import net.fabricmc.api.ClientModInitializer
import net.fabricmc.fabric.api.client.screen.v1.ScreenEvents
import net.fabricmc.fabric.api.client.screen.v1.ScreenMouseEvents
import net.psunset.jef.JefCore
import net.psunset.jef.JustEnoughFilters

object JefImpl : ClientModInitializer {

    override fun onInitializeClient() {
        if (!JustEnoughFilters.init()) return
        ScreenEvents.AFTER_INIT.register { client, screen, scaledWidth, scaledHeight ->
            ScreenMouseEvents.allowMouseClick(screen).register { _, mouseX, mouseY, button ->
                return@register !JefCore.mouseClicked(mouseX, mouseY, button)
            }
        }
    }
}