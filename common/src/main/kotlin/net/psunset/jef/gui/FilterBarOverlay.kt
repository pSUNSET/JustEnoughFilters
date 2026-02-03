package net.psunset.jef.gui

import net.minecraft.client.gui.GuiGraphics
import net.minecraft.client.gui.components.Button
import net.psunset.jef.core.FilterManager
import net.psunset.jef.gui.widget.ClearButton
import net.psunset.jef.gui.widget.FilterToggleButton
import net.psunset.jef.gui.widget.ItemTypeCycleButton
import net.psunset.jef.gui.widget.LogicModeCycleButton

class FilterBarOverlay {
    private val buttons = mutableListOf<Button>()
    private var initialized = false

    fun init(x: Int, y: Int, availableWidth: Int? = null, checkButtonWidth: Int = 20, padding: Int = 2) {
        buttons.clear()

        val buttonCount = JefOverlayManager.allButtonsCount()
        
        var btnSize = checkButtonWidth
        var totalWidth = (btnSize + padding) * buttonCount - padding // removing last padding
        
        // If we have a width constraint and we overflow it
        if (availableWidth != null) {
            if (totalWidth > availableWidth) {
                // Resize buttons to fit
                // (w + p) * n - p = A
                // w * n + p * (n - 1) = A
                // w = (A - p * (n - 1)) / n
                val availableForButtons = availableWidth - (padding * (buttonCount - 1))
                if (availableForButtons > 0) {
                     btnSize = availableForButtons / buttonCount
                }
                totalWidth = (btnSize + padding) * buttonCount - padding
            }
        }
        
        // Center alignment if availableWidth is provided
        var currentX = x
        if (availableWidth != null && totalWidth < availableWidth) {
            currentX += (availableWidth - totalWidth) / 2
        } else {
             // If no width provided, we just start at x. 
             // But usually x is calculated as "screen.width - calculated_width" in fallback.
             // JustEnoughFilters fallback logic does that.
        }

        // ItemType Button
        val typeBtn = ItemTypeCycleButton(currentX, y, btnSize, btnSize)
        buttons.add(typeBtn)
        currentX += btnSize + padding

        // Logic Button
        val logicBtn = LogicModeCycleButton(currentX, y, btnSize, btnSize)
        buttons.add(logicBtn)
        currentX += btnSize + padding

        // Filter Buttons
        for (filter in FilterManager.allToggledFilters) {
            val btn = FilterToggleButton(filter, currentX, y, btnSize, btnSize)
            buttons.add(btn)
            currentX += btnSize + padding // 2px spacing
        }
        
        // Clear Button
        val clearBtn = ClearButton(currentX, y, btnSize, btnSize)
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
