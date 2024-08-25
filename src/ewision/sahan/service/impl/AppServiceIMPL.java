package ewision.sahan.service.impl;

import ewision.sahan.application.main.Dashboard;
import ewision.sahan.customer.CreateCustomer;
import ewision.sahan.customer.CustomerList;
import ewision.sahan.expenses.CreateExpense;
import ewision.sahan.login.LoginForm;
import ewision.sahan.pos.POS;
import ewision.sahan.pos.POSUI;
import ewision.sahan.supplier.CreateSupplier;
import ewision.sahan.supplier.SupplierList;

import ewision.sahan.product.CreateProduct1;
import ewision.sahan.services.CreateService;
import ewision.sahan.product.ProductList;
import ewision.sahan.product.ViewProduct;
import ewision.sahan.purchase.CreatePurchase1;
import ewision.sahan.purchase.PurchaseList;
import ewision.sahan.purchase.UpdatePurchase;
import ewision.sahan.sale.CreateSale1;
import ewision.sahan.sale.SalesList;
import ewision.sahan.sale.UpdateSale;
import ewision.sahan.services.ServiceList;
import ewision.sahan.stock.UpdateStock;
import ewision.sahan.users.UserList;
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
    public void openCreateSale() {
        CreateSale1 createSale = new CreateSale1();
        showMainPanel((JPanel) createSale);
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

    @Override
    public void openCustomerList() {
        CustomerList customerList = new CustomerList();
        showMainPanel((JPanel) customerList);
    }

    @Override
    public void openCreateCustomer() {
        CreateCustomer createCustomer = new CreateCustomer();
        showMainPanel((JPanel) createCustomer);
    }

    @Override
    public void openCreateSupplier() {
        CreateSupplier createSupplier = new CreateSupplier();
        showMainPanel((JPanel) createSupplier);
    }

    @Override
    public void openSupplierList() {
        SupplierList supplierList = new SupplierList();
        showMainPanel((JPanel) supplierList);
    }

    @Override
    public void openSaleList() {
        SalesList saleList = new SalesList();
        showMainPanel((JPanel) saleList);
    }

    @Override
    public void openCreatePurchase() {
        CreatePurchase1 createPurchase = new CreatePurchase1();
        showMainPanel((JPanel) createPurchase);
    }

    @Override
    public void openPurchaseList() {
        PurchaseList purchaseList = new PurchaseList();
        showMainPanel((JPanel) purchaseList);
    }

    @Override
    public void openUserList() {
        UserList userList = new UserList();
        showMainPanel((JPanel) userList);
    }

    @Override
    public void openPOS() {
        //POS pos = new POS();
        POSUI pos = new POSUI();
        showMainPanel((JPanel) pos);
    }

    @Override
    public void openLogin() {
        LoginForm signIn = new LoginForm();
        showMainPanel((JPanel) signIn);
    }

    @Override
    public void openUpdateSale(String saledId, boolean isUpdate) {
        UpdateSale updateSale = new UpdateSale(saledId, isUpdate);
        showMainPanel((JPanel) updateSale);
    }

    @Override
    public void openUpdatePurchase(String purchaseId, boolean isUpdate) {
        UpdatePurchase updateSale = new UpdatePurchase(purchaseId, isUpdate);
        showMainPanel((JPanel) updateSale);
    }

    @Override
    public void openViewProduct(String id) {
        ViewProduct viewProduct = new ViewProduct(id);
        showMainPanel((JPanel) viewProduct);
    }

    @Override
    public void openUpdateStock() {
        UpdateStock updateStock = new UpdateStock();
        showMainPanel((JPanel) updateStock);        
    }

    @Override
    public void openExpenseList() {
    }

    @Override
    public void openCreateExpense() {
        CreateExpense panel = new CreateExpense();
        showMainPanel((JPanel) panel);        
    }

}
