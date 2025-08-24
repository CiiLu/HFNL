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
import hfnl.mod.util.ModI18n;
import javafx.scene.layout.VBox;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.impl.FabricLoaderImpl;
import org.jackhuang.hmcl.ui.FXUtils;
import org.jackhuang.hmcl.ui.construct.ComponentList;
import org.jackhuang.hmcl.ui.construct.IconedTwoLineListItem;
import org.jackhuang.hmcl.ui.main.AboutPage;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(AboutPage.class)
public abstract class AboutPageMixin {
    @Inject(method = "<init>", at = @At(value = "INVOKE", target = "Ljavafx/scene/control/ScrollPane;<init>(Ljavafx/scene/Node;)V"))
    public void inject(CallbackInfo ci, @Local VBox content) {
        ComponentList about = new ComponentList();

        IconedTwoLineListItem hfnl = new IconedTwoLineListItem();
        hfnl.setImage(FXUtils.newBuiltinImage("/assets/img/furnace.png"));
        hfnl.setTitle("Hello Furnace Launcher");
        hfnl.setExternalLink("https://github.com/CiiLu/HFNL");
        hfnl.setSubtitle(FabricLoader.getInstance().getAllMods().size() + " " + ModI18n.get("loaddedmod"));

        IconedTwoLineListItem fabric = new IconedTwoLineListItem();
        fabric.setImage(FXUtils.newBuiltinImage("/assets/img/fabric.png"));
        fabric.setTitle("Fabric Loader");
        fabric.setSubtitle(FabricLoaderImpl.VERSION);
        fabric.setExternalLink("https://github.com/FabricMC");

        about.getContent().setAll(hfnl, fabric);
        content.getChildren().add(0, about);
        content.getChildren().add(0, ComponentList.createComponentListTitle("HFNL"));
    }
}
