package hfnl.launch.utils;

import java.io.*;

public class FileLogger extends PrintStream {
    private final PrintStream consoleStream;
    private final PrintStream fileStream;

    public FileLogger(PrintStream console, File file) throws FileNotFoundException {
        super(new ByteArrayOutputStream());
        this.consoleStream = console;
        this.fileStream = new PrintStream(new FileOutputStream(file));
    }

    @Override
    public void write(byte[] buf, int off, int len) {
        consoleStream.write(buf, off, len);
        fileStream.write(buf, off, len);
    }

    @Override
    public void println(String x) {
        consoleStream.println(x);
        fileStream.println(x);
    }

    @Override
    public void close() {
        consoleStream.close();
        fileStream.close();
    }

    @Override
    public void flush() {
        consoleStream.flush();
        fileStream.flush();
    }
}