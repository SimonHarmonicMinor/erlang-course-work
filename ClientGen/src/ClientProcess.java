import com.ericsson.otp.erlang.*;

import java.io.*;
import java.util.Random;
import java.util.UUID;

public class ClientProcess {
    private OtpNode rootNode;
    private OtpSelf self;
    private OtpPeer remotePeer;
    private OtpConnection connection;
    private OtpMbox mailbox;
    private Random random;
    private int maxActions;
    private LogCenter log;
    private UUID id;
    private String displayName = null;
    private String[] messageTypes = new String[]{
            "CHECK_BALANCE",
            "WITHDRAW_MONEY",
            "PUT_MONEY",
            "TRANSFER_MONEY"
    };

    private int userNr;

    public ClientProcess(OtpNode root, OtpPeer peer) {
        log = LogCenter.getInstance();
        maxActions = Integer.MAX_VALUE;
        random = new Random();
        userNr = random.nextInt(1000);
        rootNode = root;
        remotePeer = peer;
        id = UUID.randomUUID();
        log.sendMessage("Created process with ID = " + id.toString());
    }

    public String getDisplayName() {
        return displayName;
    }

    public String showDisplayName() {
        return (displayName != null ? displayName : id.toString());
    }

    public void setDisplayName(String displayName) {
        if (displayName != null)
            log.sendMessage(id.toString() + " is " + displayName + " now.");
        this.displayName = displayName;
    }
    public void setMaxActions(int maxActions) {
        this.maxActions = maxActions;
    }

    private void work() {
        // TODO: reimplement work method
        for (int i = 0; i < maxActions; i++) {
            int msgCode = random.nextInt(4);
            log.sendMessage("[" + showDisplayName() + "] Sending " + messageTypes[msgCode]);
            try {
                switch (msgCode) {
                    case 0: {
                        connection.sendRPC("bankServerLib", "check_balance", new OtpErlangList(new OtpErlangObject[]{
                            new OtpErlangInt(userNr)
                        }));

                    }
                    case 1: {
                        connection.sendRPC("bankServerLib", "withdraw_money", new OtpErlangList(new OtpErlangObject[]{
                                new OtpErlangInt(random.nextInt(1000)),
                                new OtpErlangFloat((float) (random.nextDouble() * (1000000.0 - 100.0) + 100.0))
                        }));
                    }
                    case 2: {
                        connection.sendRPC("bankServerLib", "put_money", new OtpErlangList(new OtpErlangObject[]{
                                new OtpErlangInt(random.nextInt(1000)),
                                new OtpErlangFloat((float) (random.nextDouble() * (1000000.0 - 100.0) + 100.0))
                        }));
                    }
                    case 3: {
                        connection.sendRPC("bankServerLib", "transfer_money", new OtpErlangList(new OtpErlangObject[]{
                                new OtpErlangInt(random.nextInt(1000)),
                                new OtpErlangInt(random.nextInt(1000)),
                                new OtpErlangFloat((float) (random.nextDouble() * (1000000.0 - 100.0) + 100.0))
                        }));
                    }
                }
                OtpErlangObject response = connection.receiveRPC();
                if (response instanceof OtpErlangString) {
                    log.sendMessage("[" + showDisplayName() + "] Got " + ((OtpErlangString) response).stringValue());
                }
            } catch (IOException exc) {
                exc.printStackTrace();
                log.sendMessage("Unable to perform request.");
            } catch (OtpErlangExit | OtpAuthException otpErlangExit) {
                otpErlangExit.printStackTrace();
            }
        }
        log.sendMessage("[" + showDisplayName() + "] Actions limit exceeded.");
    }

    public void start(){
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                mailbox = rootNode.createMbox();
                try {
                    self = new OtpSelf(mailbox.getName());
                    connection = self.connect(remotePeer);
                } catch (IOException | OtpAuthException e) {
                    e.printStackTrace();
                }
                work();
            }
        });
        thread.start();

    }
}
