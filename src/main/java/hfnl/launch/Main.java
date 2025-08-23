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
import net.fabricmc.api.EnvType;
import net.fabricmc.loader.impl.game.minecraft.Slf4jLogHandler;
import net.fabricmc.loader.impl.launch.knot.Knot;
import net.fabricmc.loader.impl.util.log.Log;

public class Main {
    public static void main(String[] args) {
        Log.init(new Slf4jLogHandler());

        System.getProperties().put("fabric.development", "true");

        Knot.launch(args, EnvType.CLIENT);
    }
}




