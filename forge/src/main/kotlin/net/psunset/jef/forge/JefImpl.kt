package net.psunset.jef.forge

import net.minecraftforge.client.event.ScreenEvent
import net.minecraftforge.fml.common.Mod
import net.minecraftforge.fml.loading.FMLLoader
import net.psunset.jef.JefCore
import net.psunset.jef.JustEnoughFilters
import thedarkcolour.kotlinforforge.forge.FORGE_BUS

@Mod(JustEnoughFilters.ID)
object JefImpl {

    init {
        if (FMLLoader.getDist().isClient) {
            if (JustEnoughFilters.init()) {
                FORGE_BUS.addListener<ScreenEvent.MouseButtonPressed.Pre>(::onMouseClick)
            }
        }
    }

    fun onMouseClick(event: ScreenEvent.MouseButtonPressed.Pre) {
        if (JefCore.mouseClicked(event.mouseX, event.mouseY, event.button)) {
            event.isCanceled = true
        }
    }
}