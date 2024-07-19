package ewision.sahan.customer;

import com.mysql.cj.protocol.Resultset;
import ewision.sahan.services.*;
import ewision.sahan.application.Application;
import ewision.sahan.application.main.DialogModal;
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
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Vector;
import java.util.logging.Level;

/**
 *
 * @author ksoff
 */
public class CustomerList extends javax.swing.JPanel {

    /**
     * Creates new form NewJPanel
     */
    public CustomerList() {
        initComponents();
        init();
        loadCustomers("");
    }

    public void loadCustomers(String customer) {
        try {
            ResultSet resultset = MySQL.execute("SELECT * FROM `clients` WHERE `name` LIKE '%"+customer+"%' OR `phone` LIKE '%"+customer+"%' OR `email` LIKE '%"+customer+"%' ORDER BY `name` ASC");

            DefaultTableModel model = (DefaultTableModel) CustomerTable.getModel();
            model.setRowCount(0);

            while (resultset.next()) {

                Vector vector = new Vector();
                vector.add(false);
                vector.add(resultset.getString("id"));
                vector.add(resultset.getString("name"));
                vector.add(resultset.getString("phone"));
                vector.add(resultset.getString("email"));
                model.addRow(vector);
            }
        } catch (SQLException ex) {
            DatabaseLogger.logger.log(Level.SEVERE, "Customers loading error: " + ex.getMessage(), ex.getMessage());
            loadTestData();
        }
    }

    /*
     * Intialization
     */
    private void init() {
        cmdSearch.setIcon(new ImageScaler().getSvgIcon("/search", 28));
        cmdSearch.setContentAreaFilled(false);
        renderTable();
        
    }

    private void renderTable() {
        new TableCenterCellRenderer().renderTables(CustomerTable);

        CustomerTable.getColumnModel().getColumn(0).setHeaderRenderer(new TableCheckBoxHeaderRenderer(CustomerTable, 0));

        HashMap<String, ActionButtonEvent> eventMap = new HashMap<>();
        eventMap.put("delete", (ActionButtonEvent) (int row) -> {
            System.out.println("Delete");
        });
        eventMap.put("view", (ActionButtonEvent) (int row) -> {
            System.out.println("View");
        });
        eventMap.put("edit", (ActionButtonEvent) (int row) -> {
            System.out.println("Edit");
        });
        CustomerTable.getColumn("Action").setCellRenderer(new TableActionPanelCellRenderer(ActionButton.VIEW_EDIT_DELETE_BUTTON, eventMap));
        //jTable1.getColumnModel().getColumn(8).setCellRenderer(new TableActionCellRender());
        //CustomerTable.getColumn("Action").setCellRenderer(new TableActionPanelCellRenderer(3));
    }

    private void loadTestData() {
        Thread t = new Thread(() -> {

            DefaultTableModel model = (DefaultTableModel) CustomerTable.getModel();
            model.setRowCount(0);

            model.addRow(new Object[]{false, "chamindu", "070", "0701229005", "chama@gmail.com", ""});
            model.addRow(new Object[]{false, "Wireless Setup", "1235", "2500", "1", ""});
            model.addRow(new Object[]{false, "Network Setup", "1235", "1000", "1", ""});
            //SwingUtilities.updateComponentTreeUI(jTable1);
        });
        t.start();
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
        cmdSearch = new javax.swing.JButton();
        SearchText = new javax.swing.JTextField();
        jPanel3 = new javax.swing.JPanel();
        jButton6 = new javax.swing.JButton();
        jButton5 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        jButton4 = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        CustomerTable = new javax.swing.JTable();

        setBorder(javax.swing.BorderFactory.createEmptyBorder(10, 10, 10, 10));
        setPreferredSize(new java.awt.Dimension(550, 590));

        jPanel1.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 1, 0, new java.awt.Color(0, 0, 0)));

        jLabel1.setFont(new java.awt.Font("Times New Roman", 1, 24)); // NOI18N
        jLabel1.setText("Customer List");

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

        cmdSearch.setBackground(Constants.TRANSPARENT);
        cmdSearch.setMaximumSize(new java.awt.Dimension(34, 34));
        cmdSearch.setPreferredSize(new java.awt.Dimension(34, 34));

        SearchText.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                SearchTextActionPerformed(evt);
            }
        });
        SearchText.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                SearchTextKeyReleased(evt);
            }
        });

        jButton6.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jButton6.setForeground(new java.awt.Color(51, 204, 255));
        jButton6.setText("Filter");
        jButton6.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 153, 204)));
        jButton6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton6ActionPerformed(evt);
            }
        });

        jButton5.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jButton5.setForeground(new java.awt.Color(0, 204, 0));
        jButton5.setText("EXCEL");
        jButton5.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 204, 0)));
        jButton5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton5ActionPerformed(evt);
            }
        });

        jButton2.setBackground(new java.awt.Color(51, 102, 255));
        jButton2.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jButton2.setText("Import");
        jButton2.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(51, 102, 255)));
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
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
                .addComponent(jButton6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jButton5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jButton4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jButton2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jButton3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
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
                .addGap(12, 12, 12)
                .addComponent(cmdSearch, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(SearchText, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(128, 128, 128)
                .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap(18, Short.MAX_VALUE)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(jPanel3, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(SearchText, javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(cmdSearch, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        CustomerTable.setBackground(Constants.TRANSPARENT);
        CustomerTable.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        CustomerTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Select", "Code", "Name", "Mobile", "Email", "Action"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Boolean.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class
            };
            boolean[] canEdit = new boolean [] {
                true, false, false, false, false, true
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        CustomerTable.setColumnSelectionAllowed(true);
        CustomerTable.setIntercellSpacing(new java.awt.Dimension(0, 1));
        CustomerTable.setMinimumSize(new java.awt.Dimension(0, 80));
        CustomerTable.setOpaque(false);
        CustomerTable.setRowHeight(80);
        CustomerTable.setShowGrid(false);
        CustomerTable.setShowHorizontalLines(true);
        CustomerTable.getTableHeader().setReorderingAllowed(false);
        CustomerTable.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                CustomerTableMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(CustomerTable);
        CustomerTable.getColumnModel().getSelectionModel().setSelectionMode(javax.swing.ListSelectionModel.SINGLE_INTERVAL_SELECTION);
        if (CustomerTable.getColumnModel().getColumnCount() > 0) {
            CustomerTable.getColumnModel().getColumn(0).setPreferredWidth(50);
            CustomerTable.getColumnModel().getColumn(0).setMaxWidth(50);
            CustomerTable.getColumnModel().getColumn(1).setResizable(false);
            CustomerTable.getColumnModel().getColumn(3).setResizable(false);
            CustomerTable.getColumnModel().getColumn(4).setResizable(false);
            CustomerTable.getColumnModel().getColumn(5).setMinWidth(136);
            CustomerTable.getColumnModel().getColumn(5).setMaxWidth(150);
        }

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, 668, Short.MAX_VALUE)
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

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        // Import
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        DialogModal modal = new DialogModal(this);
        modal.openCreateCustomer();
        modal.setVisible(true);
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

    private void CustomerTableMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_CustomerTableMouseClicked
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
    }//GEN-LAST:event_CustomerTableMouseClicked

    private void SearchTextActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_SearchTextActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_SearchTextActionPerformed

    private void SearchTextKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_SearchTextKeyReleased

        String customer = SearchText.getText();
//        if (!customer.isEmpty()) {
            loadCustomers(customer);
//        } else {
//            loadCustomers("");
//        }
    }//GEN-LAST:event_SearchTextKeyReleased


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTable CustomerTable;
    private javax.swing.JTextField SearchText;
    private javax.swing.JButton cmdSearch;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JButton jButton5;
    private javax.swing.JButton jButton6;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JScrollPane jScrollPane1;
    // End of variables declaration//GEN-END:variables
}
