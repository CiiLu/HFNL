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

import hfnl.launch.Main;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

@Mixin(targets = "org.jackhuang.hmcl.upgrade.UpdateHandler")
public abstract class UpdateHandlerMixin {

    /**
     * @author
     * @reason
     */
    @Overwrite
    private static boolean isNestedApplication() {
        StackTraceElement[] stacktrace = Thread.currentThread().getStackTrace();
        for(int i = 0; i < stacktrace.length; ++i) {
            StackTraceElement element = stacktrace[i];
            if (Main.class.getName().equals(element.getClassName()) && "main".equals(element.getMethodName())) {
                return i + 1 != stacktrace.length;
            }
        }
        return false;
    }
}
