package ewision.sahan.report;

import ewision.sahan.loggers.CommonLogger;
import java.util.logging.Level;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperReport;

/**
 *
 * @author ksoff
 */
public class CompileReport {

    public JasperReport compileReport(String reportPath) {
        try {
            //String report = "C:\\Users\\cleanfuel\\Documents\\NetBeansProjects\\StringManipulation\\src\\stringmanipulation\\report1.jrxml";
            return JasperCompileManager.compileReport(reportPath);
            //JasperPrint jasp_print = JasperFillManager.fillReport(jasp_report, null, new JREmptyDataSource());
            //JasperViewer.viewReport(jasp_print);
        } catch (NullPointerException e) {
            CommonLogger.logger.log(Level.SEVERE, "NullpointerException in Jasper Report Compiling: " + e.getMessage(), e.getMessage());
        } catch (JRException e) {
            CommonLogger.logger.log(Level.SEVERE, "JRException in Jasper Report Compiling: " + e.getMessage(), e.getMessage());
        }
        return null;
    }
}
