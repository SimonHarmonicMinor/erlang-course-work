import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;
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
            log.sendMessage("Created node with cookie " + mainNode.cookie());
        } catch (IOException exc) {
            exc.printStackTrace();
            log.sendMessage("Unable to create node. Is EPMD working?");
            return;
        }
        try {
            remotePeer = new OtpPeer(Defines.nodeNameTarget + "@" + Defines.hostNameTarget);
            log.sendMessage("Connected to remote peer " + remotePeer.toString());
        } catch (Exception exc) {
            log.sendMessage("Unable to connect to remote node.");
            return;
        }
        for (int i = 0; i < count; i++) {
            processes.add(new ClientProcess(mainNode, remotePeer));
        }
        log.sendMessage("Launching processes...");
        for (ClientProcess process : processes) {
            process.setDisplayName(Namegen.getRandomName());
            process.start();
        }
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        LogCenter logCenter = LogCenter.getInstance();
        logCenter.installLogger(new ConsoleLogger());
        logCenter.installLogger(new FileLogger("ClientGen.log"));
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

    private static class Namegen {
        private static final Random random = new Random();
        private static final String[] names = new String[]{
                "Ivan", "Petr", "Fedor", "Mikhail", "Roman", "Artem", "Semyon", "Konstantin", "Aleksei", "Vladimir",
                "Nikita", "Timofei"
        };
        private static final String[] namesF = new String[]{
                "Olga", "Maria", "Marina", "Ekaterina", "Tatyana", "Natalya", "Aleksandra", "Valeriya", "Svetlana",
                "Elena", "Elizaveta", "Irina", "Viktoriya", "Vasilina", "Olesya"
        };
        private static final String[] surnames = new String[]{
                "Ivanov", "Petrov", "Sidorov", "Fedorov", "Semyonov", "Romanov", "Alekseev", "Vladimirov", "Nikitin",
                "Makushin", "Scherbakov", "Kirekov", "Inyushin", "Krasnoslabodtsev", "Siukha", "Ovsyannikov", "Basarab", "Hyshov",
                "Ananyev", "Fast", "Lukin", "Oreschenko", "Salomatov"
        };
        private static final String[] surnamesF = new String[]{
                "Ivanova", "Petrova", "Sidorova", "Fedorova", "Semyonova", "Romanova", "Alekseeva", "Nikitina",
                "Volkova", "Matveeva", "Pischulina", "Mukhameddinova", "Popova", "Vorobyeva", "Varzanova"
        };
        public static String getRandomName() {
            boolean isF = (random.nextDouble() < 0.5);
            return isF
                    ? (namesF[random.nextInt(namesF.length)] + " " + surnamesF[random.nextInt(surnamesF.length)])
                    : (names[random.nextInt(names.length)] + " " + surnames[random.nextInt(surnames.length)]);
        }
    }
}
