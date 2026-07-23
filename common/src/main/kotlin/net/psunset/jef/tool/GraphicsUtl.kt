package net.psunset.jef.tool

import net.minecraft.CrashReport
import net.minecraft.ReportedException
import net.minecraft.client.gui.GuiGraphicsExtractor
import net.minecraft.client.renderer.item.TrackingItemStackRenderState
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.item.ItemDisplayContext
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.Level
import net.psunset.jef.gui.render.state.ScaledGuiItemRenderState
import org.joml.Matrix3x2f

object GraphicsUtl {
    @JvmStatic
    private val deferredExtractors = arrayListOf<Function0<Unit>>()

    /**
     * Fired at the head of [GuiGraphicsExtractor.extractDeferredElements]
     */
    @JvmStatic
    fun registerDeferredExtractor(runnable: () -> Unit) {
        deferredExtractors.add(runnable)
    }

    @JvmStatic
    fun runDeferredExtractors() {
        deferredExtractors.forEach { it() }
        deferredExtractors.clear()
    }
}

/**
 * `scale` defaults to `16.0f` in vanilla.
 */
fun GuiGraphicsExtractor.scaledItem(
    stack: ItemStack,
    x: Int,
    y: Int,
    scale: Float = 16.0f,
    seed: Int = 0,
) {
    this.scaledItem(this.minecraft.player, this.minecraft.level, stack, x, y, scale, seed)
}

/**
 * `scale` defaults to `16.0f` in vanilla.
 */
fun GuiGraphicsExtractor.scaledFakeItem(
    stack: ItemStack,
    x: Int,
    y: Int,
    scale: Float = 16.0f,
    seed: Int = 0,
) {
    this.scaledItem(null, null, stack, x, y, scale, seed)
}

/**
 * `scale` defaults to `16.0f` in vanilla.
 * An edition of [GuiGraphicsExtractor.item] using [ScaledGuiItemRenderState]
 */
fun GuiGraphicsExtractor.scaledItem(
    owner: LivingEntity?,
    level: Level?,
    stack: ItemStack,
    x: Int,
    y: Int,
    scale: Float = 16.0f,
    seed: Int = 0,
) {
    if (!stack.isEmpty) {
        val itemStackRenderState = TrackingItemStackRenderState()
        this.minecraft.itemModelResolver.updateForTopItem(
            itemStackRenderState,
            stack,
            ItemDisplayContext.GUI,
            level,
            owner,
            seed
        )

        try {
            this.guiRenderState.addItem(
                ScaledGuiItemRenderState(
                    Matrix3x2f(this.pose),
                    itemStackRenderState,
                    x,
                    y,
                    scale,
                    this.scissorStack.peek()
                )
            )
        } catch (t: Throwable) {
            val report = CrashReport.forThrowable(t, "Rendering item")
            val category = report.addCategory("Item being rendered")
            category.setDetail("Item Type") { stack.item.toString() }
            category.setDetail("Item Components") { stack.components.toString() }
            category.setDetail("Item Foil") { stack.hasFoil().toString() }
            throw ReportedException(report)
        }
    }
}
