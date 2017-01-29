package ru.jakimenko.tool.rabbit;

import java.time.ZonedDateTime;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.MarkerManager;

/**
 *
 * @author kyyakime
 */
public class TaskMessage {

    public static final Marker marker = MarkerManager.getMarker("RABBIT");
    
    private final Long testId;
    private final ZonedDateTime operationDate;
    private final String description;
    private final int tries; 

    public TaskMessage(Long testDeviceId, ZonedDateTime operationDate, String description, int tries) {
        this.testId = testDeviceId;
        this.operationDate = operationDate;
        this.description = description;
        this.tries = tries;
    }

    public Long getTestId() {
        return testId;
    }

    public ZonedDateTime getOperationDate() {
        return operationDate;
    }

    public String getDescription() {
        return description;
    }

    public int getTries() {
        return tries;
    }    

    @Override
    public String toString() {
        return "{" 
                + "testId=" + testId
                + ", operationDate=" + operationDate 
                + ", tries=" + tries
                + (description==null ? "" : ", description="+description)                
                + '}';
    }    
    
}
