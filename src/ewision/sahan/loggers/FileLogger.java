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
public class FileLogger {

    public static final Logger logger = Logger.getLogger(FileLogger.class.getName());

    static {

        try {
            File file = new File("logs/file");
            file.mkdirs();

            FileHandler fileHandler = new FileHandler("logs//file//" + new SQLDateFormatter().getStringDate(new Date()) + "_" + System.currentTimeMillis() + "_file.log");
            //FileHandler fileHandler = new FileHandler("logs//" + new SQLDateFormatter().getStringDate(new Date()) + "_" + System.currentTimeMillis() + "_file.log");
            //FileHandler fileHandler = new FileHandler("logs//" + new SQLDateFormatter().getStringDate(new Date()) + "_file.log");
            //FileHandler fileHandler = new FileHandler("logs//" + System.currentTimeMillis() + "_file.log");
            fileHandler.setFormatter(new SimpleFormatter());
            logger.addHandler(fileHandler);
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }

    }

}
