package net.psunset.jef.mixin;

import dev.emi.emi.api.stack.EmiStack;
import dev.emi.emi.search.EmiSearch;
import net.psunset.jef.core.FilterManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = EmiSearch.CompiledQuery.class, remap = false)
public class EmiCompiledQueryMixin {

    @Inject(method = "isEmpty", at = @At("RETURN"), cancellable = true)
    private void jef$andIsActiveFilterEmpty(CallbackInfoReturnable<Boolean> cir) {
        if (!cir.getReturnValue()) return;
        cir.setReturnValue(FilterManager.INSTANCE.areAllFiltersDisabled());
    }

    @Inject(method = "test", at = @At("RETURN"), cancellable = true)
    private void jef$applyFilters(EmiStack stack, CallbackInfoReturnable<Boolean> cir) {
        if (!cir.getReturnValue()) return;

        if (!stack.getItemStack().isEmpty()) {
            cir.setReturnValue(FilterManager.INSTANCE.test(stack.getItemStack()));
            return;
        }
        cir.setReturnValue(FilterManager.INSTANCE.testNonItem(stack.getKey()));
    }
}
