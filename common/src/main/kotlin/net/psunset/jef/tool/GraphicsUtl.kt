package net.psunset.jef.tool

import net.minecraft.CrashReport
import net.minecraft.ReportedException
import net.minecraft.client.gui.GuiGraphics
import net.minecraft.client.renderer.item.TrackingItemStackRenderState
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.item.ItemDisplayContext
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.Level
import net.psunset.jef.gui.render.state.ScaledGuiItemRenderState
import org.joml.Matrix3x2f

/**
 * `scale` defaults to `16.0f` in vanilla.
 */
fun GuiGraphics.renderScaledItem(
    stack: ItemStack,
    x: Int,
    y: Int,
    scale: Float = 16.0f,
    seed: Int = 0,
) {
    this.renderScaledItem(this.minecraft.player, this.minecraft.level, stack, x, y, scale, seed)
}

/**
 * `scale` defaults to `16.0f` in vanilla.
 */
fun GuiGraphics.renderScaledFakeItem(
    stack: ItemStack,
    x: Int,
    y: Int,
    scale: Float = 16.0f,
    seed: Int = 0,
) {
    this.renderScaledItem(null, null, stack, x, y, scale, seed)
}

/**
 * `scale` defaults to `16.0f` in vanilla.
 * An edition of [GuiGraphics.renderItem] using [ScaledGuiItemRenderState]
 */
fun GuiGraphics.renderScaledItem(
    entity: LivingEntity?,
    level: Level?,
    stack: ItemStack,
    x: Int,
    y: Int,
    scale: Float = 16.0f,
    seed: Int = 0,
) {
    if (!stack.isEmpty) {
        val trackingItemStackRenderState = TrackingItemStackRenderState()
        this.minecraft.itemModelResolver.updateForTopItem(
            trackingItemStackRenderState,
            stack,
            ItemDisplayContext.GUI,
            level,
            entity,
            seed
        )

        try {
            this.guiRenderState.submitItem(
                ScaledGuiItemRenderState(
                    stack.item.name.toString(),
                    Matrix3x2f(this.pose()),
                    trackingItemStackRenderState,
                    x,
                    y,
                    scale,
                    this.scissorStack.peek()
                )
            )
        } catch (throwable: Throwable) {
            val crashReport = CrashReport.forThrowable(throwable, "Rendering item")
            val crashReportCategory = crashReport.addCategory("Item being rendered")
            crashReportCategory.setDetail("Item Type") { stack.item.toString() }
            crashReportCategory.setDetail("Item Components") { stack.components.toString() }
            crashReportCategory.setDetail("Item Foil") { stack.hasFoil().toString() }
            throw ReportedException(crashReport)
        }
    }
}