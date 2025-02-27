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

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDialogLayout;
import hfnl.mod.I18n;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import org.jackhuang.hmcl.ui.Controllers;
import org.jackhuang.hmcl.ui.construct.DialogCloseEvent;
import org.jackhuang.hmcl.ui.construct.JFXHyperlink;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Controllers.class)
public class ControllersMixin {
    @Inject(method = "initialize(Ljavafx/stage/Stage;)V", at = @At("RETURN"))
    private static void initializeInjecter(Stage stage, CallbackInfo ci) {
        JFXDialogLayout dialog = new JFXDialogLayout();

        dialog.setHeading(new Label("Hello Furnace Launcher"));
        dialog.setBody(new Label(I18n.get("hfnl.notice")));

        JFXHyperlink hmclRepoLink = new JFXHyperlink("HMCL Github");
        hmclRepoLink.setExternalLink("https://github.com/HMCL-dev/HMCL/");

        JFXButton yesButton = new JFXButton("确定");
        JFXButton noButton = new JFXButton("退出");

        yesButton.setOnAction(e -> dialog.fireEvent(new DialogCloseEvent()));

        noButton.setOnAction(e -> javafx.application.Platform.exit());

        dialog.setActions(hmclRepoLink, yesButton, noButton);

        Controllers.dialog(dialog);
    }
}
