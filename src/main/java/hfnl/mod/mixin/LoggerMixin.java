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

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.LoggerContext;
import org.apache.logging.log4j.core.appender.FileAppender;
import org.apache.logging.log4j.core.config.Configuration;
import org.jackhuang.hmcl.Metadata;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Unique;

import java.io.*;
import java.nio.file.Path;

@Mixin(org.jackhuang.hmcl.util.logging.Logger.class)
public abstract class LoggerMixin {
    @Unique
    private static final Logger LOGGER = LoggerFactory.getLogger("HMCL");

    /**
     * @author CiiLu
     * @reason [
     */
    @Overwrite
    private void onExit() {
    }

    /**
     * @author CiiLu
     * @reason [
     */
    @Overwrite
    public void start(Path path) {
    }

    /**
     * @author CiiLu
     * @reason [
     */
    @Overwrite
    private void log(System.Logger.Level level, String caller, String msg, Throwable exception) {
        if (level == System.Logger.Level.ERROR) {
            LOGGER.error(msg, exception);
        } else if (level == System.Logger.Level.INFO) {
            LOGGER.info(msg, exception);
        } else if (level == System.Logger.Level.DEBUG) {
            LOGGER.debug(msg, exception);
        } else if (level == System.Logger.Level.TRACE) {
            LOGGER.trace(msg, exception);
        } else if (level == System.Logger.Level.WARNING) {
            LOGGER.warn(msg, exception);
        }
    }

    /**
     * @author CiiLu
     * @reason [
     */
    @Overwrite
    public Path getLogFile() {
        LoggerContext ctx = (LoggerContext) LogManager.getContext(false);
        Configuration config = ctx.getConfiguration();
        FileAppender fileAppender = config.getAppender("File");

        return Path.of(Metadata.CURRENT_DIRECTORY + File.separator + fileAppender.getFileName());
    }

    /**
     * @author CiiLu
     * @reason [
     */
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
