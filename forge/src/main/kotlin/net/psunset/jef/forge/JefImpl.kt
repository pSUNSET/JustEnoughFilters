package net.psunset.jef.forge

import net.minecraft.core.registries.Registries
import net.minecraft.world.item.Item
import net.minecraftforge.client.ConfigScreenHandler
import net.minecraftforge.client.event.ScreenEvent
import net.minecraftforge.fml.common.Mod
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent
import net.minecraftforge.fml.loading.FMLLoader
import net.minecraftforge.registries.DeferredRegister
import net.psunset.jef.JustEnoughFilters
import net.psunset.jef.gui.config.JefMainConfigScreen
import net.psunset.jef.gui.inventory.InventoryOverlayManager
import net.psunset.jef.item.DummyItem
import net.psunset.jef.platform.forge.PlatformImpl
import thedarkcolour.kotlinforforge.forge.FORGE_BUS
import thedarkcolour.kotlinforforge.forge.LOADING_CONTEXT
import thedarkcolour.kotlinforforge.forge.MOD_BUS
import thedarkcolour.kotlinforforge.forge.registerObject

@Mod(JustEnoughFilters.ID)
object JefImpl {

    @JvmField
    val ITEM_REGISTRY: DeferredRegister<Item> = DeferredRegister.create(Registries.ITEM, JustEnoughFilters.ID)

    @JvmStatic
    val DUMMY_ITEM: Item by ITEM_REGISTRY.registerObject("dummy") { Item(Item.Properties()) }

    init {
        PlatformImpl  // init it

        if (FMLLoader.getDist().isClient) {
            if (JustEnoughFilters.preInit()) {

                LOADING_CONTEXT.registerExtensionPoint(ConfigScreenHandler.ConfigScreenFactory::class.java) {
                    ConfigScreenHandler.ConfigScreenFactory { _, modsScreen -> JefMainConfigScreen(modsScreen) }
                }

                FORGE_BUS.addListener<ScreenEvent.MouseButtonPressed.Pre>(::onMouseClick)
                MOD_BUS.addListener<FMLClientSetupEvent>(::onClientSetup)

                ITEM_REGISTRY.register(MOD_BUS)

                JustEnoughFilters.init()
            }
        }
    }

    fun onMouseClick(event: ScreenEvent.MouseButtonPressed.Pre) {
        if (InventoryOverlayManager.mouseClicked(event.mouseX, event.mouseY, event.button)) {
            event.isCanceled = true
        }
    }

    fun onClientSetup(event: FMLClientSetupEvent) {
        DummyItem._inst = DUMMY_ITEM
        event.enqueueWork {
            JustEnoughFilters.postInit()
        }
    }
}