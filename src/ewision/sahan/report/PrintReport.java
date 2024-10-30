package ewision.sahan.report;

import ewision.sahan.loggers.CommonLogger;
import java.util.HashMap;
import java.util.logging.Level;
import javax.swing.table.DefaultTableModel;
import net.sf.jasperreports.engine.JREmptyDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRRewindableDataSource;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperPrintManager;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRTableModelDataSource;
import net.sf.jasperreports.view.JasperViewer;

/**
 *
 * @author ksoff
 */
public class PrintReport {

    public void PrintCompiledReport(String reportJRXMLPath, HashMap parameters, JRRewindableDataSource dataSource) {
        try {
            JasperReport report = new CompileReport().compileReport(String.valueOf(getClass().getResource(reportJRXMLPath)));
            //JasperReport report = new CompileReport().compileReport(String.valueOf(getClass().getResource("/ewision/sahan/reports/report.jrxml")));

            JasperPrint jasperReport = JasperFillManager.fillReport(report, parameters, dataSource);

            //JasperPrint jasperReport = JasperFillManager.fillReport(report, parameters, connection); // DatabaseSource is Database Connection
            //JasperPrint jasperReport = JasperFillManager.fillReport(report, parameters, new JRTableModelDataSource(new DefaultTableModel()));
            //JasperPrint jasperReport = JasperFillManager.fillReport(report, parameters, new JREmptyDataSource());
            JasperViewer.viewReport(jasperReport);
        } catch (NullPointerException e) {
            CommonLogger.logger.log(Level.SEVERE, "NullpointerException in Jasper Report Compiling: " + e.getMessage(), e.getMessage());
        } catch (JRException e) {
            CommonLogger.logger.log(Level.SEVERE, "JRException in Jasper Report Compiling: " + e.getMessage(), e.getMessage());
        }
    }

    public void PrintReport(String reportPath, HashMap parameters, JRRewindableDataSource dataSource) {
        try {
            JasperPrint jasperReport = JasperFillManager.fillReport(getClass().getResourceAsStream(reportPath), parameters, new JREmptyDataSource());
            if (dataSource instanceof JRTableModelDataSource) {
                jasperReport = JasperFillManager.fillReport(getClass().getResourceAsStream(reportPath), parameters, (JRTableModelDataSource) dataSource);
            } else {
                jasperReport = JasperFillManager.fillReport(getClass().getResourceAsStream(reportPath), parameters, dataSource);
            }
            //JasperPrint jasperReport = JasperFillManager.fillReport(getClass().getResourceAsStream("/ewision/sahan/reports/report.jrxml"), parameters, dataSource);

            //JasperPrint jasperReport = JasperFillManager.fillReport(report, parameters, connection); // DatabaseSource is Database Connection
            //JasperPrint jasperReport = JasperFillManager.fillReport(report, parameters, new JRTableModelDataSource(new DefaultTableModel()));
            //JasperPrint jasperReport = JasperFillManager.fillReport(report, parameters, new JREmptyDataSource());
            //JasperViewer.viewReport(jasperReport, false);
            //JasperPrintManager.printReport(jasperReport, true);
            JasperPrintManager.printReport(jasperReport, false);
        } catch (NullPointerException e) {
            CommonLogger.logger.log(Level.SEVERE, "NullpointerException in Jasper Report Compiling: " + e.getMessage(), e.getMessage());
        } catch (JRException e) {
            CommonLogger.logger.log(Level.SEVERE, "JRException in Jasper Report Filling: " + e.getMessage(), e.getMessage());
        }
    }

    public void PrintViewReport(String reportPath, HashMap parameters, JRRewindableDataSource dataSource) {
        try {
            JasperPrint jasperReport = JasperFillManager.fillReport(getClass().getResourceAsStream(reportPath), parameters, new JREmptyDataSource());
            if (dataSource instanceof JRTableModelDataSource) {
                jasperReport = JasperFillManager.fillReport(getClass().getResourceAsStream(reportPath), parameters, (JRTableModelDataSource) dataSource);
            } else {
                jasperReport = JasperFillManager.fillReport(getClass().getResourceAsStream(reportPath), parameters, dataSource);
            }
            //JasperPrint jasperReport = JasperFillManager.fillReport(getClass().getResourceAsStream("/ewision/sahan/reports/report.jrxml"), parameters, dataSource);

            //JasperPrint jasperReport = JasperFillManager.fillReport(report, parameters, connection); // DatabaseSource is Database Connection
            //JasperPrint jasperReport = JasperFillManager.fillReport(report, parameters, new JRTableModelDataSource(new DefaultTableModel()));
            //JasperPrint jasperReport = JasperFillManager.fillReport(report, parameters, new JREmptyDataSource());
            JasperViewer.viewReport(jasperReport, false);
            JasperPrintManager.printReport(jasperReport, false);
        } catch (NullPointerException e) {
            CommonLogger.logger.log(Level.SEVERE, "NullpointerException in Jasper Report Compiling: " + e.getMessage(), e.getMessage());
        } catch (JRException e) {
            CommonLogger.logger.log(Level.SEVERE, "JRException in Jasper Report Filling: " + e.getMessage(), e.getMessage());
        }
    }
    public void ViewReport(String reportPath, HashMap parameters, JRRewindableDataSource dataSource) {
        try {
            JasperPrint jasperReport = JasperFillManager.fillReport(getClass().getResourceAsStream(reportPath), parameters, new JREmptyDataSource());
            if (dataSource instanceof JRTableModelDataSource) {
                jasperReport = JasperFillManager.fillReport(getClass().getResourceAsStream(reportPath), parameters, (JRTableModelDataSource) dataSource);
            } else {
                jasperReport = JasperFillManager.fillReport(getClass().getResourceAsStream(reportPath), parameters, dataSource);
            }
            //JasperPrint jasperReport = JasperFillManager.fillReport(getClass().getResourceAsStream("/ewision/sahan/reports/report.jrxml"), parameters, dataSource);

            //JasperPrint jasperReport = JasperFillManager.fillReport(report, parameters, connection); // DatabaseSource is Database Connection
            //JasperPrint jasperReport = JasperFillManager.fillReport(report, parameters, new JRTableModelDataSource(new DefaultTableModel()));
            //JasperPrint jasperReport = JasperFillManager.fillReport(report, parameters, new JREmptyDataSource());
            JasperViewer.viewReport(jasperReport, false);
            //JasperPrintManager.printReport(jasperReport, false);
        } catch (NullPointerException e) {
            CommonLogger.logger.log(Level.SEVERE, "NullpointerException in Jasper Report Compiling: " + e.getMessage(), e.getMessage());
        } catch (JRException e) {
            CommonLogger.logger.log(Level.SEVERE, "JRException in Jasper Report Filling: " + e.getMessage(), e.getMessage());
        }
    }

}
