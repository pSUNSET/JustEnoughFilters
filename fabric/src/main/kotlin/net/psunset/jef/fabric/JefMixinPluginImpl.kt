package net.psunset.jef.fabric

import net.psunset.jef.JefMixinPlugin
import net.psunset.jef.platform.fabric.PlatformImpl

class JefMixinPluginImpl : JefMixinPlugin() {
    companion object {
        init {
            PlatformImpl  // init it
        }
    }
}