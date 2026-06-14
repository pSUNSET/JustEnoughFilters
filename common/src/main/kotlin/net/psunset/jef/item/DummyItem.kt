package net.psunset.jef.item

import net.minecraft.world.item.Item
import org.jetbrains.annotations.ApiStatus

object DummyItem {
    /**
     * Registered by Fabric and NeoForge Registration
     */
    @ApiStatus.Internal
    @JvmField
    var _inst: Item? = null

    val INSTANCE: Item get() = _inst!!
}