package net.psunset.jef.forge

import net.psunset.jef.JefMixinPlugin
import net.psunset.jef.platform.forge.PlatformImpl

class JefMixinPluginImpl : JefMixinPlugin() {
    companion object {
        init {
            PlatformImpl  // init it
        }
    }
}