import com.intellij.notification.*;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.project.ProjectManager;


/**
 * Created by adamwilliams1 on 2/9/16.
 */
class Notifier {
    private static final NotificationGroup GROUP_DISPLAY_ID_INFO =
            new NotificationGroup("SSH Tunnel Notification Group", NotificationDisplayType.BALLOON, true);

    public static void displayMessage(final String message) {
        ApplicationManager.getApplication().invokeLater(new Runnable() {
            @Override
            public void run() {
                Notification notification = GROUP_DISPLAY_ID_INFO.createNotification(message, NotificationType.INFORMATION);
                Project[] projects = ProjectManager.getInstance().getOpenProjects();
                Notifications.Bus.notify(notification, projects[0]);

            }
        });
    }
}
