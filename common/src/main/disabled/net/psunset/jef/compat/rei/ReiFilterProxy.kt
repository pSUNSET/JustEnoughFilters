package net.psunset.jef.compat.rei

import me.shedaniel.rei.api.client.REIRuntime
import net.psunset.jef.api.IFilterProxy

// When REI update to mc26.1 :(
object ReiFilterProxy : IFilterProxy {
    override fun `jef$refresh`() {
        REIRuntime.getInstance().overlay.ifPresent { it.queueReloadOverlay() }
    }
}