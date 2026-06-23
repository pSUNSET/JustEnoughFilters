package net.psunset.jef.compat.jei.neoforge

import net.minecraft.core.component.DataComponentMap
import net.minecraft.world.level.material.Fluid
import net.neoforged.neoforge.fluids.FluidStack
import net.psunset.jef.compat.jei.JeiNonItemHelper

object JeiNonItemHelperImpl : JeiNonItemHelper() {

    override val fluidApi: Class<*> = FluidStack::class.java

    override fun getFluid(obj: Any): Fluid {
        return (obj as FluidStack).fluid
    }

    override fun getFluidComponents(obj: Any): DataComponentMap {
        return (obj as FluidStack).components
    }
}