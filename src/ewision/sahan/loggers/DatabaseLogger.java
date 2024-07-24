package ewision.sahan.loggers;

import ewision.sahan.utils.SQLDateFormatter;
import java.io.File;
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
            File db = new File("logs/db");
            db.mkdirs();

            FileHandler fileHandler = new FileHandler("logs//db//" + new SQLDateFormatter().getStringDate(new Date()) + "_" + System.currentTimeMillis() + "_db.log");
            //FileHandler fileHandler = new FileHandler("logs//" + new SQLDateFormatter().getStringDate(new Date()) + "_" + System.currentTimeMillis() + "_db.log");
            //FileHandler fileHandler = new FileHandler("logs//" + new SQLDateFormatter().getStringDate(new Date()) + "_db.log");
            //FileHandler fileHandler = new FileHandler("logs//" + System.currentTimeMillis() + "_db.log");
            fileHandler.setFormatter(new SimpleFormatter());
            logger.addHandler(fileHandler);
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }

    }

}
