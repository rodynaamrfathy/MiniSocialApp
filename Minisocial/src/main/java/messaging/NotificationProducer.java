package messaging;

import javax.annotation.Resource;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.inject.Inject;
import javax.jms.JMSContext;
import javax.jms.JMSProducer;
import javax.jms.ObjectMessage;
import javax.jms.Topic;


@Singleton
@Startup
public class NotificationProducer {

	// using default context
    @Inject
    private JMSContext context;

    @Resource(lookup = "java:jboss/exported/jms/topic/NotificationTopic")
    private Topic notificationTopic;

    public void sendNotification(NotificationEvent event) {
    	try {
    		JMSProducer producer = context.createProducer();
    		ObjectMessage message = context.createObjectMessage(event); //  use your event
    		producer.send(notificationTopic, message);
    		System.out.println(" Notification sent: " + event.getMessage());
    		System.out.printf("sent message", event);
    	}
    	catch (Exception e) {
    		e.printStackTrace();
    	}
    }
}