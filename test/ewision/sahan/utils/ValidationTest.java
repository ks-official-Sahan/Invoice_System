package ewision.sahan.utils;

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
public class ValidationTest {
    
    public ValidationTest() {
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
     * Test of mobile method, of class Validation.
     */
    @Test
    public void testMobile() {
        System.out.println("mobile");
        String mobile = "";
        boolean expResult = false;
        boolean result = Validation.mobile(mobile);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of email method, of class Validation.
     */
    @Test
    public void testEmail() {
        System.out.println("email");
        String email = "";
        boolean expResult = false;
        boolean result = Validation.email(email);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of password method, of class Validation.
     */
    @Test
    public void testPassword() {
        System.out.println("password");
        String password = "";
        boolean expResult = false;
        boolean result = Validation.password(password);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }
    
}
