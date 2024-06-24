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

    public void openProductList();

    public void openServiceList();
}
