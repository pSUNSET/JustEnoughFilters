package net.psunset.jef.compat.emi

import dev.emi.emi.search.EmiSearch
import net.psunset.jef.api.IFilterProxy

object EmiFilterProxy : IFilterProxy {
    override fun `jef$refresh`() {
        EmiSearch.update()
    }
}