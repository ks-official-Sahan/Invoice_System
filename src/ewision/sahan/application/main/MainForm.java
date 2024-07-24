package ewision.sahan.application.main;

import com.formdev.flatlaf.FlatClientProperties;
import com.formdev.flatlaf.extras.FlatSVGIcon;
import com.formdev.flatlaf.util.UIScale;
import ewision.sahan.application.Application;
import ewision.sahan.application.sub.CreateProduct;
import ewision.sahan.application.sub.Form0;
import ewision.sahan.application.sub.FormDashboard;
import ewision.sahan.application.sub.FormInbox;
import ewision.sahan.application.sub.FormRead;
import ewision.sahan.menu.Menu;
import ewision.sahan.menu.MenuAction;
import ewision.sahan.model.ModelMenu;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.ComponentOrientation;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.LayoutManager;
import java.awt.event.ActionEvent;
import javax.swing.JButton;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;

/**
 *
 * @author ks.official.sahan
 */
public class MainForm extends JLayeredPane {

    private Menu menu;
    private JPanel panelBody;
    private HeaderBar headerBar;
    private JButton menuButton;

    public MainForm() {
        init();
    }

    private void init() {
        setBorder(new EmptyBorder(5, 5, 5, 5)); // Setting some padding
        //setBorder(new EmptyBorder(10, 10, 10, 10));
        setLayout(new MainFormLayout()); // Custom Layout applied

        headerBar = new HeaderBar();
//        headerBar.add(new JButton("Hello"));

        menu = new Menu();
        panelBody = new JPanel(new BorderLayout()); // Border Layout to load Panels
        panelBody.setBorder(new EmptyBorder(2, 5, 0, 5));

        initMenuArrowIcon();
        menuButton.putClientProperty(FlatClientProperties.STYLE, ""
                + "background:$Menu.button.background;"
                + "arc:999;" // Roundness
                + "focusWidth:0;"
                + "borderWidth:0");
        menuButton.addActionListener((ActionEvent e) -> {
            setMenuFull(!menu.isMenuFull());
        });

        initMenuEvent();
        setLayer(menuButton, JLayeredPane.POPUP_LAYER);
        add(headerBar);
        add(menuButton);
        add(menu);
        add(panelBody);
    }

    @Override
    public void applyComponentOrientation(ComponentOrientation o) {
        super.applyComponentOrientation(o);
        initMenuArrowIcon();
    }

    private void initMenuArrowIcon() {
        if (menuButton == null) {
            menuButton = new JButton();
        }
        String icon = (getComponentOrientation().isLeftToRight()) ? "menu_left.svg" : "menu_right.svg";
        menuButton.setIcon(new FlatSVGIcon("ewision/sahan/icon/svg/" + icon, 0.8f));
    }

    /* Check index and open relavant panels */
    private void initMenuEvent() {
        menu.addMenuEvent((int index, int subIndex, MenuAction action) -> {
            // Application.mainForm.showForm(new DefaultForm("Form : " + index + " " + subIndex));
            if (index == 0) {
                //Application.showForm(new Form0());
                //Application.showForm(new CreateProduct());
                Application.appService.openDashboard();
            } else if (index == 1) {
                Application.appService.openProductList();
//                if (subIndex == 1) {
//                    Application.showForm(new FormInbox());
//                } else if (subIndex == 2) {
//                    Application.showForm(new FormRead());
//                } else {
//                    action.cancel();
//                }
            } else if (index == 2) {
                Application.appService.openServiceList();
            } else if (index == 3) {
                Application.appService.openSaleList();
            } else if (index == 4) {
                Application.appService.openPurchaseList();
            } else if (index == 5) {
                Application.appService.openCustomerList();
            } else if (index == 6) {
                Application.appService.openSupplierList();
            } else if (index == 7) {
                if (Application.getUser().getRoleId() == 1) {
                    Application.appService.openUserList();
                } else {
                    Application.appService.openDashboard();
                }
            } else if (index == 8) {
                Application.appService.openPOS();
            } else if (index == 9) {
                Application.logout();
            } else {
                action.cancel();
            }
        });
    }

    /* Setup menu icon and menu status */
    private void setMenuFull(boolean full) {
        String icon;
        if (getComponentOrientation().isLeftToRight()) {
            icon = (full) ? "menu_left.svg" : "menu_right.svg";
        } else {
            icon = (full) ? "menu_right.svg" : "menu_left.svg";
        }
        menuButton.setIcon(new FlatSVGIcon("ewision/sahan/icon/svg/" + icon, 0.8f));
        menu.setMenuFull(full);
        revalidate();
    }

    public void hideMenu() {
        menu.hideMenuItem();
    }

    //public void showForm(Component component) {
    public void showForm(JPanel panel) {
        panelBody.removeAll();
        panelBody.add(panel);
        panelBody.repaint();
        panelBody.revalidate();
        SwingUtilities.updateComponentTreeUI(this);
    }

    public void setSelectedMenu(int index, int subIndex) {
        menu.setSelectedMenu(index, subIndex);
    }

    private class MainFormLayout implements LayoutManager {

        @Override
        public void addLayoutComponent(String name, Component comp) {
        }

        @Override
        public void removeLayoutComponent(Component comp) {
        }

        @Override
        public Dimension preferredLayoutSize(Container parent) {
            synchronized (parent.getTreeLock()) {
                return new Dimension(5, 5);
            }
        }

        @Override
        public Dimension minimumLayoutSize(Container parent) {
            synchronized (parent.getTreeLock()) {
                return new Dimension(0, 0);
            }
        }

        @Override
        public void layoutContainer(Container parent) {
            synchronized (parent.getTreeLock()) {
                boolean ltr = parent.getComponentOrientation().isLeftToRight();
                Insets insets = UIScale.scale(parent.getInsets());
                int x = insets.left;
                int y = insets.top;
                int width = parent.getWidth() - (insets.left + insets.right);
                int height = parent.getHeight() - (insets.top + insets.bottom);
                int menuWidth = UIScale.scale(menu.isMenuFull() ? menu.getMenuMaxWidth() : menu.getMenuMinWidth());
                int menuX = ltr ? x : x + width - menuWidth;
                menu.setBounds(menuX, y, menuWidth, height);
                int menuButtonWidth = menuButton.getPreferredSize().width;
                int menuButtonHeight = menuButton.getPreferredSize().height;
                int menubX;
                if (ltr) {
                    menubX = (int) (x + menuWidth - (menuButtonWidth * (menu.isMenuFull() ? 0.5f : 0.3f)));
                } else {
                    menubX = (int) (menuX - (menuButtonWidth * (menu.isMenuFull() ? 0.5f : 0.7f)));
                }
                menuButton.setBounds(menubX, UIScale.scale(30), menuButtonWidth, menuButtonHeight);

                int gap = UIScale.scale(5);
                int bodyWidth = width - menuWidth - gap;
                int bodyHeight = height;
                int bodyx = ltr ? (x + menuWidth + gap) : x;
                int bodyy = y;

                headerBar.setBounds(bodyx, bodyy, bodyWidth, 60);
                panelBody.setBounds(bodyx, bodyy + 60, bodyWidth, bodyHeight - 60);
                //panelBody.setBounds(bodyx, bodyy, bodyWidth, bodyHeight);
            }
        }
    }
}
