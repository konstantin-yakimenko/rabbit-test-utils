package ru.jakimenko.tool.util;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TimeZone;
import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;


/**
 *
 * @author kyyakime
 */
@ManagedBean
@ApplicationScoped
public final class DateUtil {

    public static final String MESSAGE_BUNDLE_NAME = "resources";

    public static final String DB_BUNDLE_NAME = "datasource";

    private final static String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";

    public static final Double DEFAULT_COUNTER_VALUE = 0.0;
    public static final String DEFAULT_DECIMAL_PATTERN = "0.00";
    
    public static final Timestamp MAX_TIMESTAMP = Timestamp.valueOf(LocalDateTime.MAX);

    public static final SimpleDateFormat fromatterDateTime = new SimpleDateFormat("dd.MM.yyyy HH:mm:ssXXX");
    public static final DateTimeFormatter formatterZonedDate = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ssXXX");
    public static final DateTimeFormatter formatterLocalDate = DateTimeFormatter.ofPattern("dd.MM.yyyy");
    public static final DateTimeFormatter formatterZonedParse = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ssXXX");

    /**
     * Значение даты окончания по умолчанию (в миллисекундах)
     */
    public String getMESSAGE_BUNDLE_NAME() {
        return MESSAGE_BUNDLE_NAME;
    }

    public static String format(ZonedDateTime date) {
        if (date == null) {
            return "null";
        } else {
            return formatterZonedDate.format(date);
        }
    }

    public static String format(Date date) {
        if (date == null) {
            return "null";
        } else {
            ZonedDateTime zoned = ZonedDateTime.ofInstant(date.toInstant(), TimeZone.getDefault().toZoneId());
            return formatterZonedDate.format(zoned);
        }
    }

    public static String format(Date date, ZoneId zoneId) {
        if (date == null) {
            return "null";
        } else {
            ZonedDateTime zoned = ZonedDateTime.ofInstant(date.toInstant(), zoneId);
            return formatterZonedDate.format(zoned);
        }
    }

    public static String format(Date startTime, Date finishTime) {
        long total = (finishTime.getTime() - startTime.getTime())/1000;
        long hour = total/3600;
        long minute = (total - hour*3600)/60;
        long second = total - hour*3600 - minute*60;
        return String.format("%02d:%02d:%02d", hour, minute, second);        
    }
    
    public static XMLGregorianCalendar toXmlGregorian(Date date) throws DatatypeConfigurationException {
        GregorianCalendar gc = new GregorianCalendar(TimeZone.getDefault());
        gc.setTime(date);
        return DatatypeFactory.newInstance().newXMLGregorianCalendar(gc);
    }

    public static Timestamp parse(String value, ZoneId zoneId) throws ParseException {
        if (value == null || value.isEmpty()) {
            return null;
        }
        SimpleDateFormat formatter = new SimpleDateFormat(DATE_FORMAT);
        formatter.setTimeZone(TimeZone.getTimeZone(zoneId));
        Date date = formatter.parse(value);
        return new Timestamp(date.getTime());
    }

    public static Timestamp parse(String value) throws Exception {
        if (value == null || value.isEmpty()) {
            return null;
        }
        ZonedDateTime zoned = ZonedDateTime.parse(value, formatterZonedParse);
        return new Timestamp(zoned.toEpochSecond() * 1000);
    }
    

}
