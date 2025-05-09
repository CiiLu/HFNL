package hfnl;

import org.apache.log4j.Level;
import org.apache.log4j.PatternLayout;
import org.apache.log4j.spi.LoggingEvent;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class ColorPatternLayout extends PatternLayout {
    private static final String ANSI_RESET = "\033[0m";
    private static final String ANSI_BLACK = "\033[30m";
    private static final String ANSI_RED = "\033[31m";
    private static final String ANSI_GREEN = "\033[32m";
    private static final String ANSI_YELLOW = "\033[33m";
    private static final String ANSI_BLUE = "\033[34m";
    private static final String ANSI_WHITE = "\033[37m";

    private final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");

    private static final Map<Integer, String> LEVEL_COLOR_MAP = new HashMap<>();

    static {
        LEVEL_COLOR_MAP.put(Level.TRACE_INT, ANSI_BLACK);
        LEVEL_COLOR_MAP.put(Level.DEBUG_INT, ANSI_BLUE);
        LEVEL_COLOR_MAP.put(Level.INFO_INT, ANSI_GREEN);
        LEVEL_COLOR_MAP.put(Level.WARN_INT, ANSI_YELLOW);
        LEVEL_COLOR_MAP.put(Level.ERROR_INT, ANSI_RED);
        LEVEL_COLOR_MAP.put(Level.FATAL_INT, ANSI_RED);
    }

    @Override
    public String format(LoggingEvent event) {
        StringBuilder sbuf = new StringBuilder(128);

        // 格式化时间戳
        String formattedDate = dateFormat.format(new Date(event.timeStamp));
        sbuf.append(ANSI_BLUE)
                .append("[")
                .append(formattedDate)
                .append("]")
                .append(ANSI_RESET);

        // 获取日志级别颜色并格式化日志头
        Level level = event.getLevel();
        String levelColor = LEVEL_COLOR_MAP.getOrDefault(level.toInt(), ANSI_WHITE);

        sbuf.append(levelColor)
                .append(" [")
                .append(event.getThreadName())
                .append("/")
                .append(level)
                .append("]")
                .append(ANSI_RESET);

        // 添加日志消息
        sbuf.append(" ")
                .append(event.getRenderedMessage())
                .append("\n");

        return sbuf.toString();
    }
}