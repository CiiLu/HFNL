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

import com.llamalad7.mixinextras.sugar.Local;
import hfnl.mod.pages.HFNLSettingPage;
import org.jackhuang.hmcl.ui.Controllers;
import org.jackhuang.hmcl.ui.SVG;
import org.jackhuang.hmcl.ui.construct.AdvancedListBox;
import org.jackhuang.hmcl.ui.construct.AdvancedListItem;
import org.jackhuang.hmcl.util.i18n.I18n;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static org.jackhuang.hmcl.ui.versions.VersionPage.wrap;

@Mixin(targets = "org.jackhuang.hmcl.ui.main.RootPage$Skin")
public abstract class RootPageMixin {
    @Inject(method = "<init>", at = @At("RETURN"))
    private void onInit(CallbackInfo ci, @Local AdvancedListBox sidebar) {
        AdvancedListItem hfnlSettingItem = new AdvancedListItem();
        hfnlSettingItem.setLeftGraphic(wrap(SVG.SCRIPT));
        hfnlSettingItem.setActionButtonVisible(false);
        hfnlSettingItem.setTitle("HFNL "+I18n.i18n("settings"));
        HFNLSettingPage hfnlSettingPage = new HFNLSettingPage();
        hfnlSettingItem.setOnAction(e -> Controllers.navigate(hfnlSettingPage));
        sidebar.add(hfnlSettingItem);
    }
}