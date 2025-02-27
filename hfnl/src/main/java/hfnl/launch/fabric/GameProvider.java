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
import net.fabricmc.loader.impl.util.SystemProperties;
import net.fabricmc.loader.impl.util.version.StringVersion;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

public class GameProvider implements net.fabricmc.loader.impl.game.GameProvider {
    private static final String[] ENTRY_POINTS = {"org.jackhuang.hmcl.Main"};
    private static final Set<String> SENSITIVE_ARGS = new HashSet<>();
    private static final StringVersion GAME_VERSION = new StringVersion("0.0.0");
    private static final GameTransformer TRANSFORMER = new GameTransformer(new EntrypointPatch());

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
        return GAME_VERSION.getFriendlyString();
    }

    @Override
    public String getNormalizedGameVersion() {
        return getRawGameVersion();
    }

    @Override
    public Collection<BuiltinMod> getBuiltinMods() {
        HashMap<String,String> contactHashmap = new HashMap<>();
        contactHashmap.put("homepage","https://github.com/HMCL-dev/HMCL");
        BuiltinModMetadata metadata = (BuiltinModMetadata) new BuiltinModMetadata.Builder(getGameId(), getNormalizedGameVersion())
                .setName(getGameName())
                .addAuthor("HMCL", new HashMap<>())
                .setContact(new ContactInformationImpl(contactHashmap))
                .setDescription("Hello Minecraft! Launcher")
                .addIcon(0,"assets/img/icon.png")
                .build();
        return Collections.singletonList(new BuiltinMod(Collections.singletonList(gameJar), metadata));
    }

    @Override
    public String getEntrypoint() {
        return entryPoint;
    }

    @Override
    public Path getLaunchDirectory() {
        return Paths.get(".");
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
        processArguments(arguments);
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

        String[] ret = arguments.toArray();
        if (!sanitize) {
            return ret;
        }

        int writeIdx = 0;

        for (int i = 0; i < ret.length; i++) {
            String arg = ret[i];

            if (i + 1 < ret.length
                    && arg.startsWith("--")
                    && SENSITIVE_ARGS.contains(arg.substring(2).toLowerCase(Locale.ENGLISH))) {
                i++;
            } else {
                ret[writeIdx++] = arg;
            }
        }

        if (writeIdx < ret.length) {
            ret = Arrays.copyOf(ret, writeIdx);
        }

        return ret;
    }

    private void processArguments(Arguments arguments) {
        if (!arguments.containsKey("gameDir")) {
            arguments.put("gameDir", getLaunchDirectory(arguments).toAbsolutePath().normalize().toString());
        }
    }

    private static Path getLaunchDirectory(Arguments arguments) {
        return Paths.get(arguments.getOrDefault("gameDir", "."));
    }

    private Path locateGameJar() {
        String gameJarProperty = System.getProperty(SystemProperties.GAME_JAR_PATH);
        Path path = Paths.get(gameJarProperty);
        if (!Files.exists(path)) {
            throw new RuntimeException("Game jar configured through " + SystemProperties.GAME_JAR_PATH + " system property doesn't exist");
        }
        return path;
    }
}



