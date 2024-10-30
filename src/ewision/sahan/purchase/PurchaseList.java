package ewision.sahan.purchase;

import ewision.sahan.sale.*;
import ewision.sahan.application.Application;
import ewision.sahan.components.action_button.ActionButton;
import ewision.sahan.components.action_button.ActionButtonEvent;
import ewision.sahan.loggers.DatabaseLogger;
import javax.swing.ImageIcon;
import javax.swing.table.DefaultTableModel;
import ewision.sahan.table.header.TableCheckBoxHeaderRenderer;
import ewision.sahan.model.Constants;
import ewision.sahan.model.MySQL;
import ewision.sahan.utils.ImageScaler;
import ewision.sahan.table.TableCenterCellRenderer;
import ewision.sahan.table.button.TableActionPanelCellRenderer;
import ewision.sahan.table.TableImageCellRenderer;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;

/**
 *
 * @author ksoff
 */
public class PurchaseList extends javax.swing.JPanel {

    /**
     * Creates new form NewJPanel
     */
    public PurchaseList() {
        initComponents();
        init();
    }

    /*
     * Intialization
     */
    private void init() {
        renderTable();
        //loadTestData();
//        Thread load = new Thread(() -> {
//            loadSales("");
//        });
//        load.start();
        loadSales("");
    }

    private void renderTable() {
        new TableCenterCellRenderer().renderTables(jTable1);

//        jTable1.getColumnModel().getColumn(0).setHeaderRenderer(new TableCheckBoxHeaderRenderer(jTable1, 0));
//        jTable1.getColumn("Image").setCellRenderer(new TableImageCellRenderer());
        //jTable1.getColumnModel().getColumn(8).setCellRenderer(new TableActionCellRender());
        HashMap<String, ActionButtonEvent> eventMap = new HashMap<>();
        eventMap.put("view", (ActionButtonEvent) (int row) -> {
            System.out.println("View: " + row);
            String id = String.valueOf(jTable1.getValueAt(row, 0));
            System.out.println("View: " + id);
            Application.appService.openUpdatePurchase(id, false);
        });
        eventMap.put("edit", (ActionButtonEvent) (int row) -> {
            System.out.println("Edit: " + row);
            String id = String.valueOf(jTable1.getValueAt(row, 0));
            System.out.println("Edit: " + id);
            Application.appService.openUpdatePurchase(id, true);
        });
        if (Application.getUser().getRoleId() == 1) {
            eventMap.put("delete", (ActionButtonEvent) (int row) -> {
                System.out.println("delete: " + row);
                String id = String.valueOf(jTable1.getValueAt(row, 0));
                System.out.println("delete: " + id);
                deletePurchase(id);
            });
            Application.appService.openUserList();
            jTable1.getColumn("Action").setCellRenderer(new TableActionPanelCellRenderer(ActionButton.VIEW_EDIT_DELETE_BUTTON, eventMap));
        } else {
            jTable1.getColumn("Action").setCellRenderer(new TableActionPanelCellRenderer(ActionButton.VIEW_EDIT_BUTTON, eventMap));
        }
    }

    private void deletePurchase(String id) {
        int result = JOptionPane.showConfirmDialog(this, "Are you sure about deleting this Purchase?", "Delete Warning", JOptionPane.YES_NO_OPTION);
        if (result == JOptionPane.YES_OPTION) {
            boolean isSuccess = true;

            String pId = "0";
            try {
                String query = "SELECT * FROM `purchases` WHERE `Ref`='" + id + "'";
                ResultSet resultSet = MySQL.execute(query);
                if (resultSet.next()) {
                    pId = resultSet.getString("id");
                }
                //String query = "SELECT * FROM `sale_details` WHERE `sale_id`";
            } catch (SQLException ex) {
                isSuccess = false;
                DatabaseLogger.logger.log(Level.SEVERE, "Purchase delete error: " + ex.getMessage(), ex.getMessage());
            }
            if (isSuccess) {
                try {
                    String pQuery = "UPDATE `purchases` SET `statut`='delete' WHERE `Ref`='" + id + "' OR `id`='" + pId + "'";
                    MySQL.execute(pQuery);

                    //String query = "SELECT * FROM `sale_details` WHERE `sale_id`";
                } catch (SQLException ex) {
                    isSuccess = false;
                    DatabaseLogger.logger.log(Level.SEVERE, "Purchase delete error: " + ex.getMessage(), ex.getMessage());
                }
            }
            if (isSuccess) {
                try {
                    String pdQuery = "SELECT * FROM `purchase_details` WHERE `purchase_id`='" + pId + "'";
                    ResultSet execute = MySQL.execute(pdQuery);
                    while (execute.next()) {

                        String stock_id = execute.getString("stocks_id");
                        if (stock_id != null) {
                            String query = "UPDATE `stocks` SET `quantity`=`quantity`-" + execute.getDouble("quantity") + " WHERE `id`='" + stock_id + "'";
                            MySQL.execute(query);
                        }
                    }
                } catch (SQLException ex) {
                    isSuccess = false;
                    DatabaseLogger.logger.log(Level.SEVERE, "Purchase details delete error: " + ex.getMessage(), ex.getMessage());
                }
            }
            loadSales("");
        }
    }

    private void loadTestData() {
        Thread t = new Thread(() -> {

//            DefaultTableModel model = (DefaultTableModel) jTable1.getModel();
//            model.setRowCount(0);
//
//            ImageScaler scaler = new ImageScaler();
//
//            ImageIcon image1 = scaler.getScaledIcon(Constants.GRADIENT_ICON, jTable1.getRowHeight() + 20, jTable1.getRowHeight() + 20);
//            ImageIcon image2 = scaler.getScaledIcon(Constants.WHITE_LOGO, jTable1.getRowHeight() + 20, jTable1.getRowHeight() + 20);
//            ImageIcon image3 = scaler.getScaledIcon(Constants.WHITE_LOGO, jTable1.getRowHeight() + 20, jTable1.getRowHeight() + 20);
//
//            model.addRow(new Object[]{false, "1", image1, "Gents Shirt", "1234", "Nike", "1000", "1", "20", "1"});
//            model.addRow(new Object[]{false, "2", image2, "Gents Short", "1235", "Nike", "1200", "1", "30", "2"});
//            model.addRow(new Object[]{false, "3", image3, "Ladies Short", "1235", "Nike", "1200", "1", "30", "3"});
            //SwingUtilities.updateComponentTreeUI(jTable1);
        });
        t.start();
    }

    private void loadSales(String txt) {
        try {
            String query = "SELECT * FROM `purchases` "
                    + "INNER JOIN `users` ON `users`.`id`=`purchases`.`user_id` "
                    + "JOIN `providers` ON `providers`.`id`=`purchases`.`provider_id` "
                    + "WHERE (`purchases`.`Ref` LIKE '%" + txt + "%' "
                    + "OR `users`.`username` LIKE '%" + txt + "%' "
                    + "OR `providers`.`name` LIKE '%" + txt + "%') AND `statut`<>'delete' "
                    + "ORDER BY `date` DESC, `purchases`.`created_at` DESC";
            ResultSet resultSet = MySQL.execute(query);

            DefaultTableModel model = (DefaultTableModel) jTable1.getModel();
            model.setRowCount(0);

            while (resultSet.next()) {
                Vector row = new Vector();

                double due = 0.00;
                //due = (Double.parseDouble(resultSet.getString("purchases.GrandTotal")) - Double.parseDouble(resultSet.getString("purchases.discount")) - Double.parseDouble(resultSet.getString("purchases.paid_amount")));                
                double total = Double.parseDouble(resultSet.getString("purchases.GrandTotal"));
                double payment = Double.parseDouble(resultSet.getString("purchases.paid_amount"));
                due = (total - payment);
                due = (due >= 0 ? due : 0.00);
                payment = (due >= 0 ? payment : total);

                //row.add(false);
                row.add(resultSet.getString("purchases.Ref"));
                row.add(resultSet.getString("date"));
                //row.add(resultSet.getString("user_id"));
                row.add(resultSet.getString("users.username"));
                //row.add(resultSet.getString("provider_id"));
                row.add(resultSet.getString("providers.phone"));
                //row.add(resultSet.getString(""));
                row.add(resultSet.getString("purchases.statut"));
                row.add(total);
                row.add(payment);
                //row.add("");
                row.add(due);
                row.add(resultSet.getString("purchases.payment_statut"));
                //row.add(resultSet.getString("purchases.shipping_status"));

                model.addRow(row);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            DatabaseLogger.logger.log(Level.SEVERE, "Sale loading error: " + ex.getMessage(), ex.getMessage());
            loadTestData();
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        jTextField1 = new javax.swing.JTextField();
        jPanel3 = new javax.swing.JPanel();
        jButton6 = new javax.swing.JButton();
        jButton5 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        jButton4 = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();

        setBorder(javax.swing.BorderFactory.createEmptyBorder(10, 10, 10, 10));
        setPreferredSize(new java.awt.Dimension(700, 590));

        jPanel1.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 1, 0, new java.awt.Color(0, 0, 0)));

        jLabel1.setFont(new java.awt.Font("Times New Roman", 1, 24)); // NOI18N
        jLabel1.setText("Purchases List");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addContainerGap(14, Short.MAX_VALUE))
        );

        jPanel2.setBorder(javax.swing.BorderFactory.createEmptyBorder(5, 0, 5, 0));
        jPanel2.setPreferredSize(new java.awt.Dimension(600, 69));

        jTextField1.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTextField1KeyReleased(evt);
            }
        });

        jButton6.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jButton6.setForeground(new java.awt.Color(51, 204, 255));
        jButton6.setText("Filter");
        jButton6.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 153, 204)));
        jButton6.setEnabled(false);
        jButton6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton6ActionPerformed(evt);
            }
        });

        jButton5.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jButton5.setForeground(new java.awt.Color(0, 204, 0));
        jButton5.setText("EXCEL");
        jButton5.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 204, 0)));
        jButton5.setEnabled(false);
        jButton5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton5ActionPerformed(evt);
            }
        });

        jButton3.setBackground(new java.awt.Color(102, 204, 255));
        jButton3.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jButton3.setText("Create");
        jButton3.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(102, 204, 255)));
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        jButton4.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jButton4.setForeground(new java.awt.Color(255, 0, 51));
        jButton4.setText("PDF");
        jButton4.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 0, 0)));
        jButton4.setEnabled(false);
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jButton6, javax.swing.GroupLayout.DEFAULT_SIZE, 65, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jButton5, javax.swing.GroupLayout.DEFAULT_SIZE, 63, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jButton4, javax.swing.GroupLayout.DEFAULT_SIZE, 61, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jButton3, javax.swing.GroupLayout.DEFAULT_SIZE, 63, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton4, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton5, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton6, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(0, 0, 0))
        );

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, 290, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(78, 78, 78)
                .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 27, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap(18, Short.MAX_VALUE)
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        jTable1.setBackground(Constants.TRANSPARENT);
        jTable1.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Ref", "Date", "Added By", "Provider", "Status", "Grand Total", "Paid", "Due", "Payment Status", "Action"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false, false, false, true
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jTable1.setColumnSelectionAllowed(true);
        jTable1.setIntercellSpacing(new java.awt.Dimension(0, 1));
        jTable1.setMinimumSize(new java.awt.Dimension(0, 80));
        jTable1.setOpaque(false);
        jTable1.setRowHeight(80);
        jTable1.setShowGrid(false);
        jTable1.setShowHorizontalLines(true);
        jTable1.getTableHeader().setReorderingAllowed(false);
        jTable1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTable1MouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(jTable1);
        jTable1.getColumnModel().getSelectionModel().setSelectionMode(javax.swing.ListSelectionModel.SINGLE_INTERVAL_SELECTION);
        if (jTable1.getColumnModel().getColumnCount() > 0) {
            jTable1.getColumnModel().getColumn(1).setMinWidth(80);
            jTable1.getColumnModel().getColumn(9).setMinWidth(136);
            jTable1.getColumnModel().getColumn(9).setMaxWidth(150);
        }

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, 680, Short.MAX_VALUE)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane1)
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        // Create
        Application.appService.openCreatePurchase();
    }//GEN-LAST:event_jButton3ActionPerformed

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
        // PDF
    }//GEN-LAST:event_jButton4ActionPerformed

    private void jButton5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton5ActionPerformed
        // Excel
    }//GEN-LAST:event_jButton5ActionPerformed

    private void jButton6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton6ActionPerformed
        // Filter
    }//GEN-LAST:event_jButton6ActionPerformed

    private void jTable1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTable1MouseClicked
        // Update UI
//        Thread t = new Thread(() -> {
////            jTable1.revalidate();
////            jTable1.repaint();
////            try {
////                SwingUtilities.updateComponentTreeUI(jTable1);
////            } catch (NullPointerException e) {
////            }
//        });
//        t.start();
    }//GEN-LAST:event_jTable1MouseClicked

    private void jTextField1KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField1KeyReleased
        loadSales(jTextField1.getText());
    }//GEN-LAST:event_jTextField1KeyReleased


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JButton jButton5;
    private javax.swing.JButton jButton6;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTable1;
    private javax.swing.JTextField jTextField1;
    // End of variables declaration//GEN-END:variables
}
