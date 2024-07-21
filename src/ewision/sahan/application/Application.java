package ewision.sahan.application;

import com.formdev.flatlaf.FlatClientProperties;
import com.formdev.flatlaf.FlatDarkLaf;
import com.formdev.flatlaf.FlatLaf;
import com.formdev.flatlaf.fonts.roboto.FlatRobotoFont;
import com.formdev.flatlaf.themes.FlatMacDarkLaf;
import ewision.sahan.menu.Menu;
import ewision.sahan.model.ModelMenu;
import ewision.sahan.model.User;
import ewision.sahan.service.impl.AppServiceIMPL;
import ewision.sahan.utils.ImageScaler;
import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import javax.swing.JPanel;
import net.miginfocom.swing.MigLayout;
import org.jdesktop.animation.timing.Animator;
import org.jdesktop.animation.timing.TimingTarget;
import org.jdesktop.animation.timing.TimingTargetAdapter;

/**
 *
 * @author ksoff
 */
public class Application extends javax.swing.JFrame {

    public static AppServiceIMPL appService;

    private Menu menu = new Menu();
    private JPanel main = new JPanel();
    private MigLayout layout;
    private Animator animator;
    private boolean menuShow;

    private static User user;

    /**
     * Creates new form Main
     */
    public Application() {
        initComponents();
        appService = new AppServiceIMPL();
        appService.setApp(this);
        init();
    }

    public Application(User user) {
        setUser(user);
        initComponents();
        appService = new AppServiceIMPL();
        appService.setApp(this);
        init();
    }

    private void init() {
        getRootPane().putClientProperty(FlatClientProperties.FULL_WINDOW_CONTENT, true);

        setSize(1366, 768);
        setLocationRelativeTo(null);

        // setBackground(new Color(0, 0, 0, 0));
        layout = new MigLayout("fill", "0[]10[]0", "0[fill]0");
        body.setLayout(layout);

        getMain().setOpaque(false);
        getMain().setLayout(new BorderLayout());

        setupMenu();

        body.add(menu, "w 50!");
        body.add(getMain(), "w 100%");

        setupAnimation();
    }

    private void setupMenu() {
        menu.addEventLogout((ActionEvent e) -> {
            System.exit(0);
        });
        menu.addEventMenu((ActionEvent e) -> {
            if (!animator.isRunning()) {
                animator.start();
            }
        });
        menu.setEvent((int index) -> {
            switch (index) {
                case 0 ->
                    //appService.showPanel(new Dashboard());
                    appService.openDashboard();
                case 1 ->
                    appService.openProductList();
                case 2 ->
                    appService.openServiceList();
                case 3 ->
                    appService.openCustomerList();
                case 4 ->
                    appService.openSupplierList();
                case 5 ->
                    // Sale
                    //appService.openCreateSale();
                    appService.openSaleList();
                case 6 ->
                    // Purchase
                    //appService.openCreatePurchase();
                    appService.openPurchaseList();
                case 7 ->
                    // POS
                    appService.openUserList();
                case 8 ->
                    // User
                    appService.openPOS();
            }
        });

        ImageScaler scaler = new ImageScaler();
        menu.addMenu(new ModelMenu("Dashboard", scaler.getSvgIcon("dashboard.svg", 30)), true);
        menu.addMenu(new ModelMenu("Products", scaler.getSvgIcon("product.svg", 30)));
        menu.addMenu(new ModelMenu("Services", scaler.getSvgIcon("service1.svg", 30)));
        menu.addMenu(new ModelMenu("Customers", scaler.getSvgIcon("customer.svg", 30)));
        menu.addMenu(new ModelMenu("Suppliers", scaler.getSvgIcon("supplier.svg", 30)));
        menu.addMenu(new ModelMenu("Invoice", scaler.getSvgIcon("bill.svg", 30)));
        menu.addMenu(new ModelMenu("Purchase", scaler.getSvgIcon("service.svg", 30)));
        menu.addMenu(new ModelMenu("POS", scaler.getSvgIcon("dashboard1.svg", 30)));
        if (user.getRoleId() == 1) {
            menu.addMenu(new ModelMenu("Users", scaler.getSvgIcon("avatar.svg", 30)));
        }
        appService.openDashboard();
    }

    private void setupAnimation() {
        TimingTarget target = new TimingTargetAdapter() {
            @Override
            public void timingEvent(float fraction) {
                double width;
                if (menuShow) {
                    width = 50 + (150 * (1f - fraction));
                    menu.setAlpha(1f - fraction);
                } else {
                    width = 50 + (150 * fraction);
                    menu.setAlpha(fraction);
                }
                layout.setComponentConstraints(menu, "w " + width + "!");
                body.revalidate();
            }

            @Override
            public void end() {
                menuShow = !menuShow;
            }
        };

        animator = new Animator(400, target);
        animator.setResolution(0);
        animator.setAcceleration(0.5f);
        animator.setDeceleration(0.5f);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        body = new javax.swing.JPanel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        body.setBorder(javax.swing.BorderFactory.createEmptyBorder(6, 6, 6, 4));

        javax.swing.GroupLayout bodyLayout = new javax.swing.GroupLayout(body);
        body.setLayout(bodyLayout);
        bodyLayout.setHorizontalGroup(
            bodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 810, Short.MAX_VALUE)
        );
        bodyLayout.setVerticalGroup(
            bodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 529, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(body, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(body, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        FlatRobotoFont.install(); // Font Install
        FlatLaf.registerCustomDefaultsSource("ewision.sahan.theme");
        //FlatDarkLaf.setup();
        FlatMacDarkLaf.setup();
        //FlatMacLightLaf.setup();

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(() -> {
            new Application().setVisible(true);
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel body;
    // End of variables declaration//GEN-END:variables

    public JPanel getMain() {
        return main;
    }

    public static User getUser() {
        return user;
    }

    public void setUser(User aUser) {
        user = aUser;
    }
}
