package ewision.sahan.service;

import ewision.sahan.application.Application;
import javax.swing.JPanel;

/**
 *
 * @author ksoff
 */
public interface AppService {

    public void setApp(Application app);

    public void showPanel(JPanel panel);

    public void openDashboard();

    public void openCreateProduct();

    public void openCreateService();

    public void openCreateSale();

    public void openCreatePurchase();

    public void openProductList();

    public void openSaleList();

    public void openServiceList();

    public void openPurchaseList();

    public void openCustomerList();

    public void openCreateCustomer();

    public void openCreateSupplier();

    public void openSupplierList();
}
