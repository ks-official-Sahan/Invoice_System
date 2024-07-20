package ewision.sahan.utils;

import ewision.sahan.loggers.CommonLogger;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author ksoff
 */
public final class SQLDateFormatter {

    SimpleDateFormat format;

    public SQLDateFormatter() {
        format = new SimpleDateFormat();
        setPattern("yyyy-MM-dd");
    }

    public SQLDateFormatter(String pattern) {
        format = new SimpleDateFormat(pattern);
    }

    public void setPattern(String pattern) {
        try {
            format.applyPattern(pattern);
        } catch (IllegalArgumentException | NullPointerException e) {
            CommonLogger.logger.log(Level.SEVERE, "Exception in " + getClass().getName() + " set Stock Cost: " + e.getClass().getName() + e.getMessage(), e.getMessage());
        }
    }

    public String getStringDate(Date date) {
        return format.format(date);
    }

    public String getStringDate(Date date, String pattern) {
        return new SimpleDateFormat(pattern).format(date);
    }

    public String getStringDateTime(Date date) {
        return new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(date);
    }

    public Date getDate(String date) {
        try {
            return format.parse(date);
        } catch (ParseException ex) {
            CommonLogger.logger.log(Level.SEVERE, "ParseException in " + getClass().getName() + " getDate: " + ex.getMessage(), ex.getMessage());
        }
        return new Date();
    }

}
