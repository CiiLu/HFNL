package hfnl.mod.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import hfnl.mod.page.ModsListPage;
import org.jackhuang.hmcl.ui.SVG;
import org.jackhuang.hmcl.ui.construct.AdvancedListBox;
import org.jackhuang.hmcl.ui.construct.TabControl;
import org.jackhuang.hmcl.ui.construct.TabHeader;
import org.jackhuang.hmcl.ui.main.LauncherSettingsPage;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import hfnl.mod.util.ModI18n;

import static org.jackhuang.hmcl.util.i18n.I18n.i18n;

@Mixin(LauncherSettingsPage.class)
public abstract class LauncherSettingsPageMixin {
    @Unique
    private final TabControl.Tab<ModsListPage> modListTab = new TabControl.Tab<>("hfnlModListTab");
    @Shadow
    @Final
    private TabHeader tab;

    @Inject(method = "<init>", at = @At(value = "INVOKE", target = "Lorg/jackhuang/hmcl/ui/construct/TabControl$Tab;setNodeSupplier(Ljava/util/function/Supplier;)V"))
    private void inject1(CallbackInfo ci) {
        modListTab.setNodeSupplier(ModsListPage::new);
    }

    @Inject(method = "<init>", at = @At(value = "INVOKE", target = "Lorg/jackhuang/hmcl/ui/FXUtils;setLimitWidth(Ljavafx/scene/layout/Region;D)V"))
    private void inject2(CallbackInfo ci, @Local AdvancedListBox sideBar) {
        sideBar.startCategory(i18n("mods"));
        sideBar.addNavigationDrawerTab(this.tab, modListTab, ModI18n.get("mods.list"), SVG.EXTENSION);
    }
}
