package ru.jakimenko.tool.rabbit;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.amqp.rabbit.listener.FatalExceptionStrategy;

/**
 *
 * @author kyyakime
 */
public class TaskExceptionStrategy implements FatalExceptionStrategy {

    private static final Logger LOG = LogManager.getLogger();
    
    @Override
    public boolean isFatal(Throwable thrwbl) {
        LOG.error(TaskMessage.marker, "Scanner exception", thrwbl);
        return true;
    }    
}
