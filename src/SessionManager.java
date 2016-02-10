import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.UserInfo;

import java.util.concurrent.ThreadLocalRandom;

/**
 * Created by adamwilliams1 on 2/9/16.
 */
public class SessionManager {
    private static Session sshSession;
    private static String bindAddress;


    public void connect() {
        ConfigurationManager settings = new ConfigurationManager();
        settings.loadSettings();

        if (SessionManager.sshSession != null) {
            this.closeSession(true);
        }

        if (SessionManager.bindAddress != null) {
            SessionManager.bindAddress = null;
        }

        this.startSession();
    }

    public void disconnect() {
        this.closeSession(false);
    }

    private void closeSession(Boolean silent) {
        if (!silent) {
            Notifier.displayMessage("<strong>SSH Tunnel Closed</strong>");
        }
        System.out.println("Disconnected from SSH tunnels.");
        SessionManager.sshSession.disconnect();
    }

    private void startSession() {
        String lHost = this.getLoopbackAddress();
        Integer xdebugPort = PluginConfig.xdebugPort;
        String rHost = "localhost";
        Integer idePort = PluginConfig.idePort;
        String username = PluginConfig.username;

        UserInfo credentials = new Credentials();
        JSch SSH = new JSch();

        try {
            Session session = SSH.getSession(username, PluginConfig.remoteAddress, 22);
            session.setUserInfo(credentials);
            session.connect();
            session.setPortForwardingR(lHost, xdebugPort, rHost, xdebugPort);
            System.out.println("Local port " + xdebugPort.toString() + " mapped to remote host " +
                    PluginConfig.remoteAddress + ":" + xdebugPort.toString() + " on interface " + lHost + ".");
            session.setPortForwardingL(idePort, lHost, idePort);
            System.out.println("local port " + idePort.toString() + " mapped to remote host " +
                    PluginConfig.remoteAddress + ".");
            Notifier.displayMessage("<strong>SSH Tunnel Opened</strong><br />\nBound to remote loopback interface " + lHost + " on remote host " + PluginConfig.remoteAddress + ".");
            SessionManager.sshSession = session;

        } catch (JSchException e) {
            e.printStackTrace();
        }
    }

    private String getRandomSubnet() {
        if (SessionManager.bindAddress == null) {
            String subNet = PluginConfig.subnet +
                    this.generateRandomNumber(0).toString() + "." +
                    this.generateRandomNumber(0).toString() + "." +
                    this.generateRandomNumber(3).toString();
            System.out.println("Chosen IP: " + subNet);
            SessionManager.bindAddress = subNet;

        }
        return SessionManager.bindAddress;
    }

    private Integer generateRandomNumber(Integer min) {
        return ThreadLocalRandom.current().nextInt(min, 254 + 1);
    }

    private String getLoopbackAddress() {
        if (!PluginConfig.loopback.equals("")) {
            return PluginConfig.loopback;
        } else {
            return this.getRandomSubnet();
        }
    }

}
