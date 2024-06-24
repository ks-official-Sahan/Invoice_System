package ewision.sahan.service.impl;

import ewision.sahan.application.Application;
import ewision.sahan.application.main.Dashboard;
import ewision.sahan.product.CreateProduct;
import ewision.sahan.services.CreateService;
import ewision.sahan.product.ProductList;
import ewision.sahan.services.ServiceList;
import ewision.sahan.service.AppService;
import ewision.sahan.customer.CreateCustomer;
import ewision.sahan.customer.CustomerList;
import ewision.sahan.supplier.CreateSupplier;
import ewision.sahan.supplier.SupplierList;
import java.awt.BorderLayout;

import ewision.sahan.product.CreateProduct1;
import ewision.sahan.services.CreateService;
import ewision.sahan.product.ProductList;
import ewision.sahan.services.ServiceList;
import ewision.sahan.service.AppService;
import java.awt.BorderLayout;
import javax.swing.JPanel;

/**
 *
 * @author ksoff
 */
public class AppServiceIMPL extends AppServiceAbstract {

    /*
     * Main Panels Loading 
     */
    @Override
    public void showPanel(JPanel panel) {
        showMainPanel(panel);
    }

    @Override
    public void openDashboard() {
        Dashboard dashboard = new Dashboard();
        showMainPanel((JPanel) dashboard);
    }

    @Override
    public void openCreateProduct() {
        //CreateProduct createProduct = new CreateProduct();
        CreateProduct1 createProduct = new CreateProduct1();
        showMainPanel((JPanel) createProduct);
    }

    @Override
    public void openCreateService() {
        CreateService createService = new CreateService();
        showMainPanel((JPanel) createService);
    }

    @Override
    public void openProductList() {
        ProductList productList = new ProductList();
        showMainPanel((JPanel) productList);
    }

    @Override
    public void openServiceList() {
        ServiceList serviceList = new ServiceList();
        showMainPanel((JPanel) serviceList);
    }

    //////////// Chathura
    public void openCustomerList() {
        CustomerList customerList = new CustomerList();
        showMainPanel((JPanel) customerList);
    }

    public void openCreateCustomer() {
        CreateCustomer createCustomer = new CreateCustomer();
        showMainPanel((JPanel) createCustomer);
    }

    public void openCreateSupplier() {
        CreateSupplier createSupplier = new CreateSupplier();
        showMainPanel((JPanel) createSupplier);
    }

    public void openSupplierList() {
        SupplierList supplierList = new SupplierList();
        showMainPanel((JPanel) supplierList);
    }
}
