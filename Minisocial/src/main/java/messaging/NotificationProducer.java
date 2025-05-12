package messaging;

import javax.annotation.Resource;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.inject.Inject;
import javax.jms.JMSContext;
import javax.jms.JMSProducer;
import javax.jms.ObjectMessage;
import javax.jms.Topic;

/**
 * NotificationProducer – Responsible for sending notification events to a JMS topic.
 * 
 * This class is a Singleton that is automatically initialized when the application starts. It uses JMS to send notification events
 * to a specified Topic. The class injects the JMSContext and the Topic resource.
 * 
 * Key Responsibilities:
 *   - Sends NotificationEvent messages to the "NotificationTopic" JMS topic.
 */
@Singleton
@Startup
public class NotificationProducer {

    /** JMS Context for message creation and sending */
    @Inject
    private JMSContext context;

    /** 📡 The JMS Topic where notifications will be sent */
    @Resource(lookup = "java:jboss/exported/jms/topic/NotificationTopic")
    private Topic notificationTopic;

    /**
     * Sends a NotificationEvent to the JMS Topic.
     * 
     * This method creates an ObjectMessage containing the NotificationEvent and sends it to the notificationTopic.
     * The notification contains details like the source and target users, event type, and message.
     * 
     * @param event The NotificationEvent to be sent.
     */
    public void sendNotification(NotificationEvent event) {
        try {
        	
            JMSProducer producer = context.createProducer();
            
          
            ObjectMessage message = context.createObjectMessage(event); 
            
         
            producer.send(notificationTopic, message);
            
            System.out.println("Notification sent: " + event.getMessage());
            System.out.printf("Sent message: %s\n", event); 
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
}
