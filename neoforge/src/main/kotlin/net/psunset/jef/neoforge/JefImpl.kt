package net.psunset.jef.neoforge

import net.minecraft.world.item.Item
import net.neoforged.api.distmarker.Dist
import net.neoforged.bus.api.IEventBus
import net.neoforged.fml.ModContainer
import net.neoforged.fml.common.Mod
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent
import net.neoforged.neoforge.client.event.ScreenEvent
import net.neoforged.neoforge.client.gui.IConfigScreenFactory
import net.neoforged.neoforge.common.NeoForge
import net.neoforged.neoforge.registries.DeferredRegister
import net.psunset.jef.JustEnoughFilters
import net.psunset.jef.config.ConfigManager
import net.psunset.jef.gui.config.JefMainConfigScreen
import net.psunset.jef.gui.inventory.InventoryOverlayManager
import net.psunset.jef.item.DummyItem
import net.psunset.jef.platform.neoforge.PlatformImpl
import thedarkcolour.kotlinforforge.neoforge.forge.MOD_BUS
import thedarkcolour.kotlinforforge.neoforge.forge.getValue

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
            MOD_BUS.addListener(FMLClientSetupEvent::class.java, ::onClientSetup)
            ITEM_REGISTRY.register(MOD_BUS)
        }
    }

    fun onMouseClick(event: ScreenEvent.MouseButtonPressed.Pre) {
        if (InventoryOverlayManager.mouseClicked(event.mouseButtonEvent, event.isDoubleClick)) {
            event.isCanceled = true
        }
    }

    fun onClientSetup(event: FMLClientSetupEvent) {
        DummyItem._inst = DUMMY_ITEM
        event.enqueueWork {
            JustEnoughFilters.postInit()
        }
    }

    companion object {
        @JvmField
        val ITEM_REGISTRY: DeferredRegister.Items = DeferredRegister.createItems(JustEnoughFilters.ID)

        val DUMMY_ITEM: Item by ITEM_REGISTRY.registerSimpleItem("dummy")
    }
}