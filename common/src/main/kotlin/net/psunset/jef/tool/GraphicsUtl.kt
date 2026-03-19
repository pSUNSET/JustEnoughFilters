package net.psunset.jef.tool

import com.mojang.blaze3d.platform.Lighting
import net.minecraft.CrashReport
import net.minecraft.ReportedException
import net.minecraft.client.gui.GuiGraphics
import net.minecraft.client.renderer.texture.OverlayTexture
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.item.ItemDisplayContext
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.Level

/**
 * `scale` defaults to `16.0f` in vanilla.
 */
fun GuiGraphics.renderScaledItem(
    stack: ItemStack,
    x: Int,
    y: Int,
    scale: Float = 16.0f,
    seed: Int = 0,
    guiOffset: Int = 0,
) {
    this.renderScaledItem(this.minecraft.player, this.minecraft.level, stack, x, y, scale, seed, guiOffset)
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
    guiOffset: Int = 0,
) {
    this.renderScaledItem(null, null, stack, x, y, scale, seed, guiOffset)
}

/**
 * `scale` defaults to `16.0f` in vanilla.
 * An edition of [GuiGraphics.renderItem]
 */
fun GuiGraphics.renderScaledItem(
    entity: LivingEntity?,
    level: Level?,
    stack: ItemStack,
    x: Int,
    y: Int,
    scale: Float = 16.0f,
    seed: Int = 0,
    guiOffset: Int = 0,
) {
    if (!stack.isEmpty) {
        val bakedModel = this.minecraft.itemRenderer.getModel(stack, level, entity, seed)
        this.pose.pushPose()
        this.pose.translate(
            x + (scale / 2.0f),
            y + (scale / 2.0f),
            150.0f + (if (bakedModel.isGui3d) guiOffset else 0)
        )

        try {
            this.pose.scale(scale, -scale, scale)
            val bl = !bakedModel.usesBlockLight()
            if (bl) {
                Lighting.setupForFlatItems()
            }

            this.minecraft.itemRenderer.render(
                stack,
                ItemDisplayContext.GUI,
                false,
                this.pose,
                this.bufferSource(),
                15728880,
                OverlayTexture.NO_OVERLAY,
                bakedModel
            )
            this.flush()
            if (bl) {
                Lighting.setupFor3DItems()
            }
        } catch (var12: Throwable) {
            val crashReport = CrashReport.forThrowable(var12, "Rendering item")
            val crashReportCategory = crashReport.addCategory("Item being rendered")
            crashReportCategory.setDetail("Item Type") { stack.item.toString() }
            crashReportCategory.setDetail("Item Components") { stack.getComponents().toString() }
            crashReportCategory.setDetail("Item Foil") { stack.hasFoil().toString() }
            throw ReportedException(crashReport)
        }

        this.pose.popPose()
    }
}