package net.psunset.jef.api

import net.minecraft.core.component.DataComponentMap
import net.minecraft.resources.Identifier
import net.minecraft.tags.TagKey
import java.util.stream.Stream

interface INonItemHelper {

    fun getName(obj: Any): String

    fun getId(obj: Any): Identifier

    fun getTags(obj: Any): Stream<out TagKey<*>>

    fun getComponents(obj: Any): DataComponentMap
}