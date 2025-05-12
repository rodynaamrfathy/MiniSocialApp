package messaging;

import javax.ejb.ActivationConfigProperty;
import javax.ejb.MessageDriven;
import javax.inject.Inject;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.ObjectMessage;

import service.NotificationService;


@MessageDriven(
    activationConfig = {
        @ActivationConfigProperty(propertyName = "destinationType", propertyValue = "javax.jms.Topic"),
        @ActivationConfigProperty(propertyName = "destinationLookup", propertyValue = "java:jboss/exported/jms/topic/NotificationTopic")
    }
)
public class NotificationTopicListener implements MessageListener {

    @Inject
    private NotificationService notificationService;

    public void onMessage(Message message) {
        try {
            if (message instanceof ObjectMessage) {
                NotificationEvent event = (NotificationEvent) ((ObjectMessage) message).getObject();
                notificationService.createNotification(event);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
