package net.psunset.jef.compat.jei.fabric

import mezz.jei.api.fabric.ingredients.fluids.IJeiFluidIngredient
import net.minecraft.core.component.DataComponentMap
import net.minecraft.world.level.material.Fluid
import net.psunset.jef.compat.jei.JeiNonItemHelper

object JeiNonItemHelperImpl : JeiNonItemHelper() {

    override val fluidApi: Class<*> = IJeiFluidIngredient::class.java

    override fun getFluid(obj: Any): Fluid {
        return (obj as IJeiFluidIngredient).fluidVariant.fluid
    }

    override fun getFluidComponents(obj: Any): DataComponentMap {
        return (obj as IJeiFluidIngredient).fluidVariant.componentMap
    }
}