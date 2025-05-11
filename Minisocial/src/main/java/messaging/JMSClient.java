package messaging;

import javax.annotation.Resource;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.inject.Inject;
import javax.jms.JMSContext;
import javax.jms.JMSProducer;
import javax.jms.Queue;
import javax.jms.TextMessage;


@Singleton
@Startup
public class JMSClient {

    @Inject
    private JMSContext context;

    @Resource(lookup = "java:/jms/topic/NotificationTopic")
    private Queue notificationQueue;

    public void sendMessage(String msg) {
    	try {
    		JMSProducer producer = context.createProducer();
    		TextMessage message = context.createTextMessage();
    		producer.send(notificationQueue, message);
    		System.out.printf("sent message", msg);
    	}
    	catch (Exception e) {
    		e.printStackTrace();
    	}
    }
}