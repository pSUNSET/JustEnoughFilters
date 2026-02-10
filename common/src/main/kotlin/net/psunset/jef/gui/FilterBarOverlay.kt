package net.psunset.jef.gui

import net.minecraft.client.gui.GuiGraphics
import net.minecraft.client.gui.components.Button
import net.psunset.jef.core.FilterManager
import net.psunset.jef.gui.widget.ClearButton
import net.psunset.jef.gui.widget.FilterToggleButton
import net.psunset.jef.gui.widget.ItemTypeCycleButton
import net.psunset.jef.gui.widget.LogicModeCycleButton

object FilterBarOverlay {

    const val EXPECTED_BTN_SIZE = 18
    const val PADDING = 2
    const val REVERSED_HEIGHT = EXPECTED_BTN_SIZE + PADDING * 2

    private val buttons = mutableListOf<Button>()
    private var initialized = false

    fun init(x: Int, y: Int, availableWidth: Int? = null) {
        buttons.clear()

        val buttonCount = JefOverlayManager.allButtonsCount()

        var btnSize = EXPECTED_BTN_SIZE
        var totalWidth = (btnSize + PADDING) * buttonCount - PADDING // removing last padding

        // If we have a width constraint and we overflow it
        if (availableWidth != null) {
            if (totalWidth > availableWidth) {
                // Resize buttons to fit
                // (w + p) * n - p = A
                // w * n + p * (n - 1) = A
                // w = (A - p * (n - 1)) / n
                val availableForButtons = availableWidth - (PADDING * (buttonCount - 1))
                if (availableForButtons > 0) {
                    btnSize = availableForButtons / buttonCount
                }
                totalWidth = (btnSize + PADDING) * buttonCount - PADDING
            }
        }

        // Center alignment if availableWidth is provided
        var currentX = x
        val currentY = y + PADDING
        if (availableWidth != null && totalWidth < availableWidth) {
            currentX += (availableWidth - totalWidth) / 2
        } else {
            // If no width provided, we just start at x.
            // But usually x is calculated as "screen.width - calculated_width" in fallback.
            // JustEnoughFilters fallback logic does that.
        }

        // ItemType Button
        val typeBtn = ItemTypeCycleButton(currentX, currentY, btnSize, btnSize)
        buttons.add(typeBtn)
        currentX += btnSize + PADDING

        // Logic Button
        val logicBtn = LogicModeCycleButton(currentX, currentY, btnSize, btnSize)
        buttons.add(logicBtn)
        currentX += btnSize + PADDING

        // Filter Buttons
        for (filter in FilterManager.allToggledFilters) {
            val btn = FilterToggleButton(filter, currentX, currentY, btnSize, btnSize)
            buttons.add(btn)
            currentX += btnSize + PADDING // 2px spacing
        }

        // Clear Button
        val clearBtn = ClearButton(currentX, currentY, btnSize, btnSize)
        buttons.add(clearBtn)

        initialized = true
    }

    fun render(guiGraphics: GuiGraphics, mouseX: Int, mouseY: Int, partialTick: Float) {
        if (!initialized) return

        for (btn in buttons) {
            btn.render(guiGraphics, mouseX, mouseY, partialTick)
        }
    }

    fun mouseClicked(mouseX: Double, mouseY: Double, button: Int): Boolean {
        if (!initialized) return false

        for (btn in buttons) {
            if (btn.mouseClicked(mouseX, mouseY, button)) {
                return true
            }
        }
        return false
    }
}