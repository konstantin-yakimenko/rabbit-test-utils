package ru.jakimenko.tool.rabbit;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import ru.jakimenko.tool.handler.FixHandler;

/**
 *
 * @author kyyakime
 */
public class TaskMessageHandler {

    private static final Logger LOG = LogManager.getLogger();

    private final FixHandler fixHandler;
    private final RabbitTemplate waitTemplate;
    private final RabbitTemplate rejectTemplate;
    private final int maxTries;

    public TaskMessageHandler(FixHandler fixHandler, RabbitTemplate waitTemplate, RabbitTemplate rejectTemplate, int maxTries) {
        this.fixHandler = fixHandler;
        this.waitTemplate = waitTemplate;
        this.rejectTemplate = rejectTemplate;
        this.maxTries = maxTries;
    }

    public void handleMessage(final TaskMessage message) {
        LOG.debug(TaskMessage.marker, String.format("Handled message: %s", message));

        if (maxTries > 0 && message.getTries() >= maxTries) {
            rejectTemplate.convertAndSend(message);
            LOG.debug(TaskMessage.marker, String.format("Message %s send to reject queue.", message));
        } else if (fixHandler.syncTestDevice(message.getTestId(), message.getOperationDate(), message.getDescription())) {
            LOG.debug(TaskMessage.marker, String.format("Message %s processed successfully", message));
        } else {
            waitTemplate.convertAndSend(message);
            LOG.debug(TaskMessage.marker, String.format("Message %s send to wait queue.", message));
        }

    }
}
