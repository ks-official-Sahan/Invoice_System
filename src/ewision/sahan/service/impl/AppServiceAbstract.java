package ewision.sahan.service.impl;

import ewision.sahan.application.Application;
import ewision.sahan.service.AppService;
import java.awt.BorderLayout;
import javax.swing.JPanel;

/**
 *
 * @author ksoff
 */
public abstract class AppServiceAbstract implements AppService {

    //private Application app;
    private JPanel main;

    @Override
    public void setApp(Application app) {
        //this.app = app;
        this.main = app.getMain();
    }

    private JPanel currentMainPanel;

    protected void showMainPanel(JPanel mainPanel) {
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
}
