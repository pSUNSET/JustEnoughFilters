package net.psunset.jef.compat.rei

import me.shedaniel.rei.api.client.REIRuntime
import net.psunset.jef.api.IFilterProxy

object ReiFilterProxyImpl : IFilterProxy {
    override fun `jef$refresh`() {
        REIRuntime.getInstance().overlay.ifPresent { it.queueReloadOverlay() }
    }
}