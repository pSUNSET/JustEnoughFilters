package net.psunset.jef

import net.psunset.jef.tool.CompatUtl
import org.objectweb.asm.tree.ClassNode
import org.spongepowered.asm.mixin.extensibility.IMixinConfigPlugin
import org.spongepowered.asm.mixin.extensibility.IMixinInfo

abstract class JefMixinPlugin : IMixinConfigPlugin {
    override fun onLoad(mixinPackage: String) {
    }

    override fun getRefMapperConfig(): String? {
        return null
    }

    override fun shouldApplyMixin(targetClassName: String, mixinClassName: String): Boolean {
        val simpleMixinClassName = mixinClassName.substringAfterLast('.')

        if (simpleMixinClassName.startsWith("Emi") && !CompatUtl.EMI.isLoaded()) {
            return false
        }

        if (simpleMixinClassName.startsWith("Rei") && !CompatUtl.REI.isLoaded()) {
            return false
        }

        if (simpleMixinClassName.startsWith("Jei") && (!CompatUtl.JEI.isLoaded() || CompatUtl.EMI.isLoaded())) {
            return false
        }

        return true
    }

    override fun acceptTargets(
        myTargets: Set<String>,
        otherTargets: Set<String>
    ) {
    }

    override fun getMixins(): List<String> {
        return listOf()
    }

    override fun preApply(
        targetClassName: String,
        targetClass: ClassNode,
        mixinClassName: String,
        mixinInfo: IMixinInfo
    ) {
    }

    override fun postApply(
        targetClassName: String,
        targetClass: ClassNode,
        mixinClassName: String,
        mixinInfo: IMixinInfo
    ) {
    }
}