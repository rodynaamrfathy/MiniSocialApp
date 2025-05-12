package messaging;

import models.ActivityLog;
import service.ActivityLogService;

import javax.ejb.ActivationConfigProperty;
import javax.ejb.MessageDriven;
import javax.inject.Inject;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.ObjectMessage;

@MessageDriven(
    activationConfig = {
        @ActivationConfigProperty(propertyName = "destinationType", propertyValue = "javax.jms.Queue"),
        @ActivationConfigProperty(propertyName = "destinationLookup", propertyValue = "java:jboss/exported/jms/queue/ActivityLogQueue")
    }
)
public class ActivityLogListener implements MessageListener {

    @Inject
    private ActivityLogService activityLogService;

    @Override
    public void onMessage(Message message) {
        try {
            if (message instanceof ObjectMessage) {
                ActivityLogEvent event = (ActivityLogEvent) ((ObjectMessage) message).getObject();
                activityLogService.logActivity(event);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
