package net.psunset.jef.api

import net.minecraft.resources.ResourceLocation
import net.minecraft.tags.TagKey
import java.util.stream.Stream

interface INonItemHelper {

    fun getName(obj: Any): String

    fun getId(obj: Any): ResourceLocation

    fun getTags(obj: Any): Stream<out TagKey<*>>
}