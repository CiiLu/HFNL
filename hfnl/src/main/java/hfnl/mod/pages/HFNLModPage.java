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
import net.fabricmc.loader.api.ModContainer;
import net.fabricmc.loader.api.metadata.ModMetadata;
import org.jackhuang.hmcl.ui.FXUtils;
import org.jackhuang.hmcl.ui.construct.ComponentList;
import org.jackhuang.hmcl.ui.construct.IconedTwoLineListItem;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;

public class HFNLModPage extends StackPane {
    private final ComponentList mods = new ComponentList();

    public HFNLModPage() {
        List<ModContainer> modContainers = FabricLoader.getInstance().getAllMods().stream().sorted(Comparator.comparing(modContainer -> modContainer.getMetadata().getName())).toList();

        modContainers.forEach(this::addModToComponentList);

        VBox content = new VBox(16);
        content.setPadding(new Insets(10));
        content.getChildren().addAll(ComponentList.createComponentListTitle(HFNLI18n.getString("hfnl.mod")), mods);
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
        mod.getTags().addAll("ID: java", HFNLI18n.getString("hfnl.vendor") + ": " + System.getProperty("java.vendor"), HFNLI18n.getString("hfnl.version") + ": " + System.getProperty("java.version"));
        mod.setTitle(System.getProperty("java.runtime.name"));
        mod.setSubtitle(System.getProperty("sun.management.compiler"));
        mod.setImage(FXUtils.newBuiltinImage("assets/img/command.png"));
    }


    private void configureRegularMod(IconedTwoLineListItem mod, ModMetadata metadata) {
        Optional<String> iconPath = metadata.getIconPath(0);
        if (iconPath.isPresent()) {
            mod.setImage(FXUtils.newBuiltinImage(iconPath.get()));
        } else {
            setDefaultImage(mod, metadata.getId());
        }

        setModExternalLink(mod, metadata);
        mod.getTags().add(HFNLI18n.getString("hfnl.version") + ":" + metadata.getVersion());
        mod.setTitle(metadata.getName());
        mod.setSubtitle(metadata.getDescription());
    }

    private void setDefaultImage(IconedTwoLineListItem mod, String modId) {
        switch (modId) {
            case "hfnl":
                mod.setImage(FXUtils.newBuiltinImage("/assets/img/furnace.png"));
                break;
            case "mixinextras":
                mod.setImage(FXUtils.newBuiltinImage("/assets/img/chest.png"));
                break;
            default:
                mod.setImage(FXUtils.newBuiltinImage("/assets/img/craft_table.png"));
        }
    }

    private void setModExternalLink(IconedTwoLineListItem mod, ModMetadata metadata) {
        metadata.getContact().get("homepage").ifPresentOrElse(mod::setExternalLink, () -> {
            if ("hmcl".equals(metadata.getId())) {
                mod.setExternalLink("https://github.com/HMCL-dev/HMCL");
            }
        });
    }
}