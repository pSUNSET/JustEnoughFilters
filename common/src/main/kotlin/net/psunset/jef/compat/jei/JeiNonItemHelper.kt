package net.psunset.jef.compat.jei

import net.minecraft.client.resources.language.I18n
import net.minecraft.core.component.DataComponentMap
import net.minecraft.resources.Identifier
import net.minecraft.tags.TagKey
import net.minecraft.util.Util
import net.minecraft.world.level.material.Fluid
import net.psunset.jef.api.INonItemHelper
import net.psunset.jef.platform.Platform
import net.psunset.jef.tool.IdUtl
import net.psunset.jef.tool.toId
import net.psunset.jef.util.NonItemHelper
import java.util.stream.Stream

abstract class JeiNonItemHelper : INonItemHelper {

    /**
     * [Fluid]'s wrapper,
     * standing for `IJeiFluidIngredient` in Fabric or `FluidStack` in NeoForge.
     */
    abstract val fluidApi: Class<*>

    init {
        NonItemHelper.innerImpl = this
    }

    /**
     * @param obj must be an instance of [fluidApi]
     */
    abstract fun getFluid(obj: Any): Fluid

    /**
     * @param obj must be an instance of [fluidApi]
     */
    abstract fun getFluidComponents(obj: Any): DataComponentMap

    override fun getName(obj: Any): String {
        if (obj.javaClass.isAssignableFrom(fluidApi)) {
            return I18n.get(Util.makeDescriptionId("block", getFluid(obj).toId()))
        }
        return "[unknown]"
    }

    override fun getId(obj: Any): Identifier {
        if (obj.javaClass.isAssignableFrom(fluidApi)) {
            return getFluid(obj).toId()
        }
        return IdUtl.UNKNOWN
    }

    override fun getTags(obj: Any): Stream<out TagKey<*>> {
        if (obj.javaClass.isAssignableFrom(fluidApi)) {
            return getFluid(obj).builtInRegistryHolder().tags()
        }
        return Stream.empty()
    }

    override fun getComponents(obj: Any): DataComponentMap {
        if (obj.javaClass.isAssignableFrom(fluidApi)) {
            return getFluidComponents(obj)
        }
        return DataComponentMap.EMPTY
    }

    companion object {
        @JvmStatic
        fun init() {
            Class.forName(
                "${JeiNonItemHelper::class.java.packageName}.${Platform.lowercaseName()}.JeiNonItemHelperImpl"
            )
        }
    }
}