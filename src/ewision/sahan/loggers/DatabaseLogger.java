package ewision.sahan.loggers;

import ewision.sahan.utils.SQLDateFormatter;
import java.io.IOException;
import java.util.Date;
import java.util.logging.FileHandler;
import java.util.logging.Handler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

/**
 *
 * @author ksoff
 */
public class DatabaseLogger {

    public static final Logger logger = Logger.getLogger(DatabaseLogger.class.getName());

    static {

        try {
            FileHandler fileHandler = new FileHandler("logs//" + new SQLDateFormatter().getStringDate(new Date()) + "_db.log");
            fileHandler.setFormatter(new SimpleFormatter());
            logger.addHandler(fileHandler);
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }

    }

}
