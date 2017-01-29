package ru.jakimenko.tool.rabbit;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageBuilder;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.support.converter.MessageConversionException;
import org.springframework.amqp.support.converter.MessageConverter;

/**
 *
 * @author kyyakime
 */
public class TaskMessageConverter implements MessageConverter {

    private final static String HEADER_TEST_DEVICE = "TestId";
    private final static String HEADER_OPERATION_DATE = "OperationDate";
    private final static String HEADER_TRIES = "Tries";

    private final static String ERROR_FIELD_NOT_RECEIVED = "The field '%s' was not received";
    private final static String ERROR_INCORRECT_TYPE = "The field '%s' has incorrect data type";

    private final static String FORMAT_DATETIME = "yyyy-MM-dd'T'HH:mm:ssX";

    private final static String DEFAULT_DESCRIPTION = "Rabbit mytest";

    private final static Logger LOG = LogManager.getLogger();

    @Override
    public Message toMessage(Object o, MessageProperties mp) throws MessageConversionException {
        TaskMessage msg = (TaskMessage) o;
        byte[] body = null;
        if (msg.getDescription() != null) {
            body = msg.getDescription().getBytes();
        }
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(FORMAT_DATETIME);
        Map<String, Object> headers = mp.getHeaders();
        headers.put(HEADER_TEST_DEVICE, msg.getTestId());
        headers.put(HEADER_OPERATION_DATE, formatter.format(msg.getOperationDate()));
        headers.put(HEADER_TRIES, msg.getTries());
        return MessageBuilder.withBody(body).andProperties(mp).build();
    }

    @Override
    public Object fromMessage(Message msg) throws MessageConversionException {
        Map<String, Object> headers = msg.getMessageProperties().getHeaders();

        Object testDeviceHeader = headers.get(HEADER_TEST_DEVICE);
        if (testDeviceHeader == null) {
            throw new MessageConversionException(String.format(ERROR_FIELD_NOT_RECEIVED, HEADER_TEST_DEVICE));
        }

        Long testId;
        if (testDeviceHeader instanceof Number) {
            testId = ((Number) testDeviceHeader).longValue();
        } else {
            throw new MessageConversionException(String.format(ERROR_INCORRECT_TYPE, HEADER_TEST_DEVICE));
        }

        Object operationDateHeader = headers.get(HEADER_OPERATION_DATE);
        if (operationDateHeader == null) {
            throw new MessageConversionException(String.format(ERROR_FIELD_NOT_RECEIVED, HEADER_OPERATION_DATE));
        }

        ZonedDateTime operationDate;
        if (operationDateHeader instanceof String) {
            try {
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern(FORMAT_DATETIME);
                operationDate = ZonedDateTime.parse((String) operationDateHeader, formatter);
            } catch (Exception ex) {
                throw new MessageConversionException(String.format(ERROR_INCORRECT_TYPE, HEADER_OPERATION_DATE));
            }
        } else {
            throw new MessageConversionException(String.format(ERROR_INCORRECT_TYPE, HEADER_OPERATION_DATE));
        }

        int tries = 0;
        Object triesHead = headers.get(HEADER_TRIES);
        if (triesHead != null && triesHead instanceof Number) {
            tries = ((Number) triesHead).intValue();
        }
        tries++;

        String description = DEFAULT_DESCRIPTION;
        byte[] body = msg.getBody();
        if (body != null && body.length > 0) {
            description = new String(body);
        }

        LOG.info(String.format("Get Message(TestDeviceId:%s, OperationDate:%s)",
                testId, operationDate)
        );

        return new TaskMessage(testId, operationDate, description, tries);
    }
}
