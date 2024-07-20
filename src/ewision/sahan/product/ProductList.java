package ewision.sahan.product;

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
import ewision.sahan.utils.CSVFileReader;
import ewision.sahan.utils.SQLDateFormatter;
import java.io.File;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Vector;
import java.util.logging.Level;

/**
 *
 * @author ksoff
 */
public class ProductList extends javax.swing.JPanel {

    /**
     * Creates new form NewJPanel
     */
    public ProductList() {
        initComponents();
        init();
    }

    /*
     * Intialization
     */
    private void init() {
        cmdSearch.setIcon(new ImageScaler().getSvgIcon("/search", 28));
        cmdSearch.setContentAreaFilled(false);
        renderTable();
        //loadTestData();
        loadProducts("");
    }

    private void renderTable() {
        new TableCenterCellRenderer().renderTables(jTable1);

        jTable1.getColumnModel().getColumn(0).setHeaderRenderer(new TableCheckBoxHeaderRenderer(jTable1, 0));
        jTable1.getColumn("Image").setCellRenderer(new TableImageCellRenderer());

        //jTable1.getColumnModel().getColumn(8).setCellRenderer(new TableActionCellRender());
        HashMap<String, ActionButtonEvent> eventMap = new HashMap<>();
        eventMap.put("delete", (ActionButtonEvent) (int row) -> {
            System.out.println("Delete: " + row);
            System.out.println("Delete: " + String.valueOf(jTable1.getValueAt(row, 8)));
        });
        eventMap.put("view", (ActionButtonEvent) (int row) -> {
            System.out.println("View: " + row);
        });
        eventMap.put("edit", (ActionButtonEvent) (int row) -> {
            System.out.println("Edit: " + row);
        });
        jTable1.getColumn("Action").setCellRenderer(new TableActionPanelCellRenderer(ActionButton.VIEW_EDIT_DELETE_BUTTON, eventMap));
    }

    private void loadTestData() {
        Thread t = new Thread(() -> {

            DefaultTableModel model = (DefaultTableModel) jTable1.getModel();
            model.setRowCount(0);

            ImageScaler scaler = new ImageScaler();

            ImageIcon image1 = scaler.getScaledIcon(Constants.GRADIENT_ICON, jTable1.getRowHeight() + 20, jTable1.getRowHeight() + 20);
            ImageIcon image2 = scaler.getScaledIcon(Constants.WHITE_LOGO, jTable1.getRowHeight() + 20, jTable1.getRowHeight() + 20);
            ImageIcon image3 = scaler.getScaledIcon(Constants.WHITE_LOGO, jTable1.getRowHeight() + 20, jTable1.getRowHeight() + 20);

            model.addRow(new Object[]{false, "1", image1, "Gents Shirt", "1234", "Nike", "1000", "1", "20", "1"});
            model.addRow(new Object[]{false, "2", image2, "Gents Short", "1235", "Nike", "1200", "1", "30", "2"});
            model.addRow(new Object[]{false, "3", image3, "Ladies Short", "1235", "Nike", "1200", "1", "30", "3"});
            //SwingUtilities.updateComponentTreeUI(jTable1);
        });
        t.start();
    }

    private void loadProducts(String product) {
        try {
            String query = "SELECT * FROM `products` "
                    + "INNER JOIN `brands` ON `products`.`brand_id`=`brands`.`id` "
                    + "INNER JOIN `categories` ON `categories`.`id`=`products`.`category_id` "
                    + "INNER JOIN `units` ON `products`.`unit_id`=`units`.`id` "
                    + "WHERE `product_type`='product' AND (`products`.`name` LIKE '%" + product + "%' OR `products`.`code` LIKE '%" + product + "%' OR `products`.`id` LIKE '%" + product + "%') ORDER BY `products`.`code` ASC";
            ResultSet resultSet = MySQL.execute(query);

            DefaultTableModel model = (DefaultTableModel) jTable1.getModel();
            model.setRowCount(0);

            ImageIcon image = new ImageScaler().getScaledIcon(Constants.GRADIENT_ICON, jTable1.getRowHeight() + 20, jTable1.getRowHeight() + 20);
            while (resultSet.next()) {
                Vector row = new Vector();
                row.add(false);
                row.add(resultSet.getInt("products.id"));
                row.add(image);
                row.add(resultSet.getString("products.name"));
                row.add(resultSet.getString("products.code"));
                row.add(resultSet.getString("brands.name"));
                //row.add(resultSet.getString("price"));
                row.add(resultSet.getDouble("products.price"));
                //row.add("1" + resultSet.getString("units.ShortName"));
                row.add(resultSet.getDouble("operator_value") + resultSet.getString("units.ShortName"));
                //row.add(resultSet.getDouble("operator_value") + resultSet.getString("units.ShortName") + " : " + resultSet.getString("units.name"));

                Double quantity = 0.0;
                ResultSet qtyRs = MySQL.execute("SELECT * FROM `stocks` WHERE `products_id`='" + resultSet.getInt("id") + "'");
                //ResultSet qtyRs = MySQL.execute("SELECT * FROM `purchase_details` WHERE `product_id`='" + resultSet.getInt("id") + "'");
                while (qtyRs.next()) {
                    double qty = qtyRs.getDouble("quantity");
                    if (qty > 0) {
                        quantity += qty;
                    }
                }
                row.add(quantity);

                model.addRow(row);
            }
        } catch (SQLException ex) {
            DatabaseLogger.logger.log(Level.SEVERE, "Products loading error: " + ex.getMessage(), ex.getMessage());
            loadTestData();
        }
    }

    private void importCSV() {
        CSVFileReader csvFileReader = new CSVFileReader();
        File csvFile = csvFileReader.selectCSV(this);
        if (csvFile != null) {
            try {
                List dataList = csvFileReader.getAll(csvFile);
                importProducts(dataList);
            } catch (IOException | CsvException ex) {
                CSVLogger.logger.log(Level.SEVERE, "Products importing error: " + ex.getMessage(), ex.getMessage());
            }
        }
    }

    private void importProducts(List<String[]> dataList) {
        String dateTime = new SQLDateFormatter().getStringDateTime(new Date());
        for (String[] dataRow : dataList) {
            String query = "INSERT IGNORE INTO "
                    + "`products` (`code`, `name`, `cost`, `price`, `category_id`, `brand_id`, `unit_id`, `unit_sale_id`, `unit_purchase_id`, `TaxNet`, "
                    + "`note`, `stock_alert`, `is_variant`, `is_imei`, `is_active`, `created_at`, `barcode_type_id`, `tax_method_id`, `product_type`) "
                    + "VALUES ('" + dataRow[0] + "', '" + dataRow[1] + "', '" + dataRow[2] + "', '" + dataRow[3] + "', '" + categoryMap.get(dataRow[4]) + "', '" + brandMap.get(dataRow[5]) + "', "
                    + "'" + unitMap.get(dataRow[6]) + "', '" + unitMap.get(dataRow[6]) + "', '" + unitMap.get(dataRow[6]) + "', '" + dataRow[7] + "', '" + dataRow[8] + "', "
                    + "'" + dataRow[9] + "', '0', '" + dataRow[10] + "', 1, "
                    + "'" + dateTime + "', '" + barcodeTypeMap.get(dataRow[11]) + "', '" + taxMethodMap.get(dataRow[12]) + "', 'product')";
            try {
                MySQL.execute(query);
                DatabaseLogger.logger.log(Level.FINE, "Product Imported: " + Arrays.toString(dataRow));
            } catch (SQLException ex) {
                DatabaseLogger.logger.log(Level.SEVERE, "Products Importing DB error: " + ex.getMessage(), ex.getMessage());
            }
        }
    }

    private void loadData() {
        loadTaxMethods();
        loadBrands();
        loadCategories();
        loadUnits();
        loadBarcodeTypes();
    }

    private HashMap<String, Integer> taxMethodMap;
    private HashMap<String, Integer> brandMap;
    private HashMap<String, Integer> categoryMap;
    private HashMap<String, Integer> barcodeTypeMap;
    private HashMap<String, Integer> unitMap;

    private void loadTaxMethods() {
        taxMethodMap = new HashMap<>();
        try {
            ResultSet resultSet = MySQL.execute("SELECT * FROM `tax_method` ORDER BY `method`");

            taxMethodMap.clear();
            while (resultSet.next()) {
                taxMethodMap.put(resultSet.getString("method"), resultSet.getInt("id"));
            }
        } catch (SQLException ex) {
            DatabaseLogger.logger.log(Level.SEVERE, "Tax Method loading error: " + ex.getMessage(), ex.getMessage());
        }
    }

    private void loadBrands() {
        brandMap = new HashMap<>();
        try {
            ResultSet resultSet = MySQL.execute("SELECT * FROM `brands` ORDER BY `name`");

            brandMap.clear();
            while (resultSet.next()) {
                brandMap.put(resultSet.getString("name"), resultSet.getInt("id"));
            }
        } catch (SQLException ex) {
            DatabaseLogger.logger.log(Level.SEVERE, "Brand loading error: " + ex.getMessage(), ex.getMessage());
        }
    }

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

    private void loadUnits() {
        unitMap = new HashMap<>();
        try {
            ResultSet resultSet = MySQL.execute("SELECT * FROM `units` ORDER BY `ShortName`");

            unitMap.clear();
            while (resultSet.next()) {
                unitMap.put(resultSet.getString("ShortName"), resultSet.getInt("id"));
            }
        } catch (SQLException ex) {
            DatabaseLogger.logger.log(Level.SEVERE, "Units loading error: " + ex.getMessage(), ex.getMessage());
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
        cmdSearch = new javax.swing.JButton();
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
        setPreferredSize(new java.awt.Dimension(700, 590));

        jPanel1.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 1, 0, new java.awt.Color(0, 0, 0)));

        jLabel1.setFont(new java.awt.Font("Times New Roman", 1, 24)); // NOI18N
        jLabel1.setText("Product List");

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

        searchField.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                searchFieldKeyReleased(evt);
            }
        });

        jButton6.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jButton6.setForeground(new java.awt.Color(51, 204, 255));
        jButton6.setText("Filter");
        jButton6.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 153, 204)));
        jButton6.setEnabled(false);
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
        jButton5.setEnabled(false);
        jButton5.setOpaque(true);
        jButton5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton5ActionPerformed(evt);
            }
        });

        jButton2.setBackground(new java.awt.Color(51, 102, 255));
        jButton2.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jButton2.setText("Import Products");
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
        jButton4.setEnabled(false);
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
                .addComponent(jButton6, javax.swing.GroupLayout.DEFAULT_SIZE, 36, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jButton5, javax.swing.GroupLayout.DEFAULT_SIZE, 40, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jButton4, javax.swing.GroupLayout.DEFAULT_SIZE, 30, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jButton2, javax.swing.GroupLayout.DEFAULT_SIZE, 98, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jButton3, javax.swing.GroupLayout.DEFAULT_SIZE, 44, Short.MAX_VALUE)
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
                .addComponent(cmdSearch, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(searchField, javax.swing.GroupLayout.PREFERRED_SIZE, 250, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(78, 78, 78)
                .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(cmdSearch, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(searchField, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(0, 0, Short.MAX_VALUE))
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
                "Select", "ID", "Image", "Name", "Code", "Brand", "Price", "Unit", "Quantity", "Action"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Boolean.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class
            };
            boolean[] canEdit = new boolean [] {
                true, false, false, false, false, false, false, false, false, true
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
            jTable1.getColumnModel().getColumn(1).setResizable(false);
            jTable1.getColumnModel().getColumn(2).setMinWidth(75);
            jTable1.getColumnModel().getColumn(2).setPreferredWidth(100);
            jTable1.getColumnModel().getColumn(3).setMinWidth(150);
            jTable1.getColumnModel().getColumn(3).setPreferredWidth(300);
            jTable1.getColumnModel().getColumn(3).setMaxWidth(300);
            jTable1.getColumnModel().getColumn(4).setResizable(false);
            jTable1.getColumnModel().getColumn(5).setResizable(false);
            jTable1.getColumnModel().getColumn(6).setResizable(false);
            jTable1.getColumnModel().getColumn(7).setResizable(false);
            jTable1.getColumnModel().getColumn(8).setResizable(false);
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

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        // Import
        loadData();
        importCSV();
        unitMap = null;
        barcodeTypeMap = null;
        categoryMap = null;
        taxMethodMap = null;
        brandMap = null;
        System.gc();
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        // Create
        Application.appService.openCreateProduct();
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

    private void searchFieldKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_searchFieldKeyReleased
        // Search
        loadProducts(searchField.getText());
    }//GEN-LAST:event_searchFieldKeyReleased


    // Variables declaration - do not modify//GEN-BEGIN:variables
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
    private javax.swing.JTable jTable1;
    private javax.swing.JTextField searchField;
    // End of variables declaration//GEN-END:variables
}
