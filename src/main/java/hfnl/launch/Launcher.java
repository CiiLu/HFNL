package hfnl.launch;

import net.fabricmc.api.EnvType;
import net.fabricmc.loader.impl.launch.knot.Knot;

public class Launcher {
    public void run(String[] args) {
        Knot.launch(args, EnvType.CLIENT);
    }
}
