package hfnl.mod.mixin;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.LoggerContext;
import org.apache.logging.log4j.core.appender.FileAppender;
import org.apache.logging.log4j.core.config.Configuration;
import org.jackhuang.hmcl.Metadata;
import org.jackhuang.hmcl.util.logging.Level;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.spongepowered.asm.mixin.*;

import java.io.*;
import java.nio.file.Path;

@Mixin(org.jackhuang.hmcl.util.logging.Logger.class)
public class LoggerMixin {
    @Shadow @Final public static org.jackhuang.hmcl.util.logging.Logger LOG;
    @Unique
    private static final Logger LOGGER = LoggerFactory.getLogger("HMCL");

    @Overwrite
    private void onExit() {
    }

    @Overwrite
    public void start(Path path) {
    }

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

    @Overwrite
    public Path getLogFile() {
        LoggerContext ctx = (LoggerContext) LogManager.getContext(false);
        Configuration config = ctx.getConfiguration();
        FileAppender fileAppender = config.getAppender("File");

        return Path.of(Metadata.CURRENT_DIRECTORY + File.separator + fileAppender.getFileName());
    }

    @Overwrite
    public void exportLogs(OutputStream output) throws IOException {
        String logFilePath = String.valueOf(getLogFile().toAbsolutePath());

        File logFile = new File(logFilePath);

        try (InputStream in = new BufferedInputStream(new FileInputStream(logFile))) {
            byte[] buffer = new byte[4096];
            int bytesRead;
            while ((bytesRead = in.read(buffer)) != -1) {
                output.write(buffer, 0, bytesRead);
            }
        }
    }

}
