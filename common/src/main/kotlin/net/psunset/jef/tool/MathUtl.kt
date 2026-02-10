package net.psunset.jef.tool

import net.minecraft.client.renderer.Rect2i

object MathUtl {
}

infix fun Rect2i.valEq(other: Rect2i?): Boolean {
    if (other == null) return false
    return x == other.x && y == other.y &&
            width == other.width && height == other.height
}