package net.psunset.jef.gui.widget

import net.minecraft.client.Minecraft
import net.minecraft.client.gui.components.AbstractButton
import net.minecraft.network.chat.Component

/**
 * Clones all `on...()` functions with the name, `onRight...()`.
 * They are only fired by right mouse button and default to run left button one.
 *
 * Raw `on...()` functions are now only fired by left mouse button.
 */
abstract class AbstractLeftRightClickButton(
    x: Int,
    y: Int,
    width: Int,
    height: Int,
    message: Component,
) : AbstractButton(x, y, width, height, message) {

    /**
     * Fired when button got clicked by right button.
     * Defaults to run `onPress()`
     */
    open fun onRightPress() {
        this.onPress()
    }

    open fun onRightClick(mouseX: Double, mouseY: Double) {
        this.onRightPress()
    }

    open fun onRightRelease(mouseX: Double, mouseY: Double) {
        this.onRelease(mouseX, mouseY)
    }

    protected open fun onRightDrag(mouseX: Double, mouseY: Double, dragX: Double, dragY: Double) {
        this.onDrag(mouseX, mouseY, dragX, dragY)
    }

    override fun mouseClicked(mouseX: Double, mouseY: Double, button: Int): Boolean {
        if (this.active && this.visible) {
            if (this.isValidClickButton(button)) {
                val bl = this.clicked(mouseX, mouseY)
                if (bl) {
                    this.playDownSound(Minecraft.getInstance().soundManager)
                    if (button == 0) {
                        this.onClick(mouseX, mouseY)
                    } else {  // button == 1
                        this.onRightClick(mouseX, mouseY)
                    }
                    return true
                }
            }

            return false
        } else {
            return false
        }
    }

    override fun mouseReleased(mouseX: Double, mouseY: Double, button: Int): Boolean {
        if (this.isValidClickButton(button)) {
            if (button == 0) {
                this.onRelease(mouseX, mouseY)
            } else {  // button == 1
                this.onRightRelease(mouseX, mouseY)
            }
            return true
        } else {
            return false
        }
    }

    override fun mouseDragged(mouseX: Double, mouseY: Double, button: Int, dragX: Double, dragY: Double): Boolean {
        if (this.isValidClickButton(button)) {
            if (button == 0) {
                this.onDrag(mouseX, mouseY, dragX, dragY)
            } else {  // button == 1
                this.onRightDrag(mouseX, mouseY, dragX, dragY)
            }
            return true
        } else {
            return false
        }
    }

    override fun isValidClickButton(button: Int): Boolean {
        return button == 0 || button == 1
    }
}