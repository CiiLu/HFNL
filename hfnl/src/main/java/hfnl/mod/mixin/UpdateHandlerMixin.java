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

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(targets = "org.jackhuang.hmcl.upgrade.UpdateHandler")
public abstract class UpdateHandlerMixin {

    @Inject(method = "isNestedApplication", at = @At("HEAD"), cancellable = true)
    private static void isNestedApplication(CallbackInfoReturnable<Boolean> cir) {
        StackTraceElement[] stacktrace = Thread.currentThread().getStackTrace();
        if (stacktrace.length > 0) {
            StackTraceElement lastElement = stacktrace[stacktrace.length - 1];
            if ("hfnl.launch.Main".equals(lastElement.getClassName()) && "main".equals(lastElement.getMethodName())) {
                cir.setReturnValue(false);
            }
        }
    }
}
