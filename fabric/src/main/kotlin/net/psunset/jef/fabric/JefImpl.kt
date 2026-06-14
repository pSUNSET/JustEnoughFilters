package net.psunset.jef.fabric

import net.fabricmc.api.ClientModInitializer
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientLifecycleEvents
import net.fabricmc.fabric.api.client.screen.v1.ScreenEvents
import net.fabricmc.fabric.api.client.screen.v1.ScreenMouseEvents
import net.minecraft.core.Registry
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.world.item.Item
import net.psunset.jef.gui.inventory.InventoryOverlayManager
import net.psunset.jef.JustEnoughFilters
import net.psunset.jef.config.ConfigManager
import net.psunset.jef.item.DummyItem
import net.psunset.jef.platform.fabric.PlatformImpl
import net.psunset.jef.tool.RLUtl

object JefImpl : ClientModInitializer {

    init {
        PlatformImpl // init it
    }

    override fun onInitializeClient() {
        if (!JustEnoughFilters.init()) return

        ScreenEvents.AFTER_INIT.register { client, screen, scaledWidth, scaledHeight ->
            ScreenMouseEvents.allowMouseClick(screen).register { _, mouseX, mouseY, button ->
                !InventoryOverlayManager.mouseClicked(mouseX, mouseY, button)
            }
        }

        ClientLifecycleEvents.CLIENT_STARTED.register {
            JustEnoughFilters.postInit()
        }

        DummyItem._inst = Registry.register(
            BuiltInRegistries.ITEM,
            RLUtl.ofJef("dummy"),
            Item(Item.Properties())
        )
    }
}