package net.psunset.jef.api

import net.psunset.jef.core.FilterManager

/**
 * An AI-gen interface for filter proxies to implement refresh functionality.
 * I don't think this is a good name, but I have no better idea at the moment.
 *
 * Use [FilterManager.registerProxy] to register self.
 */
interface IFilterProxy {

    /**
     * Be fired when the filter needs to refresh its state.
     * Often, this means the window got resized.
     */
    fun `jef$refresh`()
}