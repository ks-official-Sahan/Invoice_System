package ewision.sahan.services;

import com.opencsv.exceptions.CsvException;
import ewision.sahan.application.Application;
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
import ewision.sahan.table.TableImageCellRenderer;
import ewision.sahan.utils.CSVFileOperator;
import ewision.sahan.utils.SQLDateFormatter;
import java.io.File;
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
import javax.swing.JOptionPane;

/**
 *
 * @author ksoff
 */
public class ServiceList extends javax.swing.JPanel {

    //private HashMap<String, ActionButtonEvent> eventMap = new HashMap<>();
    /**
     * Creates new form NewJPanel
     */
    public ServiceList() {
        initComponents();
        init();
    }

    /*
     * Intialization
     */
    private void init() {
        renderTable();
        //loadTestData();
        loadServices("");
    }

    private void renderTable() {
        new TableCenterCellRenderer().renderTables(jTable1);

        jTable1.getColumnModel().getColumn(0).setHeaderRenderer(new TableCheckBoxHeaderRenderer(jTable1, 0));
        jTable1.getColumn("Image").setCellRenderer(new TableImageCellRenderer());

        HashMap<String, ActionButtonEvent> eventMap = new HashMap<>();
        eventMap.put("delete", (ActionButtonEvent) (int row) -> {
            System.out.println("Delete: " + row);
            System.out.println("Delete: " + String.valueOf(jTable1.getValueAt(row, 8)));
            String id = String.valueOf(jTable1.getValueAt(row, 1));
            System.out.println("Delete: " + id);
            deleteProduct(id);
        });
        eventMap.put("view", (ActionButtonEvent) (int row) -> {
            System.out.println("View");
        });
        eventMap.put("edit", (ActionButtonEvent) (int row) -> {
            System.out.println("Edit");
        });
        jTable1.getColumn("Action").setCellRenderer(new TableActionPanelCellRenderer(ActionButton.VIEW_EDIT_DELETE_BUTTON, eventMap));
        //jTable1.getColumnModel().getColumn(8).setCellRenderer(new TableActionCellRender());
    }

    private void deleteProduct(String id) {
        int result = JOptionPane.showConfirmDialog(this, "Are you sure about deleting this product?", "Delete Warning", JOptionPane.YES_NO_OPTION);
        if (result == JOptionPane.YES_OPTION) {
            try {
                String query = "UPDATE `products` SET `is_active`='0' WHERE `id`='" + id + "'";
                MySQL.execute(query);
                loadServices("");
            } catch (SQLException ex) {
                DatabaseLogger.logger.log(Level.SEVERE, "Products delete error: " + ex.getMessage(), ex.getMessage());
            }
        }
    }

    private void loadServices(String service) {
        try {
            String query = "SELECT * FROM `products` "
                    //                    + "INNER JOIN `categories` ON `categories`.`id`=`products`.`category_id` "
                    + "WHERE `product_type`='service' AND `is_active`='1' AND (`products`.`name` LIKE '%" + service + "%' OR `products`.`code` LIKE '%" + service + "%' OR `products`.`id` LIKE '%" + service + "%') ORDER BY `products`.`code` ASC";
            ResultSet resultSet = MySQL.execute(query);

            DefaultTableModel model = (DefaultTableModel) jTable1.getModel();
            model.setRowCount(0);

            ImageIcon image = new ImageScaler().getScaledIcon(Constants.GRADIENT_ICON, jTable1.getRowHeight() -10, jTable1.getRowHeight() - 10);
            while (resultSet.next()) {

                Vector row = new Vector();
                row.add(false);
                row.add(resultSet.getInt("products.id"));
                row.add(image);
                row.add(resultSet.getString("products.name"));
                row.add(resultSet.getString("products.code"));
                //row.add(resultSet.getString("price"));
                row.add(resultSet.getDouble("products.price"));
                //row.add("1" + resultSet.getString("units.ShortName"));
                //row.add(resultSet.getDouble("operator_value") + resultSet.getString("units.ShortName"));
                //row.add(resultSet.getDouble("operator_value") + resultSet.getString("units.ShortName") + " : " + resultSet.getString("units.name"));

                model.addRow(row);
            }
        } catch (SQLException ex) {
            DatabaseLogger.logger.log(Level.SEVERE, "Services loading error: " + ex.getMessage(), ex.getMessage());
            loadTestData();
        }
    }

    private void loadTestData() {
        Thread t = new Thread(() -> {

            DefaultTableModel model = (DefaultTableModel) jTable1.getModel();
            model.setRowCount(0);

            ImageScaler scaler = new ImageScaler();

            ImageIcon image1 = scaler.getScaledIcon(Constants.GRADIENT_ICON, jTable1.getRowHeight() + 20, jTable1.getRowHeight() + 20);
            ImageIcon image2 = scaler.getScaledIcon(Constants.WHITE_LOGO, jTable1.getRowHeight() + 20, jTable1.getRowHeight() + 20);
            ImageIcon image3 = scaler.getScaledIcon(Constants.WHITE_LOGO, jTable1.getRowHeight() + 20, jTable1.getRowHeight() + 20);

            model.addRow(new Object[]{false, "2", image1, "Wired Setup", "1234", "3500", ""});
            model.addRow(new Object[]{false, "3", image2, "Wireless Setup", "1235", "2500", ""});
            model.addRow(new Object[]{false, "5", image3, "Network Setup", "1235", "1000", ""});
            //SwingUtilities.updateComponentTreeUI(jTable1);
        });
        t.start();
    }

    private void importCSV() {
        CSVFileOperator csvFileReader = new CSVFileOperator();
        File csvFile = csvFileReader.selectCSV(this);
        if (csvFile != null) {
            try {
                List dataList = csvFileReader.getAll(csvFile);
                importServices(dataList);
            } catch (IOException | CsvException ex) {
                CSVLogger.logger.log(Level.SEVERE, "Customers importing error: " + ex.getMessage(), ex.getMessage());
            }
        }
    }

    private void importServices(List<String[]> dataList) {
        String dateTime = new SQLDateFormatter().getStringDateTime(new Date());
        for (String[] dataRow : dataList) {
            String category = categoryMap.get(dataRow[4]) == null ? "NULL" : String.valueOf(categoryMap.get(dataRow[6]));
            String barcodeType = barcodeTypeMap.get(dataRow[5]) == null ? "NULL" : String.valueOf(barcodeTypeMap.get(dataRow[9]));
            String query = "INSERT IGNORE INTO `products` (`code`, `name`, `cost`, `price`, `categories_id`, `barcode_type_id`, `note`, `product_type`) "
                    + "VALUES ('" + dataRow[2] + "', '" + dataRow[1] + "', '" + dataRow[3] + "', '" + dataRow[4] + "', '" + category + "', '" + barcodeType + "', '" + dataRow[8] + "', '" + dataRow[10] + "')";
            try {
                MySQL.execute(query);
                DatabaseLogger.logger.log(Level.FINE, "Customers Imported: {0}", Arrays.toString(dataRow));
            } catch (SQLException ex) {
                DatabaseLogger.logger.log(Level.SEVERE, "Customers Importing DB error: " + ex.getMessage(), ex.getMessage());
            }
        }
    }

    private void loadData() {
        loadCategories();
        loadBarcodeTypes();
    }

    private HashMap<String, Integer> categoryMap;
    private HashMap<String, Integer> barcodeTypeMap;

    private void loadCategories() {
        categoryMap = new HashMap<>();
        try {
            ResultSet resultSet = MySQL.execute("SELECT * FROM `categories` ORDER BY `name`");

            categoryMap.clear();
            while (resultSet.next()) {
                categoryMap.put(resultSet.getString("name"), resultSet.getInt("id"));
            }

        } catch (SQLException ex) {
            DatabaseLogger.logger.log(Level.SEVERE, "Category loading error: " + ex.getMessage(), ex.getMessage());
        }
    }

    private void loadBarcodeTypes() {
        barcodeTypeMap = new HashMap<>();
        try {
            ResultSet resultSet = MySQL.execute("SELECT * FROM `barcode_type` ORDER BY `barcode_type`");

            barcodeTypeMap.clear();
            while (resultSet.next()) {
                barcodeTypeMap.put(resultSet.getString("barcode_type"), resultSet.getInt("id"));
            }
        } catch (SQLException ex) {
            DatabaseLogger.logger.log(Level.SEVERE, "Barcode Type loading error: " + ex.getMessage(), ex.getMessage());
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
        searchField = new javax.swing.JTextField();
        jPanel3 = new javax.swing.JPanel();
        jButton6 = new javax.swing.JButton();
        jButton5 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        jButton4 = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();

        setBorder(javax.swing.BorderFactory.createEmptyBorder(10, 10, 10, 10));
        setPreferredSize(new java.awt.Dimension(550, 590));

        jPanel1.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 1, 0, new java.awt.Color(0, 0, 0)));

        jLabel1.setFont(new java.awt.Font("Times New Roman", 1, 24)); // NOI18N
        jLabel1.setText("Service List");

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

        searchField.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                searchFieldKeyReleased(evt);
            }
        });

        jButton6.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jButton6.setForeground(new java.awt.Color(51, 204, 255));
        jButton6.setText("Filter");
        jButton6.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 153, 204)));
        jButton6.setOpaque(true);
        jButton6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton6ActionPerformed(evt);
            }
        });

        jButton5.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jButton5.setForeground(new java.awt.Color(0, 204, 0));
        jButton5.setText("EXCEL");
        jButton5.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 204, 0)));
        jButton5.setOpaque(true);
        jButton5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton5ActionPerformed(evt);
            }
        });

        jButton2.setBackground(new java.awt.Color(51, 102, 255));
        jButton2.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jButton2.setText("Import Services");
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
        jButton4.setOpaque(true);
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
                .addContainerGap()
                .addComponent(searchField, javax.swing.GroupLayout.PREFERRED_SIZE, 240, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(128, 128, 128)
                .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addComponent(searchField, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
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
                "Select", "ID", "Image", "Name", "Code", "Charge", "Action"
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
            jTable1.getColumnModel().getColumn(0).setPreferredWidth(50);
            jTable1.getColumnModel().getColumn(0).setMaxWidth(50);
            jTable1.getColumnModel().getColumn(1).setPreferredWidth(50);
            jTable1.getColumnModel().getColumn(1).setMaxWidth(50);
            jTable1.getColumnModel().getColumn(2).setMinWidth(75);
            jTable1.getColumnModel().getColumn(2).setPreferredWidth(100);
            jTable1.getColumnModel().getColumn(3).setMinWidth(150);
            jTable1.getColumnModel().getColumn(3).setPreferredWidth(300);
            jTable1.getColumnModel().getColumn(3).setMaxWidth(300);
            jTable1.getColumnModel().getColumn(4).setResizable(false);
            jTable1.getColumnModel().getColumn(5).setResizable(false);
            jTable1.getColumnModel().getColumn(6).setMinWidth(136);
            jTable1.getColumnModel().getColumn(6).setMaxWidth(150);
        }

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, 660, Short.MAX_VALUE)
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
        loadData();
        importCSV();
        barcodeTypeMap = null;
        categoryMap = null;
        System.gc();
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        // Create
        Application.appService.openCreateService();
    }//GEN-LAST:event_jButton3ActionPerformed

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
        // PDF
    }//GEN-LAST:event_jButton4ActionPerformed

    private void jButton5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton5ActionPerformed
        // Excel
        exportCSV();
        System.gc();
    }//GEN-LAST:event_jButton5ActionPerformed

    private void exportCSV() {
        CSVFileOperator csvFileWriter = new CSVFileOperator();
        File fileDirectory = csvFileWriter.selectCSVPath(this);

        File file = new File(fileDirectory + "/services_" + System.currentTimeMillis() + ".csv");

        try {
            csvFileWriter.writeCSV(file, exportUsers());
            JOptionPane.showMessageDialog(this, "Data Exported to: " + file, "Saved", JOptionPane.INFORMATION_MESSAGE);
        } catch (IOException e) {
            CSVLogger.logger.log(Level.SEVERE, "Services exporting error:" + e.getMessage(), e.getMessage());
        }
    }

    private List exportUsers() {
        List<String[]> data = new ArrayList<>();
        data.add(new String[]{"ID", "Name", "Code", "Cost", "Price", "Type", "Category Id", "TaxNet", "Note", "Barcode Type Id", "Tax Method Id"});

        try {
//            ResultSet countryRS = MySQL.execute("SELECT * FROM `categories`");
//            HashMap<Integer, String> countryMap = new HashMap<>();
//            while (countryRS.next()) {
//                countryMap.put(countryRS.getInt("id"), countryRS.getString("name"));
//            }

            String query = "SELECT * FROM `products` WHERE `product_type`='service' ORDER BY `products`.`code` ASC";
            ResultSet resultset = MySQL.execute(query);
            while (resultset.next()) {
                String id = resultset.getString("id");
                String code = resultset.getString("code");
                String name = resultset.getString("name");
                String cost = resultset.getString("cost");
                String price = resultset.getString("price");
                String catId = resultset.getString("category_id");
                String taxNet = resultset.getString("TaxNet");
                String note = resultset.getString("note");
                String btId = resultset.getString("barcode_type_id");
                String tmId = resultset.getString("tax_method_id");
                String type = resultset.getString("product_type");

                data.add(new String[]{id, name, code, cost, price, type, catId, taxNet, note, btId, tmId});
            }
        } catch (SQLException ex) {
            DatabaseLogger.logger.log(Level.SEVERE, "Services exporting DB error: " + ex.getMessage(), ex.getMessage());
        }
        return data;
    }

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

    private void searchFieldKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_searchFieldKeyReleased
        // Search
        loadServices(searchField.getText());
    }//GEN-LAST:event_searchFieldKeyReleased


    // Variables declaration - do not modify//GEN-BEGIN:variables
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
    private javax.swing.JTable jTable1;
    private javax.swing.JTextField searchField;
    // End of variables declaration//GEN-END:variables
}
