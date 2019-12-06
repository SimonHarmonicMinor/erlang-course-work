import com.ericsson.otp.erlang.*;

import java.util.UUID;

public class ClientProcess {
    private OtpNode rootNode;
    private OtpMbox mailbox;
    private String address = "127.0.0.1", port = "27502";
    private LogCenter log;
    private UUID id;
    private String displayName = null;

    public ClientProcess(OtpNode root) {
        log = LogCenter.getInstance();
        rootNode = root;
        id = UUID.randomUUID();
        log.sendMessage("Created node with ID = " + id.toString());
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setAddress(String address) {
        this.address = address;
    }
    public void setAddress(String address, String port) {
        this.address = address;
    }
    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public void start() {
        mailbox = rootNode.createMbox();
    }
}
