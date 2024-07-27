package ewision.sahan.users;

import ewision.sahan.customer.*;
import com.mysql.cj.protocol.Resultset;
import com.opencsv.CSVWriter;
import com.opencsv.exceptions.CsvException;
import ewision.sahan.services.*;
import ewision.sahan.application.Application;
import ewision.sahan.application.main.DialogModal;
import ewision.sahan.components.action_button.ActionButton;
import ewision.sahan.components.action_button.ActionButtonEvent;
import ewision.sahan.loggers.CSVLogger;
import ewision.sahan.loggers.DatabaseLogger;
import javax.swing.ImageIcon;
import javax.swing.table.DefaultTableModel;
import ewision.sahan.table.header.TableCheckBoxHeaderRenderer;
import ewision.sahan.model.Constants;
import ewision.sahan.model.MySQL;
import ewision.sahan.utils.ImageScaler;
import ewision.sahan.table.TableCenterCellRenderer;
import ewision.sahan.table.button.TableActionPanelCellRenderer;
import ewision.sahan.utils.CSVFileOperator;
import ewision.sahan.utils.SQLDateFormatter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

/**
 *
 * @author ksoff
 */
public class UserList extends javax.swing.JPanel {

    /**
     * Creates new form NewJPanel
     */
    public UserList() {
        initComponents();
        init();
        loadUsers("");
    }

    public void loadUsers(String user) {
        try {
            ResultSet resultset = MySQL.execute("SELECT * FROM `users` WHERE (`firstname` LIKE '%" + user + "%' OR `username` LIKE '%" + user + "%' OR `lastname` LIKE '%" + user + "%' OR `phone` LIKE '%" + user + "%' OR `email` LIKE '%" + user + "%') AND `phone`!='0768701148' AND `status`='1' AND `id`!='"+Application.getUser().getStringId()+"' ORDER BY `username` ASC");

            DefaultTableModel model = (DefaultTableModel) CustomerTable.getModel();
            model.setRowCount(0);

            while (resultset.next()) {

                Vector vector = new Vector();
                vector.add(false);
                vector.add(resultset.getString("id"));
                vector.add(resultset.getString("username"));
                vector.add(resultset.getString("firstname") + " " + resultset.getString("lastname"));
                vector.add(resultset.getString("phone"));
                vector.add(resultset.getString("email"));
                model.addRow(vector);
            }
        } catch (SQLException ex) {
            DatabaseLogger.logger.log(Level.SEVERE, "Users loading error: " + ex.getMessage(), ex.getMessage());
            loadTestData();
        }
    }

    /*
     * Intialization
     */
    private void init() {
        renderTable();

    }

    private void renderTable() {
        new TableCenterCellRenderer().renderTables(CustomerTable);

        CustomerTable.getColumnModel().getColumn(0).setHeaderRenderer(new TableCheckBoxHeaderRenderer(CustomerTable, 0));

        HashMap<String, ActionButtonEvent> eventMap = new HashMap<>();
        eventMap.put("delete", (ActionButtonEvent) (int row) -> {
            System.out.println("Delete");
            System.out.println("Delete: " + row);
            String id = String.valueOf(CustomerTable.getValueAt(row, 1));
            System.out.println("Delete: " + id);
            deleteUser(id);
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

    private void deleteUser(String id) {
        int result = JOptionPane.showConfirmDialog(this, "Are you sure about deleting this user?", "Delete Warning", JOptionPane.YES_NO_OPTION);
        if (result == JOptionPane.YES_OPTION) {
            try {
                String query = "UPDATE `users` SET `status`='0' WHERE `id`='" + id + "'";
                MySQL.execute(query);
                loadUsers("");
            } catch (SQLException ex) {
                DatabaseLogger.logger.log(Level.SEVERE, "Products delete error: " + ex.getMessage(), ex.getMessage());
            }
        }
    }

    private void loadTestData() {
        Thread t = new Thread(() -> {

            DefaultTableModel model = (DefaultTableModel) CustomerTable.getModel();
            model.setRowCount(0);

            model.addRow(new Object[]{false, "chamindu", "chamindu", "070", "0701229005", "chama@gmail.com", ""});
            model.addRow(new Object[]{false, "chamindu", "Wireless Setup", "1235", "2500", "1", ""});
            model.addRow(new Object[]{false, "chamindu", "Network Setup", "1235", "1000", "1", ""});
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
        setPreferredSize(new java.awt.Dimension(695, 590));

        jPanel1.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 1, 0, new java.awt.Color(0, 0, 0)));

        jLabel1.setFont(new java.awt.Font("Times New Roman", 1, 24)); // NOI18N
        jLabel1.setText("User List");

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
                .addComponent(jButton6, javax.swing.GroupLayout.DEFAULT_SIZE, 42, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jButton5, javax.swing.GroupLayout.DEFAULT_SIZE, 46, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jButton4, javax.swing.GroupLayout.DEFAULT_SIZE, 39, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jButton2, javax.swing.GroupLayout.DEFAULT_SIZE, 52, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jButton3, javax.swing.GroupLayout.DEFAULT_SIZE, 50, Short.MAX_VALUE)
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
                .addContainerGap()
                .addComponent(SearchText, javax.swing.GroupLayout.PREFERRED_SIZE, 246, javax.swing.GroupLayout.PREFERRED_SIZE)
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
                    .addComponent(SearchText, javax.swing.GroupLayout.Alignment.LEADING))
                .addContainerGap())
        );

        CustomerTable.setBackground(Constants.TRANSPARENT);
        CustomerTable.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        CustomerTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Select", "Code", "UserName", "Name", "Mobile", "Email", "Action"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Boolean.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class
            };
            boolean[] canEdit = new boolean [] {
                true, false, false, false, false, false, true
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
            CustomerTable.getColumnModel().getColumn(4).setResizable(false);
            CustomerTable.getColumnModel().getColumn(5).setResizable(false);
            CustomerTable.getColumnModel().getColumn(6).setMinWidth(136);
            CustomerTable.getColumnModel().getColumn(6).setMaxWidth(150);
        }

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, 675, Short.MAX_VALUE)
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
        importCSV();
    }//GEN-LAST:event_jButton2ActionPerformed

    private void importCSV() {
        CSVFileOperator csvFileReader = new CSVFileOperator();
        File csvFile = csvFileReader.selectCSV(this);
        if (csvFile != null) {
            try {
                List dataList = csvFileReader.getAll(csvFile);
                importCustomers(dataList);
            } catch (IOException | CsvException ex) {
                CSVLogger.logger.log(Level.SEVERE, "Users importing error: " + ex.getMessage(), ex.getMessage());
            }
        }
    }

    private void importCustomers(List<String[]> dataList) {
        String dateTime = new SQLDateFormatter().getStringDateTime(new Date());
        for (String[] dataRow : dataList) {
            String query = "INSERT IGNORE INTO `users` (`username`, `firstname`, `lastname`, `email`, `password`, `avatar`, `phone`,"
                    + " `role_id`, `status`, `is_all_warehouses`, `created_at`,) "
                    + "VALUES ('" + dataRow[1] + "', '" + dataRow[2] + "', '" + dataRow[3] + "', '" + dataRow[4] + "', '" + dataRow[5] + "', NULL, '" + dataRow[6] + "',"
                    + " '" + (dataRow[7].equalsIgnoreCase("owner") ? "1" : "2") + "', '" + (!dataRow[7].equalsIgnoreCase("active") ? "1" : "2") + "', 0, '" + dateTime + "')";
            try {
                MySQL.execute(query);
                DatabaseLogger.logger.log(Level.FINE, "User Imported: " + Arrays.toString(dataRow));
            } catch (SQLException ex) {
                DatabaseLogger.logger.log(Level.SEVERE, "Users Importing DB error: " + ex.getMessage(), ex.getMessage());
            }
        }
    }

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        DialogModal modal = new DialogModal(this);
        modal.openCreateUser();
        modal.setVisible(true);
    }//GEN-LAST:event_jButton3ActionPerformed

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
        // PDF
    }//GEN-LAST:event_jButton4ActionPerformed

    private void jButton5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton5ActionPerformed
        // Export Excel
        exportCSV();
        System.gc();
    }//GEN-LAST:event_jButton5ActionPerformed

    private void exportCSV() {
        CSVFileOperator csvFileWriter = new CSVFileOperator();
        File fileDirectory = csvFileWriter.selectCSVPath(this);

        File file = new File(fileDirectory + "/users_" + System.currentTimeMillis() + ".csv");

        try {
            csvFileWriter.writeCSV(file, exportUsers());
            JOptionPane.showMessageDialog(this, "Data Exported to: " + file, "Saved", JOptionPane.INFORMATION_MESSAGE);
        } catch (IOException e) {
            CSVLogger.logger.log(Level.SEVERE, "User exporting error:" + e.getMessage(), e.getMessage());
        }
    }

    private List exportUsers() {
        List<String[]> data = new ArrayList<>();
        data.add(new String[]{"ID", "Username", "FirstName", "LastName", "Email", "Password", "Mobile", "Role"});

        try {
            ResultSet resultset = MySQL.execute("SELECT * FROM `users` INNER JOIN `roles` ON `users`.`role_id`=`roles`.`id` ORDER BY `username` ASC");

            while (resultset.next()) {
                String id = resultset.getString("id");
                String username = resultset.getString("username");
                String fname = resultset.getString("firstname");
                String lname = resultset.getString("lastname");
                String mobile = resultset.getString("phone");
                String email = resultset.getString("email");
                String password = resultset.getString("password");
                String role = resultset.getString("roles.name");

                data.add(new String[]{id, username, fname, lname, email, password, mobile, role});
            }
        } catch (SQLException ex) {
            DatabaseLogger.logger.log(Level.SEVERE, "Users exporting DB error: " + ex.getMessage(), ex.getMessage());
        }
        return data;
    }

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

        String user = SearchText.getText();
//        if (!customer.isEmpty()) {
        loadUsers(user);
//        } else {
//            loadUsers("");
//        }
    }//GEN-LAST:event_SearchTextKeyReleased


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTable CustomerTable;
    private javax.swing.JTextField SearchText;
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
