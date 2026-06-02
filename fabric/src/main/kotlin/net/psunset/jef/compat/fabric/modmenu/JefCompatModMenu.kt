package net.psunset.jef.compat.fabric.modmenu

import com.terraformersmc.modmenu.api.ConfigScreenFactory
import com.terraformersmc.modmenu.api.ModMenuApi
import net.psunset.jef.JustEnoughFilters
import net.psunset.jef.gui.config.JefMainConfigScreen

class JefCompatModMenu : ModMenuApi {
    override fun getModConfigScreenFactory(): ConfigScreenFactory<JefMainConfigScreen?> {
        return ConfigScreenFactory {
            if (JustEnoughFilters.isActive == true) {
                JefMainConfigScreen(it)
            } else {
                null
            }
        }
    }
}