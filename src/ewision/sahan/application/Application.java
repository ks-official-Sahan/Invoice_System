package ewision.sahan.application;

import com.formdev.flatlaf.FlatClientProperties;
import com.formdev.flatlaf.FlatLaf;
import com.formdev.flatlaf.extras.FlatAnimatedLafChange;
import com.formdev.flatlaf.fonts.roboto.FlatRobotoFont;
import com.formdev.flatlaf.themes.FlatMacDarkLaf;
import ewision.sahan.application.main.LoginForm1;
import ewision.sahan.application.main.MainForm;
import ewision.sahan.login.LoginForm;
import ewision.sahan.model.Constants;
import ewision.sahan.model.Shop;
import ewision.sahan.model.User;
import ewision.sahan.service.impl.AppServiceIMPL;
import java.awt.Dimension;
import java.awt.Font;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import raven.toast.Notifications;

/**
 *
 * @author ks.official.sahan
 */
public class Application extends javax.swing.JFrame {

    private static Application app;
    private final MainForm mainForm;

    public MainForm getMainForm() {
        return mainForm;
    }
    private final LoginForm1 loginForm;
    //private final LoginForm loginForm;
    //private final SignIn loginForm;

    public final static AppServiceIMPL appService = new AppServiceIMPL();
    private static User user = new User();
    private static Shop shop;

    public static User getUser() {
        return user;
    }

    public static void setUser(User aUser) {
        user = aUser;
    }

    public static Shop getShop() {
        return shop;
    }

    public static void setShop(Shop aShop) {
        shop = aShop;
    }

    public Application() {
        initComponents();
        mainForm = new MainForm();
        loginForm = new LoginForm1();
        //loginForm = new LoginForm();
        //loginForm = new SignIn();
        init();
    }

    private void init() {
        ImageIcon imageIcon = new ImageIcon(getClass().getResource(Constants.GRADIENT_ICON));
        this.setIconImage(imageIcon.getImage()); //Windows
        //Taskbar taskbar = Taskbar.getTaskbar();
        //taskbar.setIconImage(imageIcon.getImage()); //MacOS New
        // //Application.getApplication().setDockIconImage(imageIcon.getImage()); //MacOS Old

        setSize(new Dimension(1366, 768));
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setLocationRelativeTo(null); // generate center
        setContentPane(loginForm); // set current panel
        getRootPane().putClientProperty(FlatClientProperties.FULL_WINDOW_CONTENT, true); // Decorated without a header space
        Notifications.getInstance().setJFrame(this);
    }

    /* Routes to showForm() method in mainForm */
    //public static void showForm(Component component) {
    public static void showForm(JPanel panel) {
        panel.applyComponentOrientation(app.getComponentOrientation());
        app.mainForm.showForm(panel);
    }

    /* Show Main Form */
    public static void login() {
        FlatAnimatedLafChange.showSnapshot();
        app.setContentPane(app.mainForm);
        app.mainForm.applyComponentOrientation(app.getComponentOrientation());
        setSelectedMenu(0, 0);
        app.mainForm.menu.setHeaderName(user.getUsername());
        app.mainForm.menu.getHeader().setText(user.getUsername());
        app.mainForm.hideMenu();
        SwingUtilities.updateComponentTreeUI(app.mainForm);
        FlatAnimatedLafChange.hideSnapshotWithAnimation();
    }

    /* Show Login Form */
    public static void logout() {
        FlatAnimatedLafChange.showSnapshot();
        app.setContentPane(app.loginForm);
        app.loginForm.applyComponentOrientation(app.getComponentOrientation());
        SwingUtilities.updateComponentTreeUI(app.loginForm);
        FlatAnimatedLafChange.hideSnapshotWithAnimation();
    }

    /* Routes to setSelectedMenu() method in mainForm */
    public static void setSelectedMenu(int index, int subIndex) {
        app.mainForm.setSelectedMenu(index, subIndex);
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 719, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 521, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    public static void main(String args[]) {
        FlatRobotoFont.install(); // Font Install
        FlatLaf.registerCustomDefaultsSource("ewision.sahan.theme"); // Theme Property setup
        UIManager.put("defaultFont", new Font(FlatRobotoFont.FAMILY, Font.PLAIN, 13)); // Default font setup
        FlatMacDarkLaf.setup(); // Theme Setup
        java.awt.EventQueue.invokeLater(() -> {
            app = new Application();
            //  app.applyComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
            app.setVisible(true);
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
}
