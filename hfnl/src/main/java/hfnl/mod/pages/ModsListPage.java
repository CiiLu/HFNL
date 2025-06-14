package hfnl.mod.pages;

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
import javafx.scene.Node;
import javafx.scene.control.Control;
import javafx.scene.control.Skin;
import javafx.scene.control.SkinBase;
import javafx.scene.layout.BorderPane;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.ModContainer;
import net.fabricmc.loader.api.metadata.ModMetadata;
import org.jackhuang.hmcl.task.Task;
import org.jackhuang.hmcl.ui.FXUtils;
import org.jackhuang.hmcl.ui.ListPageBase;
import org.jackhuang.hmcl.ui.SVG;
import org.jackhuang.hmcl.ui.ToolbarListPageSkin;
import org.jackhuang.hmcl.ui.construct.IconedTwoLineListItem;
import org.jackhuang.hmcl.ui.construct.RipplerContainer;

import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.jackhuang.hmcl.ui.FXUtils.runInFX;
import static org.jackhuang.hmcl.ui.FXUtils.setLimitHeight;
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

        Task.runAsync(() -> {
            runInFX(() -> {
                itemsProperty().clear();

                mods.forEach((mod) -> itemsProperty().add(new ModItem(mod)));
            });

        }).whenComplete((a) -> {
            runInFX(() -> setLoading(false));
        }).start();

    }

    public static final class ItemSkin extends SkinBase<ModItem> {
        public ItemSkin(ModItem control) {
            super(control);

            setLimitHeight(control, 50);

            BorderPane root = new BorderPane();
            root.getStyleClass().add("md-list-cell");
            root.setPadding(new Insets(8));

            root.setCenter(createModItem(control.mod));

            getChildren().setAll(new RipplerContainer(root));
        }

        private Node createModItem(ModContainer modContainer) {
            ModMetadata metadata = modContainer.getMetadata();
            String modid = metadata.getId();

            if ("java".equals(modid)) {
                return createJavaMod();
            } else {
                return createMod(metadata);
            }
        }

        private Node createJavaMod() {
            IconedTwoLineListItem mod = new IconedTwoLineListItem();
            mod.setExternalLink(System.getProperty("java.vendor.url"));
            mod.getTags().add("ID: java");
            mod.getTags().add(i18n("java.info.version") + ": " + System.getProperty("java.version"));
            mod.getTags().add(i18n("java.info.vendor") + ": " + System.getProperty("java.vendor"));
            mod.setTitle("Java");
            mod.setSubtitle(System.getProperty("java.runtime.name"));
            mod.setImage(FXUtils.newBuiltinImage("assets/img/command.png"));
            return mod;
        }


        private Node createMod(ModMetadata metadata) {
            Optional<String> iconPath = metadata.getIconPath(0);
            IconedTwoLineListItem mod = new IconedTwoLineListItem();

            mod.setExternalLink(metadata.getContact().get("homepage").orElse(""));
            mod.setImage(FXUtils.newBuiltinImage(iconPath.orElse(Objects.equals(mod.getId(), "mixinextras") ? "/assets/img/chest.png" : "/assets/img/craft_table.png")));
            mod.getTags().add("ID: " + metadata.getId());
            mod.getTags().add(i18n("java.info.version") + ":" + metadata.getVersion());
            mod.setTitle(metadata.getName());
            mod.setSubtitle(metadata.getDescription());

            return  mod;
        }
    }

    private final class ModsListPageSkin extends ToolbarListPageSkin<ModsListPage> {
        ModsListPageSkin() {
            super(ModsListPage.this);
        }

        @Override
        protected List<Node> initializeToolbar(ModsListPage skinnable) {
            return List.of(createToolbarButton2(i18n("button.refresh"), SVG.REFRESH, skinnable::refresh),
                    createToolbarButton2(i18n("folder.mod"), SVG.FOLDER, skinnable::openFolder));
        }
    }

    private void openFolder() {
        FXUtils.openFolder(FabricLoader.getInstance().getGameDir().resolve("mods").toFile());
    }

    public final class ModItem extends Control {
        final ModContainer mod;

        private ModItem(ModContainer mod) {
            this.mod = mod;
        }

        protected Skin<?> createDefaultSkin() {
            return new ItemSkin(this);
        }
    }
}
