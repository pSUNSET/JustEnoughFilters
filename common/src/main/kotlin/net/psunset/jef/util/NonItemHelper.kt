package net.psunset.jef.util

import net.minecraft.core.component.DataComponentMap
import net.minecraft.resources.Identifier
import net.minecraft.tags.TagKey
import net.psunset.jef.api.INonItemHelper
import org.jetbrains.annotations.ApiStatus
import java.util.stream.Stream

object NonItemHelper {

    @ApiStatus.Internal
    @JvmStatic
    lateinit var innerImpl: INonItemHelper

    @JvmStatic
    fun getName(obj: Any): String {
        return innerImpl.getName(obj)
    }

    @JvmStatic
    fun getId(obj: Any): Identifier {
        return innerImpl.getId(obj)
    }

    @JvmStatic
    fun getTags(obj: Any): Stream<out TagKey<*>> {
        return innerImpl.getTags(obj)
    }

    @JvmStatic
    fun getComponents(obj: Any): DataComponentMap {
        return innerImpl.getComponents(obj)
    }
}