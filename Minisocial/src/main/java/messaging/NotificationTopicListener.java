package messaging;

import javax.ejb.ActivationConfigProperty;
import javax.ejb.MessageDriven;
import javax.inject.Inject;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.ObjectMessage;

import service.NotificationService;

/**
 * NotificationTopicListener – Listens for incoming notification messages from a JMS Topic.
 * 
 * This class is a Message-Driven Bean (MDB) that listens for messages sent to the "NotificationTopic" JMS topic.
 * It processes incoming `NotificationEvent` messages and delegates them to the `NotificationService` to create notifications.
 * 
 * Key Responsibilities:
 *   - Listens for messages on the JMS Topic and processes them.
 *   - Uses the `NotificationService` to handle incoming notification events.
 */
@MessageDriven(
    activationConfig = {
        // Defines the destination type as Topic and specifies the topic to listen to
        @ActivationConfigProperty(propertyName = "destinationType", propertyValue = "javax.jms.Topic"),
        @ActivationConfigProperty(propertyName = "destinationLookup", propertyValue = "java:jboss/exported/jms/topic/NotificationTopic")
    }
)
public class NotificationTopicListener implements MessageListener {

    /** Service used to handle notification events */
    @Inject
    private NotificationService notificationService;

    /**
     * Processes incoming messages from the JMS topic.
     * 
     * This method is triggered when a new message arrives on the "NotificationTopic". If the message is an
     * instance of `ObjectMessage`, it extracts the `NotificationEvent` and passes it to the `NotificationService`
     * to create a notification.
     * 
     * @param message The JMS message that contains the notification event.
     */
    public void onMessage(Message message) {
        try {
            // ✅ Check if the message is of type ObjectMessage
            if (message instanceof ObjectMessage) {
                // Extract the NotificationEvent from the message
                NotificationEvent event = (NotificationEvent) ((ObjectMessage) message).getObject();
                
                // Pass the event to NotificationService to create the notification
                notificationService.createNotification(event);
            }
        } catch (Exception e) {
            // Handle any errors that occur during message processing
            e.printStackTrace();
        }
    }
}
