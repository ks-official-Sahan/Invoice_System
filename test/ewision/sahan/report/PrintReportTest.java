package ewision.sahan.report;

import java.util.HashMap;
import net.sf.jasperreports.engine.JRRewindableDataSource;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author ksoff
 */
public class PrintReportTest {
    
    public PrintReportTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
    }
    
    @After
    public void tearDown() {
    }

    /**
     * Test of PrintCompiledReport method, of class PrintReport.
     */
    @Test
    public void testPrintCompiledReport() {
        System.out.println("PrintCompiledReport");
        String reportJRXMLPath = "";
        HashMap parameters = null;
        JRRewindableDataSource dataSource = null;
        PrintReport instance = new PrintReport();
        instance.PrintCompiledReport(reportJRXMLPath, parameters, dataSource);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of PrintReport method, of class PrintReport.
     */
    @Test
    public void testPrintReport() {
        System.out.println("PrintReport");
        String reportPath = "";
        HashMap parameters = null;
        JRRewindableDataSource dataSource = null;
        PrintReport instance = new PrintReport();
        instance.PrintReport(reportPath, parameters, dataSource);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }
    
}
