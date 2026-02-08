package net.psunset.jef.neoforge

import net.psunset.jef.JefMixinPlugin
import net.psunset.jef.platform.neoforge.PlatformImpl

class JefMixinPluginImpl : JefMixinPlugin() {
    companion object {
        init {
            PlatformImpl  // init it
        }
    }
}