/*
 * This program is free software: you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software Foundation,
 * either version 3 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program.
 * If not, see <https://www.gnu.org/licenses/>.
 * */package hfnl.mod.mixin;

import javafx.event.Event;
import javafx.event.EventType;
import org.jackhuang.hmcl.ui.construct.SpinnerPane;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

@Mixin(SpinnerPane.class)
public abstract class SpinnerPaneMixin {
    @Shadow
    @Final
    public static EventType<Event> FAILED_ACTION;

    @ModifyConstant(method = "<clinit>", constant = @Constant(stringValue = "FAILED_ACTION"))
    private static String inject(String constant) {
        return constant + Thread.currentThread().getName();
    }
}
