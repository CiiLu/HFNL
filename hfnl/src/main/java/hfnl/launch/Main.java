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

package hfnl.launch;

import hfnl.launch.utils.FileLogger;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
/*
* /*

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
*/


public class Main {
    public static void main(String[] args) throws Exception {
        int choice = JOptionPane.showConfirmDialog(null, "本程序为 Hello Minecraft! Launcher 添加了 Fabric Loader 支持。\n如果你不知道你在做什么，请立即退出此程序并下载官方版 HMCL！\n是否继续？", "注意", JOptionPane.YES_NO_OPTION);
        if(choice != JOptionPane.YES_OPTION){
            return;
        }

        System.getProperties().putIfAbsent("fabric.development", "true");
        String time = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH-mm-ss"));
        File logPath = new File("./logs/");

        if (!logPath.exists()) {
            logPath.mkdirs();
        }

        File logFile = new File("logs/" + time + ".log");

        try (FileLogger logger = new FileLogger(System.out, logFile)) {
            System.setErr(logger);
            System.setOut(logger);
            new Launcher().run(args);
        }
    }
}




