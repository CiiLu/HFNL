/*
 * This program is free software: you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software Foundation,
 * either version 3 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program.
 * If not, see <https://www.gnu.org/licenses/>.
 * */
package hfnl.mod.mixin;

import org.jackhuang.hmcl.setting.Config;
import org.jackhuang.hmcl.setting.ConfigHolder;
import org.jackhuang.hmcl.setting.GlobalConfig;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

import java.io.IOException;

@Mixin(ConfigHolder.class)
public abstract class ConfigHolderMixin {
    @Shadow
    private static Config configInstance;
    @Shadow
    private static GlobalConfig globalConfigInstance;

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

    /**
     * @author CiiLu
     * @reason [
     */
    @Overwrite
    public static GlobalConfig globalConfig() {
        if (globalConfigInstance == null) {
            try {
                ConfigHolder.init();
            } catch (IOException ignored) {
            }
        }
        return  globalConfigInstance;
    }
}
