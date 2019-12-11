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
        log.sendMessage("Created process with UIID = " + id.toString() + "and User ID = " + userNr);
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
        for (int i = 0; i < maxActions; i++) {
            int msgCode = random.nextInt(4);
            log.sendMessage("[" + showDisplayName() + "] Sending " + messageTypes[msgCode]);
            try {
                Thread.sleep(1000 + random.nextInt(2001));
                OtpErlangObject[] arr = new OtpErlangObject[2];
                arr[0] = new OtpErlangTuple(new OtpErlangObject[]{
                        new OtpErlangAtom("bank_server"),
                        new OtpErlangAtom(Defines.nodeNameTarget + "@" + Defines.hostNameTarget)
                });
                switch (msgCode) {
                    case 0: {
                        arr[1] = new OtpErlangTuple(new OtpErlangObject[]{
                                new OtpErlangAtom("CHECK_BALANCE"),
                                new OtpErlangInt(userNr)
                        });
                        break;
                    }
                    case 1: {
                        int amount = random.nextInt(10000000);
                        log.sendMessage("[" + showDisplayName() + "] With amount: " + amount);
                        arr[1] = new OtpErlangTuple(new OtpErlangObject[]{
                                new OtpErlangAtom("WITHDRAW_MONEY"),
                                new OtpErlangInt(userNr),
                                new OtpErlangInt(amount)
                        });
                        break;
                    }
                    case 2: {
                        int amount = random.nextInt(10000000);
                        log.sendMessage("[" + showDisplayName() + "] With amount: " + amount);
                        arr[1] = new OtpErlangTuple(new OtpErlangObject[]{
                                new OtpErlangAtom("PUT_MONEY"),
                                new OtpErlangInt(userNr),
                                new OtpErlangInt(amount)
                        });
                        break;
                    }
                    case 3: {
                        int amount = random.nextInt(10000000);
                        log.sendMessage("[" + showDisplayName() + "] With amount: " + amount);
                        int targetId = random.nextInt(1000);
                        log.sendMessage("[" + showDisplayName() + "] With target: " + targetId);
                        arr[1] = new OtpErlangTuple(new OtpErlangObject[]{
                                new OtpErlangAtom("TRANSFER_MONEY"),
                                new OtpErlangInt(userNr),
                                new OtpErlangInt(targetId),
                                new OtpErlangInt(amount)
                        });
                        break;
                    }
                }
                connection.sendRPC("gen_server", "call", arr);
                OtpErlangObject response = connection.receive();
                if (response instanceof OtpErlangString) {
                    log.sendMessage("[" + showDisplayName() + "] Got " + ((OtpErlangString) response).stringValue());
                } else if (response instanceof OtpErlangTuple) {
                    OtpErlangTuple responseTuple = (OtpErlangTuple) response;
                    if (responseTuple.elements().length >= 2 && responseTuple.elements()[0].equals(Defines.responseTupleHeader)) {
                        OtpErlangObject responseContent = responseTuple.elements()[1];
                        log.sendMessage("[" + showDisplayName() + "] Got " + responseContent.toString());
                    } else {
                        log.sendMessage("[" + showDisplayName() + "] Got " + response.toString());
                    }
                }
            } catch (Exception exc) {
                exc.printStackTrace();
                log.sendMessage("Unable to perform request.");
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
                    Thread.sleep(random.nextInt(10000));
                    self = new OtpSelf(id.toString());
                    connection = self.connect(remotePeer);
                } catch (Exception e) {
                    e.printStackTrace();
                    log.sendMessage("[" + showDisplayName() + "] Unable to launch client process.");
                    return;
                }
                work();
            }
        });
        thread.start();

    }
}
