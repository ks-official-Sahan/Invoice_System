package ewision.sahan.menu;

import ewision.sahan.menu.MenuSelectedEvent;
import ewision.sahan.model.ModelMenu;
import ewision.sahan.utils.ImageScaler;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import net.miginfocom.swing.MigLayout;

/**
 *
 * @author ksoff
 */
public class Menu extends javax.swing.JPanel {
    
    private MigLayout layout;
    private JPanel panelMenu;
    private JButton cmdMenu;
    //private ButtonCustom cmdLogOut;
    private JButton cmdLogOut;
    private Header header;
    private Bottom bottom;
    private MenuSelectedEvent event;
    
    public void setEvent(MenuSelectedEvent event) {
        this.event = event;
    }

    /**
     * Creates new form Menu
     */
    public Menu() {
        initComponents();
        setOpaque(false);
        init();
    }
    
    private void init() {
        //putClientProperty(FlatClientProperties.STYLE, "arc:20");
        
        setLayout(new MigLayout("wrap, fillx, insets 0", "[fill]", "5[100]40[]push[60]0")); //sets bottom to bottom
        //setLayout(new MigLayout("wrap, fillx, insets 0", "[fill]", "5[]0"));
        panelMenu = new JPanel();
        header = new Header();
        bottom = new Bottom();
        
        createButtonMenu();
        createLogoutButton();
        panelMenu.setOpaque(false);
        layout = new MigLayout("fillx, wrap", "0[fill]0", "5[]0");
        panelMenu.setLayout(layout);
        
        add(cmdMenu, "pos 1al 0al 100% 20");
        add(cmdLogOut, "pos 1al 1al 100% 100, height 70!");
        add(header);
        add(panelMenu);
        add(bottom);
    }
    
    private void createLogoutButton() {
//        cmdLogOut = new ButtonCustom();
//        cmdLogOut.addMouseListener(new MouseAdapter() {
//            @Override
//            public void mouseEntered(MouseEvent e) {
//                cmdLogOut.setMouseOver(true);
//                cmdLogOut.repaint();
//            }
//
//            @Override
//            public void mouseExited(MouseEvent e) {
//                cmdLogOut.setMouseOver(false);
//                cmdLogOut.repaint();
//            }
//        });
        cmdLogOut = new JButton();
        cmdLogOut.setBorder(new EmptyBorder(0, 0, 0, 10));
        cmdLogOut.setContentAreaFilled(false);
        cmdLogOut.setCursor(new Cursor(Cursor.HAND_CURSOR));
        //cmdLogOut.setIcon(new ImageIcon(getClass().getResource("/ewision/sahan/icon/png/edit-30.png")));
        cmdLogOut.setIcon(new ImageScaler().getSvgIcon("logout.svg", 30));
    }
    
    private void createButtonMenu() {
        cmdMenu = new JButton();
        cmdMenu.setBorder(new EmptyBorder(0, 0, 0, 10));
        cmdMenu.setContentAreaFilled(false);
        cmdMenu.setCursor(new Cursor(Cursor.HAND_CURSOR));
        //cmdMenu.setIcon(new ImageIcon(getClass().getResource("/ewision/sahan/icon/png/view-30.png")));

        //cmdMenu.setIcon(new FlatSVGIcon("ewision//sahan//icon//svg//menu.svg", 30, 30));
        //FlatSVGIcon icon = new FlatSVGIcon(getClass().getResource("/ewision//sahan//icon//svg//menu.svg"));
        //icon = icon.derive(30, 30);
        //cmdMenu.setIcon(icon);
        
        cmdMenu.setIcon(new ImageScaler().getSvgIcon("menu.svg", 30));
        //cmdMenu.setBorder(new EmptyBorder(0, 0, 0, 0));
    }
    
    public void addEventMenu(ActionListener event) {
        cmdMenu.addActionListener(event);
    }
    
    public void addEventLogout(ActionListener event) {
        cmdLogOut.addActionListener(event);
    }
    
    public void addMenu(ModelMenu menu) {
        addMenu(menu, false);
    }
    public void addMenu(ModelMenu menu, boolean isSelected) {
        MenuItem item = new MenuItem(menu.getIcon(), menu.getMenuName(), panelMenu.getComponentCount(), isSelected);
        item.addEvent(new MenuSelectedEvent() {
            @Override
            public void selected(int index) {
                clearMenu(index);
            }
        });
        item.addEvent(event);
        panelMenu.add(item);
    }
    
    private void clearMenu(int selectedIndex) {
        for (Component com : panelMenu.getComponents()) {
            MenuItem item = (MenuItem) com;
            if (item.getIndex() != selectedIndex) {
                item.setSelected(false);
            }
        }
    }
    
    public void setAlpha(float alpha) {
        header.setAlpha(alpha);
        bottom.setAlpha(alpha);
    }
    
    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
//        GradientPaint gradientPaint = new GradientPaint(0, 0, Color.decode("#60a5fa"), 0, getHeight(), Color.decode("#047857"));
//        g2.setPaint(gradientPaint);
        g2.setColor(new Color(49,62,74));
        g2.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);
        super.paintComponent(g);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 255, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 685, Short.MAX_VALUE)
        );
    }// </editor-fold>//GEN-END:initComponents

    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
}
