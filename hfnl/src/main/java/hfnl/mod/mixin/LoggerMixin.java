package hfnl.mod.mixin;

import org.jackhuang.hmcl.util.logging.Level;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Unique;

import java.nio.file.Path;

@Mixin(org.jackhuang.hmcl.util.logging.Logger.class)
public class LoggerMixin {
    @Unique
    private static final Logger LOGGER = LoggerFactory.getLogger("HMCL");

    /**
     * @author
     * @reason
     */
    @Overwrite
    private void onExit() {
    }

    /**
     * @author
     * @reason
     */
    @Overwrite
    public void start(Path path) {
    }

    /**
     * @author
     * @reason
     */

    @Overwrite
    private void log(Level level, String caller, String msg, Throwable exception) {
        if (level == Level.ERROR) {
            LOGGER.error(msg, exception);
        } else if (level == Level.INFO) {
            LOGGER.info(msg, exception);
        } else if (level == Level.DEBUG) {
            LOGGER.debug(msg, exception);
        } else if (level == Level.TRACE) {
            LOGGER.trace(msg, exception);
        } else if (level == Level.WARNING) {
            LOGGER.warn(msg, exception);
        }
    }
}
