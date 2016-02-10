import com.intellij.ide.util.PropertiesComponent;
import com.intellij.openapi.options.Configurable;
import com.intellij.openapi.options.ConfigurationException;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

/**
 * Created by adamwilliams1 on 2/9/16.
 */


class ConfigurationManager implements Configurable {

    private JTextField remoteHost;
    private JTextField idePort;
    private JTextField xdebugPort;
    private JTextField username;
    private JPasswordField password;
    private JTextField loopbackAddress;

    private JPanel rootPanel;

    public ConfigurationManager() {
        this.loadSettings();
    }


    @Nls
    @Override
    public String getDisplayName() {
        return "RSC SSH Tunnel Manager";
    }

    @Nullable
    @Override
    public String getHelpTopic() {
        return null;
    }

    @Nullable
    @Override
    public JComponent createComponent() {

        return rootPanel;

    }

    @Override
    public boolean isModified() {
        return true;
    }

    @Override
    public void apply() throws ConfigurationException {
        PluginConfig.remoteAddress = remoteHost.getText();
        PluginConfig.xdebugPort = Integer.parseInt(xdebugPort.getText());
        PluginConfig.idePort = Integer.parseInt(idePort.getText());
        PluginConfig.username = username.getText();
        PluginConfig.password = new String(password.getPassword());
        PluginConfig.loopback = loopbackAddress.getText();

        this.persistSettings();

    }

    @Override
    public void reset() {
        if (PluginConfig.remoteAddress != null) {
            remoteHost.setText(PluginConfig.remoteAddress);
        }
        if (PluginConfig.xdebugPort != null) {
            xdebugPort.setText(PluginConfig.xdebugPort.toString());
        }
        if (PluginConfig.idePort != null) {
            idePort.setText(PluginConfig.idePort.toString());
        }
        if (PluginConfig.username != null) {
            username.setText(PluginConfig.username);
        }
        if (PluginConfig.password != null) {
            password.setText(PluginConfig.password);
        }
        if (PluginConfig.loopback != null) {
            loopbackAddress.setText(PluginConfig.loopback);
        }

    }

    @Override
    public void disposeUIResources() {

    }

    private void persistSettings() {
        PropertiesComponent keyStore = PropertiesComponent.getInstance();

        keyStore.setValue("rscssh_remoteHost", PluginConfig.remoteAddress);
        keyStore.setValue("rscssh_xdebugPort", PluginConfig.xdebugPort.toString());
        keyStore.setValue("rscssh_idePort", PluginConfig.idePort.toString());
        keyStore.setValue("rscssh_username", PluginConfig.username);
        keyStore.setValue("rscssh_password", PluginConfig.password);
        keyStore.setValue("rscssh_loopback", PluginConfig.loopback);

    }

    public void loadSettings() {
        PropertiesComponent keyStore = PropertiesComponent.getInstance();

        PluginConfig.remoteAddress = keyStore.getValue("rscssh_remoteHost");
        PluginConfig.username = keyStore.getValue("rscssh_username");
        PluginConfig.password = keyStore.getValue("rscssh_password");
        PluginConfig.loopback = keyStore.getValue("rscssh_loopback");

        String raw_xdebugPort = keyStore.getValue("rscssh_xdebugPort");
        String raw_idePort = keyStore.getValue("rscssh_idePort");

        if (raw_idePort != null) {
            PluginConfig.idePort = Integer.parseInt(raw_idePort);
        }
        if (raw_xdebugPort != null) {
            PluginConfig.xdebugPort = Integer.parseInt(raw_xdebugPort);
        }

    }
}
