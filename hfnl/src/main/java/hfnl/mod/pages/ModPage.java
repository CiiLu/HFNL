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
import net.fabricmc.loader.api.ModContainer;
import net.fabricmc.loader.api.metadata.ModMetadata;
import org.jackhuang.hmcl.ui.FXUtils;
import org.jackhuang.hmcl.ui.construct.ComponentList;
import org.jackhuang.hmcl.ui.construct.IconedTwoLineListItem;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class ModPage extends StackPane {
    private final ComponentList mods = new ComponentList();

    public ModPage() {
        List<ModContainer> modContainers = FabricLoader.getInstance()
                .getAllMods()
                .stream()
                .sorted(Comparator.comparing(modContainer -> modContainer.getMetadata().getName()))
                .collect(Collectors.toList());

        modContainers.forEach(this::addModToComponentList);

        VBox content = new VBox(16);
        content.setPadding(new Insets(10));
        content.getChildren().addAll(ComponentList.createComponentListTitle(I18n.get("hfnl.mod")), mods);
        ScrollPane scrollPane = new ScrollPane(content);
        scrollPane.setFitToWidth(true);
        FXUtils.smoothScrolling(scrollPane);
        getChildren().setAll(scrollPane);
    }

    private void addModToComponentList(ModContainer modContainer) {
        IconedTwoLineListItem modItem = createModItem(modContainer);
        mods.getContent().add(modItem);
    }

    private IconedTwoLineListItem createModItem(ModContainer modContainer) {
        ModMetadata metadata = modContainer.getMetadata();
        String modid = metadata.getId();
        IconedTwoLineListItem mod = new IconedTwoLineListItem();

        if ("java".equals(modid)) {
            configureJavaMod(mod);
        } else {
            configureRegularMod(mod, metadata);
        }

        return mod;
    }

    private void configureJavaMod(IconedTwoLineListItem mod) {
        mod.setExternalLink(System.getProperty("java.vendor.url"));
        mod.getTags().add("ID: java");
        mod.getTags().add(I18n.get("hfnl.version") + ": " + System.getProperty("java.version"));
        mod.getTags().add(I18n.get("hfnl.vendor") + ": " + System.getProperty("java.vendor"));
        mod.setTitle("Java");
        mod.setSubtitle(System.getProperty("java.runtime.name"));
        mod.setImage(FXUtils.newBuiltinImage("assets/img/command.png"));
    }


    private void configureRegularMod(IconedTwoLineListItem mod, ModMetadata metadata) {
        Optional<String> iconPath = metadata.getIconPath(0);
        if (iconPath.isPresent()) {
            mod.setImage(FXUtils.newBuiltinImage(iconPath.get()));
        } else {
            setDefaultImage(mod, metadata.getId());
        }

        if(metadata.getContact().get("homepage").isPresent()) {
            mod.setExternalLink(metadata.getContact().get("homepage").get());
        }
        mod.getTags().add("ID: " + metadata.getId());
        mod.getTags().add(I18n.get("hfnl.version") + ":" + metadata.getVersion());
        mod.setTitle(metadata.getName());
        mod.setSubtitle(metadata.getDescription());
    }

    private void setDefaultImage(IconedTwoLineListItem mod, String modId) {
        if ("mixinextras".equals(modId)) {
            mod.setImage((FXUtils.newBuiltinImage("/assets/img/chest.png")));
        } else {
            mod.setImage(FXUtils.newBuiltinImage("/assets/img/craft_table.png"));
        }
    }
}