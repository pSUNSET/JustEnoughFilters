package net.psunset.jef.forge

import net.minecraftforge.client.ConfigScreenHandler
import net.minecraftforge.client.event.ScreenEvent
import net.minecraftforge.fml.ModLoadingContext
import net.minecraftforge.fml.common.Mod
import net.minecraftforge.fml.loading.FMLLoader
import net.psunset.jef.JustEnoughFilters
import net.psunset.jef.gui.config.JefMainConfigScreen
import net.psunset.jef.gui.inventory.InventoryOverlayManager
import net.psunset.jef.platform.forge.PlatformImpl
import thedarkcolour.kotlinforforge.forge.FORGE_BUS
import thedarkcolour.kotlinforforge.forge.LOADING_CONTEXT

@Mod(JustEnoughFilters.ID)
object JefImpl {

    init {
        PlatformImpl  // init it
        if (FMLLoader.getDist().isClient) {
            if (JustEnoughFilters.init()) {
                FORGE_BUS.addListener<ScreenEvent.MouseButtonPressed.Pre>(::onMouseClick)

                LOADING_CONTEXT.registerExtensionPoint(ConfigScreenHandler.ConfigScreenFactory::class.java) {
                    ConfigScreenHandler.ConfigScreenFactory { _, modsScreen -> JefMainConfigScreen(modsScreen) }
                }
            }
        }
    }

    fun onMouseClick(event: ScreenEvent.MouseButtonPressed.Pre) {
        if (InventoryOverlayManager.mouseClicked(event.mouseX, event.mouseY, event.button)) {
            event.isCanceled = true
        }
    }
}