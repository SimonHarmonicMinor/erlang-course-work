import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;

public class FileLogger implements Logger {
    File file = null;
    FileWriter fileWriter = null;
    BufferedWriter bufferedWriter = null;
    private final SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss.SSS");

    public FileLogger(String path) {
        file = Paths.get(path).toFile();
        try {
            if (!file.exists()) {
                file.createNewFile();
            }
            fileWriter = new FileWriter(file);
            bufferedWriter = new BufferedWriter(fileWriter);
        } catch (IOException exc) {

        }
    }

    @Override
    public void sendMessage(String message) {
        if (bufferedWriter != null) {
            try {
                bufferedWriter.write(format.format(new Date()) + " " + message + "\n");
                bufferedWriter.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
