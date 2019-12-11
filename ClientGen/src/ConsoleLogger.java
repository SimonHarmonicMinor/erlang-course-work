import java.text.SimpleDateFormat;
import java.util.Date;

public class ConsoleLogger implements Logger {

    private final SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss.SSS");
    @Override
    public void sendMessage(String message) {
        System.out.println(format.format(new Date()) + " " + message);
    }
}
