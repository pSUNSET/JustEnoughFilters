package net.psunset.jef.compat.emi

import dev.emi.emi.api.stack.EmiStack
import net.minecraft.Util
import net.minecraft.client.resources.language.I18n
import net.minecraft.core.component.DataComponentMap
import net.minecraft.resources.ResourceLocation
import net.minecraft.tags.TagKey
import net.minecraft.world.item.Items
import net.minecraft.world.level.material.Fluid
import net.psunset.jef.api.INonItemHelper
import net.psunset.jef.tool.RLUtl
import net.psunset.jef.tool.toId
import net.psunset.jef.util.NonItemHelper
import java.util.stream.Stream

object EmiNonItemHelper : INonItemHelper {

    override fun getName(obj: Any): String {
        if (obj is Fluid) {
            return I18n.get(Util.makeDescriptionId("block", obj.toId()))
        }
        if (obj == Items.AIR) {
            return "[empty]"
        }
        return "[unknown]"
    }

    override fun getId(obj: Any): ResourceLocation {
        if (obj is Fluid) {
            return obj.toId()
        }
        if (obj == Items.AIR) {
            return EmiStack.EMPTY.id
        }
        return RLUtl.UNKNOWN
    }

    override fun getTags(obj: Any): Stream<out TagKey<*>> {
        if (obj is Fluid) {
            return obj.builtInRegistryHolder().tags()
        }
        return Stream.empty()
    }

    override fun getComponents(obj: Any): DataComponentMap {
        if (obj is Fluid) {
            return DataComponentMap.EMPTY
            TODO()
        }
        return DataComponentMap.EMPTY
    }

    fun init() {
        NonItemHelper.innerImpl = this
    }
}