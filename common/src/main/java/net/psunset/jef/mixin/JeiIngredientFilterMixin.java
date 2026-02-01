package net.psunset.jef.mixin;

import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.ingredients.ITypedIngredient;
import mezz.jei.gui.ingredients.IngredientFilter;
import net.minecraft.world.item.ItemStack;
import net.psunset.jef.core.FilterManager;
import net.psunset.jef.api.IFilterProxy;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.stream.Stream;

@Mixin(value = IngredientFilter.class, remap = false)
public abstract class JeiIngredientFilterMixin implements IFilterProxy {

    @Shadow
    public abstract void invalidateCache();

    @Shadow
    private void notifyListenersOfChange() {
    }

    @Inject(method = "<init>", at = @At("RETURN"))
    private void jef$registerProxy(CallbackInfo ci) {
        FilterManager.INSTANCE.registerProxy(this);
    }

    @Unique
    @Override
    public void jef$refresh() {
        this.invalidateCache();
        this.notifyListenersOfChange();
    }

    @Inject(method = "getIngredientListUncached", at = @At("RETURN"), cancellable = true)
    private void jef$applyFilters(String filterText, CallbackInfoReturnable<Stream<ITypedIngredient<?>>> cir) {
        Stream<ITypedIngredient<?>> originalStream = cir.getReturnValue();

        Stream<ITypedIngredient<?>> filteredStream = originalStream.filter(ingredient -> {
            if (ingredient.getType() == VanillaTypes.ITEM_STACK) {
                ItemStack itemStack = (ItemStack) ingredient.getIngredient();
                return FilterManager.INSTANCE.test(itemStack);
            }
            return FilterManager.INSTANCE.testNonItem(ingredient.getIngredient());
        });

        cir.setReturnValue(filteredStream);
    }
}
