package messaging;

import javax.ejb.ActivationConfigProperty;
import javax.ejb.MessageDriven;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

@MessageDriven(
	    name = "NotificationListener",
	    activationConfig = {
	        @ActivationConfigProperty(propertyName = "destinationType", propertyValue = "javax.jms.Topic"),
	        @ActivationConfigProperty(propertyName = "destinationLookup", propertyValue = "java:/jms/topic/NotificationTopic"),
	        @ActivationConfigProperty(propertyName = "subscriptionDurability", propertyValue = "Durable"),
	        @ActivationConfigProperty(propertyName = "clientId", propertyValue = "NotificationClient"),
	        @ActivationConfigProperty(propertyName = "subscriptionName", propertyValue = "NotificationSubscription")
	    }
	)
public class NotificationListener implements MessageListener{

	@Override
	public void onMessage(Message message) {
        try {
            if (message instanceof TextMessage textMessage) {
                String body = textMessage.getText();
                System.out.println("Notification received: " + body);
                // TODO: deserialize JSON and process event (e.g., store, push to frontend, etc.)
            } else {
                System.out.println("Received non-text message: " + message);
            }
        } catch (Exception e) {
            e.printStackTrace(); // You should ideally log this
        }
    }

}
