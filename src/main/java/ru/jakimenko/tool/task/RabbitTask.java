package ru.jakimenko.tool.task;

import com.rabbitmq.client.AMQP.Queue.DeclareOk;
import com.rabbitmq.client.Channel;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

/**
 *
 * @author kyyakime
 */
public class RabbitTask extends Task {

    private final static Logger LOG = LogManager.getLogger();

    private final RabbitTemplate taskTemplate;
    private final RabbitTemplate waitTemplate;
    private final RabbitTemplate rejectTemplate;
    private final String waitQueueName;

    private volatile boolean running = false;

    public RabbitTask(RabbitTemplate taskTemplate, RabbitTemplate waitTemplate, String waitQueueName,
            String name, String cron, boolean autoStart, RabbitTemplate rejectTemplate) {
        super(name, cron, autoStart);
        this.taskTemplate = taskTemplate;
        this.waitTemplate = waitTemplate;
        this.waitQueueName = waitQueueName;
        this.rejectTemplate = rejectTemplate;
    }

    @Override
    public void run() {
        if (isActive() && !running) {
            start();
        }
    }

    public void process() {
        if (running) {
            stop();
        } else {
            start();
        }
    }

    
    public boolean isRunning() {
        return running;
    }

    private void start() {
        
        try {
            running = true;
            int count = getMessageCount();
            for (int i=0; running && i<count; i++) {
                Object message = waitTemplate.receiveAndConvert(waitQueueName);
                if (message==null) {
                    break;
                }
                taskTemplate.convertAndSend(message);
            }
            
        } catch (Exception ex) {
            LOG.error("Error in rabbit task", ex);
        } finally {
            running = false;
        }
        
    }

    private void stop() {
        running = false;
    }

    private int getMessageCount() {
        DeclareOk declareOk = waitTemplate.execute((Channel channel) -> channel.queueDeclarePassive(waitQueueName));
        return declareOk.getMessageCount();
    }
    
    private int getMessageCountReject() {
        DeclareOk declareOk = rejectTemplate.execute((Channel channel) -> channel.queueDeclarePassive("queue.mytest.wait.reject"));
        return declareOk.getMessageCount();
    }
    
    private void clearReject() throws Exception {
            int count = getMessageCountReject();
            for (int i=0; running && i<count; i++) {
                Object message = rejectTemplate.receiveAndConvert("queue.mytest.wait.reject");
            }
    }
    

}
