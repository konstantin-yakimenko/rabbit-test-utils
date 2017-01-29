package ru.jakimenko.tool.handler;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.ZonedDateTime;
import java.util.Random;

/**
 *
 * @author kyyakime
 */
@Component
public class FixHandler {

    private static final Logger LOG = LogManager.getLogger();


    private final int millsOnHandle;
    private final int verBadHandle;

    @Autowired
    public FixHandler(
            @Value("${mills.on.handle.message.random}") int millsOnHandle,
            @Value("${variant.message.in.wait}") int verBadHandle) {
        this.verBadHandle = verBadHandle;
        this.millsOnHandle = millsOnHandle;
    }

    public boolean syncTestDevice(final Long testDeviceId, final ZonedDateTime operationDate, final String description) {
        Random r = new Random();
        try {
            Thread.sleep(r.nextInt(millsOnHandle));
        } catch (Exception e) {
        }
        return r.nextInt(verBadHandle) > 1;
    }
}
