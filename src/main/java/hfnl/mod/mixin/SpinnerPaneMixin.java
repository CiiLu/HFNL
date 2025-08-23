package hfnl.mod.mixin;

import javafx.event.Event;
import javafx.event.EventType;
import org.jackhuang.hmcl.ui.construct.SpinnerPane;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(SpinnerPane.class)
public abstract class SpinnerPaneMixin {
    @Shadow @Final public static EventType<Event> FAILED_ACTION;

    @Inject(method = "<clinit>",at = @At("HEAD"), cancellable = true)
    private static void inject(CallbackInfo ci){
        if(FAILED_ACTION != null) ci.cancel();
    }
}
