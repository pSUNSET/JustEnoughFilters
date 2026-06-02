package net.psunset.jef.tool

import net.psunset.jef.JustEnoughFilters
import net.psunset.jef.platform.Platform
import java.nio.file.Path

object PathUtl {
    @JvmStatic
    fun jefConfigDir(): Path {
        return Platform.configDir().resolve(JustEnoughFilters.ID)
    }
}