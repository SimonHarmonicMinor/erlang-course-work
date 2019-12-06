import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import com.ericsson.otp.erlang.*;

public class Main {
    private static final String PATTERN_IPV4_PORT = "(\\b(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\b)(?:\\:(\\b(?:6553[0-5]|655[0-2][0-9]|65[0-4][0-9]{2}|6[0-4][0-9]{3}|[1-5]?[0-9]{1,4})\\b))?";
    private static int numProcesses = 0;
    private static ArrayList<ClientProcess> processes = new ArrayList<>();
    private static String address = "127.0.0.1";
    private static String port = "27502";

    public static void launchProcesses(int count) {
        LogCenter log = LogCenter.getInstance();
        OtpNode mainNode = null;
        ArrayList<OtpMbox> mailboxes = new ArrayList<>();
        try {
            mainNode = new OtpNode("me");
            log.sendMessage("Created node with cookie " + mainNode.cookie());
        } catch (IOException exc) {
            exc.printStackTrace();
            log.sendMessage("Unable to create node");
            return;
        }

        for (int i = 0; i < count; i++) {
            processes.add(new ClientProcess(mainNode));
        }
        log.sendMessage("Launching processes...");
        for (ClientProcess process : processes) {
            process.setAddress(address, port);
            //process.start();
        }
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        LogCenter logCenter = LogCenter.getInstance();
        logCenter.installLogger(new ConsoleLogger());
        do {
            System.out.print("How many processes? ");
            try {
                numProcesses = Integer.parseInt(scanner.nextLine());
            } catch (Exception exc) {
                exc.printStackTrace();
            }
        } while (numProcesses <= 0);
        System.out.print("Address [" + address + ":" + port + "]: ");
        String addrInput = scanner.nextLine();
        Matcher matcher = Pattern.compile(PATTERN_IPV4_PORT).matcher(addrInput);
        if (matcher.find()) {
            address = matcher.group(1);
            port = matcher.group(2) == null ? port : matcher.group(2);
        }
        logCenter.sendMessage("Set address to " + address + ":" + port);
        launchProcesses(numProcesses);
    }
}
