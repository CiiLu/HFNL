package hfnl.mod.page;

/*
 * Hello Minecraft! Launcher
 * Copyright (C) 2025 huangyuhui <huanghongxun2008@126.com> and contributors
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Control;
import javafx.scene.control.Skin;
import javafx.scene.control.SkinBase;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.ModContainer;
import org.jackhuang.hmcl.ui.FXUtils;
import org.jackhuang.hmcl.ui.ListPageBase;
import org.jackhuang.hmcl.ui.SVG;
import org.jackhuang.hmcl.ui.ToolbarListPageSkin;
import org.jackhuang.hmcl.ui.construct.RipplerContainer;
import org.jackhuang.hmcl.ui.construct.TwoLineListItem;

import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.jackhuang.hmcl.ui.FXUtils.runInFX;
import static org.jackhuang.hmcl.util.i18n.I18n.i18n;

public final class ModsListPage extends ListPageBase<ModsListPage.ModItem> {
    public ModsListPage() {
        super();
        refresh();
    }

    @Override
    protected Skin<?> createDefaultSkin() {
        return new ModsListPageSkin();
    }

    public void refresh() {
        setLoading(true);

        List<ModContainer> mods = FabricLoader.getInstance().getAllMods().stream().sorted(Comparator.comparing(modContainer -> modContainer.getMetadata().getName())).collect(Collectors.toList());

        runInFX(() -> {
            itemsProperty().clear();
            mods.forEach((mod) -> itemsProperty().add(new ModItem(mod)));
            setLoading(false);
        });
    }

    private void openFolder() {
        FXUtils.openFolder(FabricLoader.getInstance().getGameDir().resolve("mods").toFile());
    }

    public static final class ItemSkin extends SkinBase<ModItem> {
        public ItemSkin(ModItem control) {
            super(control);

            FXUtils.setLimitHeight(control, 50);

            BorderPane root = new BorderPane();
            root.getStyleClass().add("md-list-cell");
            root.setPadding(new Insets(8));

            StackPane left = new StackPane();
            left.setMaxSize(32, 32);
            left.setPrefSize(32, 32);

            left.getChildren().add(new ImageView(control.getIcon()));
            left.setPadding(new Insets(0, 8, 0, 0));

            BorderPane.setAlignment(left, Pos.CENTER);
            root.setLeft(left);

            TwoLineListItem center = new TwoLineListItem();
            center.setTitle(control.getName());
            center.setSubtitle(control.getDescription());
            center.getTags().add("ID: " + control.mod.getMetadata().getId());
            center.getTags().add(i18n("java.info.version") + ":" + control.mod.getMetadata().getVersion());
            root.setCenter(center);

            RipplerContainer container = new RipplerContainer(root);

            this.getChildren().add(container);
        }
    }

    public static final class ModItem extends Control {
        private final ModContainer mod;

        private ModItem(ModContainer mod) {
            this.mod = mod;
        }

        @Override
        protected Skin<?> createDefaultSkin() {
            return new ItemSkin(this);
        }

        public String getName() {
            if (Objects.equals(mod.getMetadata().getId(), "java")) {
                return "Java";
            } else {
                return mod.getMetadata().getName();
            }
        }

        public String getDescription() {
            if (Objects.equals(mod.getMetadata().getId(), "java")) {
                return System.getProperty("java.runtime.name");
            } else {
                return mod.getMetadata().getDescription();
            }
        }

        public Image getIcon() {
            Optional<String> iconPath = mod.getMetadata().getIconPath(0);
            return FXUtils.newBuiltinImage(iconPath.orElse(Objects.equals(mod.getMetadata().getId(), "mixinextras") ? "/assets/img/chest.png" : "/assets/img/craft_table.png"));
        }
    }

    private final class ModsListPageSkin extends ToolbarListPageSkin<ModsListPage> {
        ModsListPageSkin() {
            super(ModsListPage.this);
        }

        @Override
        protected List<Node> initializeToolbar(ModsListPage skinnable) {
            return List.of(createToolbarButton2(i18n("button.refresh"), SVG.REFRESH, skinnable::refresh), createToolbarButton2(i18n("folder.mod"), SVG.FOLDER, skinnable::openFolder));
        }
    }
}