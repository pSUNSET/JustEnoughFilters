package net.psunset.jef.gui.render.state

import net.minecraft.client.gui.navigation.ScreenRectangle
import net.minecraft.client.gui.render.state.GuiItemRenderState
import net.minecraft.client.renderer.item.TrackingItemStackRenderState
import org.joml.Matrix3x2f

/**
 * Creates a state with a fixed, specific `scale`.
 * `scale` defaults to `16` in vanilla.
 * May be broken when `itemStackRenderState.isOversizedInGui()` is `true`.
 */
class ScaledGuiItemRenderState(
    name: String,
    pose: Matrix3x2f,
    itemStackRenderState: TrackingItemStackRenderState,
    x: Int,
    y: Int,
    @JvmField val scale: Float,
    scissorArea: ScreenRectangle?
) : GuiItemRenderState(name, pose, itemStackRenderState, x, y, scissorArea) {

    private val fakeOversizedItemBounds: ScreenRectangle = ScreenRectangle(x, y, scale.toInt(), scale.toInt())

    private val scaledBounds: ScreenRectangle? = fakeOversizedItemBounds.let {
        it.transformMaxBounds(pose)
        if (scissorArea != null) {
            scissorArea.intersection(it)
        } else {
            it
        }
    }

    override fun oversizedItemBounds(): ScreenRectangle {
        return this.fakeOversizedItemBounds
    }

    override fun bounds(): ScreenRectangle? {
        return this.scaledBounds
    }
}