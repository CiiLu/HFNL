package hfnl.mod.mixin;

import org.jackhuang.hmcl.setting.Config;
import org.jackhuang.hmcl.setting.ConfigHolder;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

import java.io.IOException;

@Mixin(ConfigHolder.class)
public abstract class ConfigHolderMixin {
    @Shadow
    private static Config configInstance;

    /**
     * @author CiiLu
     * @reason [
     */
    @Overwrite
    public static Config config() {
        if (configInstance == null) {
            try {
                ConfigHolder.init();
            } catch (IOException ignored) {
            }
        }
        return configInstance;
    }
}
