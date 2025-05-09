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
package hfnl.launch.fabric;

import net.fabricmc.loader.impl.FormattedException;
import net.fabricmc.loader.impl.game.GameProviderHelper;
import net.fabricmc.loader.impl.game.patch.GameTransformer;
import net.fabricmc.loader.impl.launch.FabricLauncher;
import net.fabricmc.loader.impl.metadata.BuiltinModMetadata;
import net.fabricmc.loader.impl.metadata.ContactInformationImpl;
import net.fabricmc.loader.impl.util.Arguments;
import net.fabricmc.loader.impl.util.log.Log;
import net.fabricmc.loader.impl.util.log.LogHandler;
import org.jackhuang.hmcl.Metadata;
import org.jackhuang.hmcl.util.io.JarUtils;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class GameProvider implements net.fabricmc.loader.impl.game.GameProvider {
    private static final String[] ENTRY_POINTS = {"org.jackhuang.hmcl.Main"};
    private static final GameTransformer TRANSFORMER = new GameTransformer(new EntrypointPatch());
    private static String GAME_VERSION = Metadata.VERSION;
    private Arguments arguments;
    private String entryPoint;
    private Path gameJar;

    @Override
    public String getGameId() {
        return "hmcl";
    }

    @Override
    public String getGameName() {
        return "HMCL";
    }

    @Override
    public String getRawGameVersion() {
        return GAME_VERSION;
    }

    @Override
    public String getNormalizedGameVersion() {
        return getRawGameVersion();
    }

    @Override
    public Collection<BuiltinMod> getBuiltinMods() {
        HashMap<String, String> contactHashmap = new HashMap<>();
        contactHashmap.put("homepage", "https://github.com/HMCL-dev/HMCL");
        BuiltinModMetadata metadata = (BuiltinModMetadata) new BuiltinModMetadata.Builder(getGameId(), getNormalizedGameVersion())
                .setName(getGameName())
                .addAuthor("HMCL-dev", new HashMap<>())
                .setContact(new ContactInformationImpl(contactHashmap))
                .setDescription("Hello Minecraft! Launcher")
                .addIcon(0, "assets/img/icon.png")
                .build();
        return Collections.singletonList(new BuiltinMod(Collections.singletonList(gameJar), metadata));
    }

    @Override
    public String getEntrypoint() {
        return entryPoint;
    }

    @Override
    public Path getLaunchDirectory() {
        return Paths.get("./");
    }

    @Override
    public boolean isObfuscated() {
        return false;
    }

    @Override
    public boolean requiresUrlClassLoader() {
        return false;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public boolean locateGame(FabricLauncher launcher, String[] args) {
        arguments = new Arguments();
        arguments.parse(args);

        gameJar = locateGameJar();

        try {
            Map<Path, java.util.zip.ZipFile> zipFiles = new HashMap<>();
            GameProviderHelper.FindResult result = GameProviderHelper.findFirst(Collections.singletonList(gameJar), zipFiles, true, ENTRY_POINTS);
            if (result == null) {
                return false;
            }
            entryPoint = result.name;
        } catch (Exception e) {
            throw new FormattedException("Error locating game", e);
        }
        return true;
    }

    @Override
    public void initialize(FabricLauncher launcher) {
        launcher.addToClassPath(gameJar);
        TRANSFORMER.locateEntrypoints(launcher, Collections.singletonList(gameJar));
    }

    @Override
    public GameTransformer getEntrypointTransformer() {
        return TRANSFORMER;
    }

    @Override
    public void unlockClassPath(FabricLauncher launcher) {
        launcher.addToClassPath(gameJar);
    }

    @Override
    public void launch(ClassLoader loader) {
        try {
            Class<?> mainClass = loader.loadClass(entryPoint);
            Method mainMethod = mainClass.getMethod("main", String[].class);
            mainMethod.invoke(null, (Object) arguments.toArray());
        } catch (InvocationTargetException e) {
            throw new FormattedException("The game has crashed!", e.getCause());
        } catch (ReflectiveOperationException e) {
            throw new FormattedException("Failed to start the game", e);
        }
    }

    @Override
    public Arguments getArguments() {
        return arguments;
    }

    @Override
    public String[] getLaunchArguments(boolean sanitize) {
        if (arguments == null) {
            return new String[0];
        }
        return arguments.toArray();
    }

    private Path locateGameJar() {
        return JarUtils.thisJarPath();
    }
}



