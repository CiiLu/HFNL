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

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.loader.impl.FabricLoaderImpl;

import java.nio.file.Paths;

public final class Hooks {

    public static void init() {
        FabricLoaderImpl fabricLoader = FabricLoaderImpl.INSTANCE;
        fabricLoader.prepareModInit(Paths.get("."), fabricLoader.getGameInstance());
        fabricLoader.invokeEntrypoints("main", ModInitializer.class, ModInitializer::onInitialize);
        fabricLoader.invokeEntrypoints("client", ClientModInitializer.class, ClientModInitializer::onInitializeClient);
    }
}