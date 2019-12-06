import java.util.ArrayList;

public class LogCenter {
    private ArrayList<Logger> loggers;
    private static LogCenter instance = null;

    private LogCenter() {
        loggers = new ArrayList<>();
    }

    public static LogCenter getInstance() {
        if (instance == null)
            instance = new LogCenter();
        return instance;
    }

    public void sendMessage(String message) {
        for (Logger logger : loggers) {
            logger.sendMessage(message);
        }
    }

    public void installLogger(Logger logger) {
        if (logger != null) {
            loggers.add(logger);
        }
    }
}
