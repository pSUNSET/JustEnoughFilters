package net.psunset.jef.mixin.fabric;

import net.fabricmc.loader.api.FabricLoader;
import net.psunset.jef.platform.Platform;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

@Mixin(Platform.class)
public class PlatformMixin {

    @Overwrite
    public static boolean isFabric() {
        return true;
    }

    @Overwrite
    public static boolean isNeoForge() {
        return false;
    }

    @Overwrite
    public static boolean isLoaded(String modId) {
        return FabricLoader.getInstance().isModLoaded(modId);
    }
}
