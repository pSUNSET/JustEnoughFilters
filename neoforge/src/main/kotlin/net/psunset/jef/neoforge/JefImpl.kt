package net.psunset.jef.neoforge

import net.neoforged.api.distmarker.Dist
import net.neoforged.bus.api.IEventBus
import net.neoforged.fml.ModContainer
import net.neoforged.fml.common.Mod
import net.neoforged.neoforge.client.event.ScreenEvent
import net.neoforged.neoforge.client.gui.IConfigScreenFactory
import net.neoforged.neoforge.common.NeoForge
import net.psunset.jef.JustEnoughFilters
import net.psunset.jef.gui.config.JefMainConfigScreen
import net.psunset.jef.gui.inventory.InventoryOverlayManager
import net.psunset.jef.platform.neoforge.PlatformImpl

@Mod(JustEnoughFilters.ID, dist = [Dist.CLIENT])
class JefImpl(container: ModContainer, modBus: IEventBus, dist: Dist) {

    init {
        PlatformImpl  // init it
        if (JustEnoughFilters.init()) {
            container.registerExtensionPoint(
                IConfigScreenFactory::class.java,
                IConfigScreenFactory { _, p -> JefMainConfigScreen(p) }
            )
            NeoForge.EVENT_BUS.addListener(ScreenEvent.MouseButtonPressed.Pre::class.java, ::onMouseClick)
        }
    }

    fun onMouseClick(event: ScreenEvent.MouseButtonPressed.Pre) {
        if (InventoryOverlayManager.mouseClicked(event.mouseButtonEvent, event.isDoubleClick)) {
            event.isCanceled = true
        }
    }
}