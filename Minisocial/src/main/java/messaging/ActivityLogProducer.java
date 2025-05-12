package messaging;

import javax.annotation.Resource;
import javax.ejb.Singleton;
import javax.inject.Inject;
import javax.jms.JMSContext;
import javax.jms.JMSProducer;
import javax.jms.ObjectMessage;
import javax.jms.Queue;

@Singleton
public class ActivityLogProducer {

    @Inject
    private JMSContext context;

    @Resource(lookup = "java:jboss/exported/jms/queue/ActivityLogQueue")
    private Queue activityLogQueue;

    public void sendActivityLog(ActivityLogEvent logEvent) {
        try {
            ObjectMessage message = context.createObjectMessage(logEvent);
            JMSProducer producer = context.createProducer();
            producer.send(activityLogQueue, message);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
