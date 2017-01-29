package ru.jakimenko.tool.task;

import java.time.ZonedDateTime;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import ru.jakimenko.tool.rabbit.TaskMessage;

/**
 *
 * @author kyyakime
 */
public class RabbitGenTask extends Task {

    private final static Logger LOG = LogManager.getLogger();

    private volatile AtomicLong testId = new AtomicLong(0L);
    
    private final RabbitTemplate taskTemplate;
    private final RabbitTemplate waitTemplate;
    private final String waitQueueName;
    private final int generateCountMes;

    private volatile boolean running = false;

    public RabbitGenTask(RabbitTemplate taskTemplate, RabbitTemplate waitTemplate, String waitQueueName,
            String name, String cron, boolean autoStart, int generateCountMes) {
        super(name, cron, autoStart);
        this.taskTemplate = taskTemplate;
        this.waitTemplate = waitTemplate;
        this.waitQueueName = waitQueueName;
        this.generateCountMes = generateCountMes;
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
            Random random = new Random(500);
            running = true;
            int count = getMessageCount();
            for (int i=0; running && i<count; i++) {

                TaskMessage message = new TaskMessage(testId.incrementAndGet(), ZonedDateTime.now(), "Some test message", 0);
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
        return generateCountMes;
    }

}
