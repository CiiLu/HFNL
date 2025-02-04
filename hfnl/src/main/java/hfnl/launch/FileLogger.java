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

import org.spongepowered.asm.logging.LoggerAdapterConsole;

import java.io.*;

public class FileLogger extends LoggerAdapterConsole {
    private final FileWriter fileWriter;
    private final PrintStream originalOut;
    private final PrintStream originalErr;

    public FileLogger(String id, String filePath) throws IOException {
        super(id);

        File logDir = new File(filePath).getParentFile();
        if (!logDir.exists()) {
            boolean created = logDir.mkdirs();
            if (!created) {
                throw new IOException("Failed to create log directory: " + logDir.getAbsolutePath());
            }
        }
        this.fileWriter = new FileWriter(filePath, true);

        this.originalOut = System.out;
        this.originalErr = System.err;

        System.setOut(new PrintStream(new MultiOutputStream(this.fileWriter, originalOut)));
        System.setErr(new PrintStream(new MultiOutputStream(this.fileWriter, originalErr)));
    }

    @Override
    public String getType() {
        return "File Logger";
    }

    public void close() {
        try {
            fileWriter.close();
            System.setOut(originalOut);
            System.setErr(originalErr);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static class MultiOutputStream extends OutputStream {
        private final Writer writer;
        private final OutputStream out;

        public MultiOutputStream(Writer writer, OutputStream out) {
            this.writer = writer;
            this.out = out;
        }

        @Override
        public void write(int b) throws IOException {
            writer.write(b);
            out.write(b);
        }

        @Override
        public void flush() throws IOException {
            writer.flush();
            out.flush();
        }

        @Override
        public void close() throws IOException {
            writer.close();
            out.close();
        }
    }
}