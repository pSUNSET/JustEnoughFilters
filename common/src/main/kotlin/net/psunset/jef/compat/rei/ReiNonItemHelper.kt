package net.psunset.jef.compat.rei

import dev.architectury.fluid.FluidStack
import me.shedaniel.rei.api.common.entry.type.BuiltinEntryTypes
import net.minecraft.client.resources.language.I18n
import net.minecraft.core.component.DataComponentMap
import net.minecraft.resources.ResourceLocation
import net.minecraft.tags.TagKey
import net.minecraft.util.Unit
import net.psunset.jef.api.INonItemHelper
import net.psunset.jef.tool.RLUtl
import net.psunset.jef.tool.toId
import net.psunset.jef.util.NonItemHelper
import java.util.stream.Stream

object ReiNonItemHelper : INonItemHelper {

    override fun getName(obj: Any): String {
        if (obj is FluidStack) {
            return I18n.get(obj.translationKey)
        }
        if (obj == Unit.INSTANCE) {
            return "[empty]"
        }
        return "[unknown]"
    }

    override fun getId(obj: Any): ResourceLocation {
        if (obj is FluidStack) {
            return obj.fluid.toId()
        }
        if (obj == Unit.INSTANCE) {
            return BuiltinEntryTypes.EMPTY_ID
        }
        return RLUtl.UNKNOWN
    }

    override fun getTags(obj: Any): Stream<out TagKey<*>> {
        if (obj is FluidStack) {
            return obj.fluid.builtInRegistryHolder().tags()
        }
        return Stream.empty()
    }

    override fun getComponents(obj: Any): DataComponentMap {
        if (obj is FluidStack) {
            return obj.components
        }
        return DataComponentMap.EMPTY
    }

    fun init() {
        NonItemHelper.innerImpl = this
    }
}