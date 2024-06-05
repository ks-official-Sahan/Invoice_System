package ewision.sahan.service.impl;

import ewision.sahan.application.Application;
import ewision.sahan.application.main.Dashboard;
import ewision.sahan.product.CreateProduct;
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
public class AppServiceIMPL implements AppService {

    private Application app;
    private JPanel main;

    @Override
    public void setApp(Application app) {
        this.app = app;
        this.main = app.getMain();
    }

    /*
     * Main Panels Loading 
     */
    private JPanel currentMainPanel;

    //@Override
    //public void showMainPanel(JPanel mainPanel) {
    private void showMainPanel(JPanel mainPanel) {
        if (this.currentMainPanel != null) {
            this.main.remove(currentMainPanel);
            this.currentMainPanel = null;
        }
        this.currentMainPanel = mainPanel;
        this.main.add(this.currentMainPanel, BorderLayout.CENTER);
        main.repaint();
        main.revalidate();
    }

    //public void showForm(JPanel form) {
    //    main.removeAll();
    //    main.add(form);
    //    main.repaint();
    //    main.revalidate();
    //}
    
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
        CreateProduct createProduct = new CreateProduct();
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

}
