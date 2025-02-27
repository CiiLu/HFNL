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
package hfnl.mod.pages;

import hfnl.mod.I18n;
import javafx.geometry.Insets;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.impl.FabricLoaderImpl;
import org.jackhuang.hmcl.ui.FXUtils;
import org.jackhuang.hmcl.ui.construct.ComponentList;
import org.jackhuang.hmcl.ui.construct.IconedTwoLineListItem;

import java.lang.reflect.Method;

public final class AboutPage extends StackPane {
    public AboutPage() {
        ComponentList about = new ComponentList();
        {
            IconedTwoLineListItem launcher = new IconedTwoLineListItem();
            launcher.setImage(FXUtils.newBuiltinImage("/assets/img/furnace.png"));
            launcher.setTitle("Hello Furnace Launcher");
            launcher.setExternalLink("https://github.com/ciilu/HFNL");
            launcher.setSubtitle(FabricLoader.getInstance().getAllMods().size() + " " + I18n.get("hfnl.loaddedmod"));

            IconedTwoLineListItem fabric = new IconedTwoLineListItem();
            fabric.setImage(FXUtils.newBuiltinImage("/assets/img/fabric.png"));
            fabric.setTitle("Fabric Loader");
            fabric.setSubtitle(FabricLoaderImpl.VERSION);
            fabric.setExternalLink("https://github.com/FabricMC");

            about.getContent().setAll(launcher, fabric);
        }

        ComponentList deps;
        try {
            Class<?> clazz = org.jackhuang.hmcl.ui.main.AboutPage.class;
            Method method = clazz.getDeclaredMethod("loadIconedTwoLineList", String.class);
            method.setAccessible(true);
            deps = (ComponentList) method.invoke(clazz, "/assets/about/hfnldeps.json");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }


        VBox content = new VBox(16);
        content.setPadding(new Insets(10));
        content.getChildren().setAll(
                ComponentList.createComponentListTitle(I18n.get("hfnl.about")), about,
                ComponentList.createComponentListTitle(I18n.get("hfnl.dep")), deps);

        ScrollPane scrollPane = new ScrollPane(content);
        scrollPane.setFitToWidth(true);
        FXUtils.smoothScrolling(scrollPane);
        getChildren().setAll(scrollPane);
    }
}
