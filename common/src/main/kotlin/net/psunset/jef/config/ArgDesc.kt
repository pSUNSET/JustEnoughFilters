package net.psunset.jef.config

import net.minecraft.resources.Identifier
import net.psunset.jef.platform.Platform
import net.psunset.jef.tool.CatchingUtl
import net.psunset.jef.tool.DataComponentUtl
import net.psunset.jef.tool.ItemLikeUtl
import net.psunset.jef.tool.IdUtl

class ArgDesc(val name: String, val type: ArgType) {

    fun validate(input: String): Boolean {
        return type.validator.invoke(input)
    }

    override fun toString(): String {
        return "$name: ${type.name}"
    }
}

class ArgType(val name: String, val validator: ((String) -> Boolean)) {

    constructor(
        displayName: String,
        selections: Collection<String>,
        ignoreCase: Boolean = false
    ) : this(
        displayName,
        validator = if (ignoreCase) {
            { selections.any { _it -> _it.equals(it, ignoreCase = true) } }
        } else {
            { selections.contains(it) }
        }
    )

    companion object {
        @JvmField
        val Str = ArgType("String") { true }

        @JvmField
        val Id = ArgType("Id") { IdUtl.validate(it) }

        @JvmField
        val PartialId = ArgType("Id.Partial") { IdUtl.validatePartial(it) }

        @JvmField
        val ItemLikeId = ArgType("Id") {
            ItemLikeUtl.validate(it)
        }

        @JvmField
        val DataId = ArgType("Id") { DataComponentUtl.validate(it) }

        @JvmField
        val Namespace = ArgType("Id.Namesapce") { Identifier.isValidNamespace(it) }

        @JvmField
        val ModId = ArgType("Id.Namespace", Platform.modIdList())

        @JvmField
        val Path = ArgType("Id.Path") { Identifier.isValidPath(it) }

        @JvmField
        val ModName = ArgType("String", Platform.modNameList(), true)

        @JvmField
        val Reg = ArgType("Regex") { CatchingUtl.isValidRegex(it) }

        @JvmField
        val Clazz = ArgType("Class") { CatchingUtl.isValidClass(it) }
    }
}