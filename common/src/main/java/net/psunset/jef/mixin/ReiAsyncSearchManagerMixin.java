package net.psunset.jef.mixin;

import me.shedaniel.rei.api.client.search.SearchFilter;
import me.shedaniel.rei.api.common.entry.EntryStack;
import me.shedaniel.rei.api.common.entry.type.VanillaEntryTypes;
import me.shedaniel.rei.impl.client.search.AsyncSearchManager;
import net.minecraft.world.item.ItemStack;
import net.psunset.jef.core.FilterManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = AsyncSearchManager.class, remap = false)
public class ReiAsyncSearchManagerMixin {

    @Inject(method = "test", at = @At("RETURN"), cancellable = true)
    private static void jef$applyFilterPredicate(SearchFilter filter, EntryStack<?> stack, long hashExact, CallbackInfoReturnable<Boolean> cir) {
        if (!cir.getReturnValue()) return;

        if (stack.getType().equals(VanillaEntryTypes.ITEM)) {
            cir.setReturnValue(FilterManager.INSTANCE.test((ItemStack) stack.getValue()));
            return;
        }

        cir.setReturnValue(FilterManager.INSTANCE.testNonItem(stack.getValue()));
    }
}
