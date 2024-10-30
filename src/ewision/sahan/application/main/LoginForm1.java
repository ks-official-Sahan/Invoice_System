package ewision.sahan.application.main;

import com.formdev.flatlaf.FlatClientProperties;
import ewision.sahan.application.Application;
import ewision.sahan.loggers.FileLogger;
import ewision.sahan.model.MySQL;
import ewision.sahan.model.Shop;
import ewision.sahan.model.User;
import ewision.sahan.utils.SQLDateFormatter;
import java.awt.HeadlessException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
//import net.miginfocom.swing.MigLayout;

/**
 *
 * @author ks.official.sahan
 */
public class LoginForm1 extends javax.swing.JPanel {

    public LoginForm1() {
        initComponents();
        background1.setBlur(panelLogin1);
        init();
    }

    private void init() {
        //setLayout(new MigLayout("al center center"));

        lbTitle.putClientProperty(FlatClientProperties.STYLE, ""
                + "font:$h1.font");

        txtPass.putClientProperty(FlatClientProperties.STYLE, ""
                + "showRevealButton:true;"
                + "showCapsLock:true");
        cmdLogin.putClientProperty(FlatClientProperties.STYLE, ""
                + "borderWidth:0;"
                + "focusWidth:0");
        txtUser.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "User Name");
        txtPass.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "Password");
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        background1 = new ewision.sahan.login.main.Background();
        panelLogin1 = new ewision.sahan.application.main.PanelLogin();
        lbTitle = new javax.swing.JLabel();
        lbUser = new javax.swing.JLabel();
        txtUser = new javax.swing.JTextField();
        lbPass = new javax.swing.JLabel();
        txtPass = new javax.swing.JPasswordField();
        cmdLogin = new javax.swing.JButton();

        panelLogin1.setOpaque(false);

        lbTitle.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lbTitle.setText("Login");
        panelLogin1.add(lbTitle);

        lbUser.setText("User Name");
        panelLogin1.add(lbUser);
        panelLogin1.add(txtUser);

        lbPass.setText("Password");
        panelLogin1.add(lbPass);
        panelLogin1.add(txtPass);

        cmdLogin.setText("Login");
        cmdLogin.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdLoginActionPerformed(evt);
            }
        });
        panelLogin1.add(cmdLogin);

        javax.swing.GroupLayout background1Layout = new javax.swing.GroupLayout(background1);
        background1.setLayout(background1Layout);
        background1Layout.setHorizontalGroup(
            background1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, background1Layout.createSequentialGroup()
                .addContainerGap(250, Short.MAX_VALUE)
                .addComponent(panelLogin1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(250, Short.MAX_VALUE))
        );
        background1Layout.setVerticalGroup(
            background1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, background1Layout.createSequentialGroup()
                .addContainerGap(150, Short.MAX_VALUE)
                .addComponent(panelLogin1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(150, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(background1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(0, 0, 0))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(background1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(0, 0, 0))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void cmdLoginActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdLoginActionPerformed
        /* Login */
        String username = txtUser.getText();
        String pass = String.valueOf(txtPass.getPassword());

        String date = new SQLDateFormatter().getStringDate(new Date());

        if (date == "2025-06-01") {
            JOptionPane.showMessageDialog(this, "Renew your software license. Contact developer: +94 768701148", "Warning", JOptionPane.ERROR_MESSAGE);
            return;
        }
        if (date == "2025-05-01") {
            JOptionPane.showMessageDialog(this, "Software license expires on 2025-06-01. \nContact developer: +94 768701148", "Warning", JOptionPane.ERROR_MESSAGE);
        }

        if (username.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Enter Your Username", "Warning", JOptionPane.ERROR_MESSAGE);
        } else if (pass.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Enter your Password", "Warning", JOptionPane.ERROR_MESSAGE);
        } else {
            try {
                ResultSet resultSet = MySQL.execute("SELECT * FROM `users` WHERE `username`='" + username + "' AND `password`='" + pass + "'");
                if (resultSet.next()) {
                    if (resultSet.getString("status").equals("1")) {
                        User user = new User(resultSet.getString("id"), resultSet.getString("role_id"), resultSet.getString("username"), resultSet.getString("email"), resultSet.getString("phone"));

                        try {
                            Shop shop;

                            File serial = new File("serial");
                            serial.mkdir();

                            File shop_file = new File("serial/_shop.ser");
                            // if (shop_file.exists() || shop_file.createNewFile()) {
                            if (shop_file.exists()) {
                                FileInputStream fis = new FileInputStream(shop_file);
                                ObjectInputStream ois = new ObjectInputStream(fis);
                                Object result = ois.readObject();
                                shop = (Shop) result;

                                ResultSet shopRs = MySQL.execute("SELECT * FROM `shop` WHERE `id`='" + resultSet.getString("shop_id") + "'");
                                shop = new Shop();
                                if (shopRs.next()) {
                                    shop.setId(shopRs.getInt("id"));
                                    shop.setName(shopRs.getString("name"));
                                    shop.setAddress(shopRs.getString("address"));
                                    shop.setEmail(shopRs.getString("email"));
                                    shop.setMobile(shopRs.getString("mobile"));
                                    shop.setLogoPath(shopRs.getString("logoPath"));
                                    shop.setLogo2Path(shopRs.getString("logo_black_path"));
                                } else {
                                    // Add shop information popup
                                }

                                FileOutputStream fos = new FileOutputStream(shop_file);
                                ObjectOutputStream oos = new ObjectOutputStream(fos);
                                oos.writeObject(shop);
                            } else {
                                ResultSet shopRs = MySQL.execute("SELECT * FROM `shop` WHERE `id`='" + resultSet.getString("shop_id") + "'");
                                shop = new Shop();
                                if (shopRs.next()) {
                                    shop.setId(shopRs.getInt("id"));
                                    shop.setName(shopRs.getString("name"));
                                    shop.setAddress(shopRs.getString("address"));
                                    shop.setEmail(shopRs.getString("email"));
                                    shop.setMobile(shopRs.getString("mobile"));
                                    shop.setLogoPath(shopRs.getString("logoPath"));
                                    shop.setLogo2Path(shopRs.getString("logo_black_path"));
                                } else {
                                    // Add shop information popup
                                }

                                FileOutputStream fos = new FileOutputStream(shop_file);
                                ObjectOutputStream oos = new ObjectOutputStream(fos);
                                oos.writeObject(shop);
                            }

                            //Application application = new Application(user);
                            Application.setUser(user);
                            Application.setShop(shop);
                            Application.login();
                        } catch (IOException | ClassNotFoundException e) {
                            FileLogger.logger.log(Level.SEVERE, "Serialization failed " + e.getMessage(), e.getMessage());
                            JOptionPane.showMessageDialog(this, "Something went wrong", "Warning", JOptionPane.ERROR_MESSAGE);
                        }

                    } else {
                        JOptionPane.showMessageDialog(this, "User is blocked", "Warning", JOptionPane.ERROR_MESSAGE);
                    }
                } else {
                    JOptionPane.showMessageDialog(this, "Invalid Username or Password", "Warning", JOptionPane.ERROR_MESSAGE);
                }
            } catch (HeadlessException | SQLException e) {
                System.out.println(e.getMessage());
            }
        }
    }//GEN-LAST:event_cmdLoginActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private ewision.sahan.login.main.Background background1;
    private javax.swing.JButton cmdLogin;
    private javax.swing.JLabel lbPass;
    private javax.swing.JLabel lbTitle;
    private javax.swing.JLabel lbUser;
    private ewision.sahan.application.main.PanelLogin panelLogin1;
    private javax.swing.JPasswordField txtPass;
    private javax.swing.JTextField txtUser;
    // End of variables declaration//GEN-END:variables
}
