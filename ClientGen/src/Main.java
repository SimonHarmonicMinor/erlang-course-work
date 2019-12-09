import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;
import com.ericsson.otp.erlang.*;

public class Main {
    private static final int maxActions = 5;
    private static int numProcesses = 0;
    private static ArrayList<ClientProcess> processes = new ArrayList<>();

    public static void launchProcesses(int count) {
        LogCenter log = LogCenter.getInstance();
        OtpNode mainNode = null;
        OtpPeer remotePeer = null;
        ArrayList<OtpMbox> mailboxes = new ArrayList<>();
        OtpSelf self;
        try {
            mainNode = new OtpNode("me");
            self = new OtpSelf("client");
            log.sendMessage("Created node with cookie " + mainNode.cookie());
        } catch (IOException exc) {
            exc.printStackTrace();
            log.sendMessage("Unable to create node. Is EPMD working?");
            return;
        }
        try {
            remotePeer = new OtpPeer("bank_app");
        } catch (Exception exc) {
            return;
        }
        for (int i = 0; i < count; i++) {
            processes.add(new ClientProcess(mainNode, remotePeer));
        }
        log.sendMessage("Launching processes...");
        for (ClientProcess process : processes) {
            process.start();
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
        launchProcesses(numProcesses);
    }
}
