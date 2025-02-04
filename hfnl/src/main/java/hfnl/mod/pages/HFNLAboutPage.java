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

import hfnl.mod.HFNLI18n;
import javafx.geometry.Insets;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.impl.FabricLoaderImpl;
import org.jackhuang.hmcl.ui.FXUtils;
import org.jackhuang.hmcl.ui.construct.ComponentList;
import org.jackhuang.hmcl.ui.construct.IconedTwoLineListItem;
import org.jackhuang.hmcl.ui.main.AboutPage;

import java.lang.reflect.Method;

public final class HFNLAboutPage extends StackPane {
    public HFNLAboutPage() {
        ComponentList about = new ComponentList();
        {
            IconedTwoLineListItem launcher = new IconedTwoLineListItem();
            launcher.setImage(FXUtils.newBuiltinImage("/assets/img/furnace.png"));
            launcher.setTitle("Hello Furnace Launcher");
            launcher.setExternalLink("https://github.com/ciilu/HFNL");
            launcher.setSubtitle(FabricLoader.getInstance().getAllMods().size() + " " + HFNLI18n.getString("hfnl.loaddedmod"));

            IconedTwoLineListItem fabric = new IconedTwoLineListItem();
            fabric.setImage(FXUtils.newBuiltinImage("/assets/img/fabric.png"));
            fabric.setTitle("Fabric Loader");
            fabric.setSubtitle(FabricLoaderImpl.VERSION);
            fabric.setExternalLink("https://github.com/FabricMC");

            about.getContent().setAll(launcher, fabric);
        }

        ComponentList deps;
        try {
            Class<?> clazz = AboutPage.class;
            Method method = clazz.getDeclaredMethod("loadIconedTwoLineList", String.class);
            method.setAccessible(true);
            deps = (ComponentList) method.invoke(clazz, "/assets/about/hfnldeps.json");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }


        VBox content = new VBox(16);
        content.setPadding(new Insets(10));
        content.getChildren().setAll(
                ComponentList.createComponentListTitle(HFNLI18n.getString("hfnl.about")), about,
                ComponentList.createComponentListTitle(HFNLI18n.getString("hfnl.dep")), deps);

        ScrollPane scrollPane = new ScrollPane(content);
        scrollPane.setFitToWidth(true);
        FXUtils.smoothScrolling(scrollPane);
        getChildren().setAll(scrollPane);
    }
}
