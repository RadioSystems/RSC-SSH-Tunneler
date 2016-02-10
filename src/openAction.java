import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;

/**
 * Created by adamwilliams1 on 2/8/16.
 */
public class openAction extends AnAction {

    @Override
    public void actionPerformed(AnActionEvent e) {
        if (ProcessManager.sessionManager == null) {
            ProcessManager.sessionManager = new SessionManager();
        }
        ProcessManager.sessionManager.connect();
    }
}
