package net.psunset.jef.mixin.neoforge;

import net.neoforged.fml.ModList;
import net.psunset.jef.platform.Platform;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

@Mixin(Platform.class)
public class PlatformMixin {

    @Overwrite
    public static boolean isFabric() {
        return false;
    }

    @Overwrite
    public static boolean isNeoForge() {
        return true;
    }

    @Overwrite
    public static boolean isLoaded(String modId) {
        return ModList.get().isLoaded(modId);
    }
}
