package ewision.sahan.loggers;

import ewision.sahan.utils.SQLDateFormatter;
import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

/**
 *
 * @author ksoff
 */
public class CSVLogger {

    public static final Logger logger = Logger.getLogger(CSVLogger.class.getName());

    static {

        try {
            File csv = new File("logs/csv");
            csv.mkdirs();

            FileHandler fileHandler = new FileHandler("logs//csv//" + new SQLDateFormatter().getStringDate(new Date()) + "_" + System.currentTimeMillis() + "_csv.log");
            //FileHandler fileHandler = new FileHandler("logs//" + new SQLDateFormatter().getStringDate(new Date()) + "_" + System.currentTimeMillis() + "_csv.log");
            //FileHandler fileHandler = new FileHandler("logs//" + new SQLDateFormatter().getStringDate(new Date()) + "_csv.log");
            //FileHandler fileHandler = new FileHandler("logs//" + System.currentTimeMillis() + "_csv.log");
            fileHandler.setFormatter(new SimpleFormatter());
            logger.addHandler(fileHandler);
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }

    }

}
