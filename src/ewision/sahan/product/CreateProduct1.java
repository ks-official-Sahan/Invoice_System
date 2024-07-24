package ewision.sahan.product;

import ewision.sahan.application.main.DialogModal;
import ewision.sahan.loggers.DatabaseLogger;
import ewision.sahan.model.MySQL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.HashMap;
import java.util.Vector;
import java.util.logging.Level;
import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListModel;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author ksoff
 */
public class CreateProduct1 extends javax.swing.JPanel {

    private boolean tableVisible = false;
    private HashMap<Integer, String> variantMap = new HashMap<>();
    private HashMap<String, Integer> taxMethodMap = new HashMap<>();
    private HashMap<String, Integer> brandMap = new HashMap<>();
    private HashMap<String, Integer> categoryMap = new HashMap<>();
    private HashMap<String, Integer> barcodeTypeMap = new HashMap<>();
    private HashMap<String, Integer> unitMap = new HashMap<>();

    /**
     * Creates new form createProduct
     */
    public CreateProduct1() {
        initComponents();
        init();
    }

    /*
     * Intialization
     */
    private void init() {
        jPanel5.setVisible(false);
        variantTableScroll.setVisible(false);

        loadBarcodeTypes();
        loadBrands();
        loadCategories();
        loadTaxMethods();
        loadUnits();
    }

    private void loadTaxMethods() {
        DefaultComboBoxModel model = new DefaultComboBoxModel();

        try {
            ResultSet resultSet = MySQL.execute("SELECT * FROM `tax_method` ORDER BY `method`");

            Vector data = new Vector();
            data.add("Select");

            taxMethodMap.clear();
            while (resultSet.next()) {
                data.add(resultSet.getString("method"));
                taxMethodMap.put(resultSet.getString("method"), resultSet.getInt("id"));
            }

            model.addAll(data);
            model.setSelectedItem("Select");
            taxTypeBox.setModel(model);
        } catch (SQLException ex) {
            DatabaseLogger.logger.log(Level.SEVERE, "Tax Method loading error: " + ex.getMessage(), ex.getMessage());
        }
    }

    private void loadBrands() {
        DefaultComboBoxModel model = new DefaultComboBoxModel();

        try {
            ResultSet resultSet = MySQL.execute("SELECT * FROM `brands` ORDER BY `name`");

            Vector data = new Vector();
            data.add("Select");

            brandMap.clear();
            while (resultSet.next()) {
                data.add(resultSet.getString("name"));
                brandMap.put(resultSet.getString("name"), resultSet.getInt("id"));
            }

            model.addAll(data);
            model.setSelectedItem("Select");
            brandBox.setModel(model);
        } catch (SQLException ex) {
            DatabaseLogger.logger.log(Level.SEVERE, "Brand loading error: " + ex.getMessage(), ex.getMessage());
        }
    }

    private void loadCategories() {
        DefaultComboBoxModel model = new DefaultComboBoxModel();

        try {
            ResultSet resultSet = MySQL.execute("SELECT * FROM `categories` ORDER BY `name`");

            Vector data = new Vector();
            data.add("Select");

            categoryMap.clear();
            while (resultSet.next()) {
                data.add(resultSet.getString("name"));
                categoryMap.put(resultSet.getString("name"), resultSet.getInt("id"));
            }

            model.addAll(data);
            model.setSelectedItem("Select");
            categoryBox.setModel(model);
        } catch (SQLException ex) {
            DatabaseLogger.logger.log(Level.SEVERE, "Category loading error: " + ex.getMessage(), ex.getMessage());
        }
    }

    private void loadBarcodeTypes() {
        DefaultComboBoxModel model = new DefaultComboBoxModel();

        try {
            ResultSet resultSet = MySQL.execute("SELECT * FROM `barcode_type` ORDER BY `barcode_type`");

            Vector data = new Vector();
            data.add("Select");

            barcodeTypeMap.clear();
            while (resultSet.next()) {
                data.add(resultSet.getString("barcode_type"));
                barcodeTypeMap.put(resultSet.getString("barcode_type"), resultSet.getInt("id"));
            }

            model.addAll(data);
            model.setSelectedItem("Select");
            barcodeTypeBox.setModel(model);
        } catch (SQLException ex) {
            DatabaseLogger.logger.log(Level.SEVERE, "Barcode Type loading error: " + ex.getMessage(), ex.getMessage());
        }
    }

    private void loadUnits() {
        try {
            ResultSet resultSet = MySQL.execute("SELECT * FROM `units` ORDER BY `ShortName`");

            Vector<String> data = new Vector();
            data.add("Select");

            unitMap.clear();
            while (resultSet.next()) {
                data.add(resultSet.getString("ShortName"));
                unitMap.put(resultSet.getString("ShortName"), resultSet.getInt("id"));
            }

            JComboBox[] comboBoxes = {purchaseUnitBox, productUnitBox, saleUnitBox};
            for (JComboBox comboBox : comboBoxes) {
                DefaultComboBoxModel model = new DefaultComboBoxModel();

                model.addAll(data);
                model.setSelectedItem("Select");

                comboBox.setModel(model);
            }

        } catch (SQLException ex) {
            DatabaseLogger.logger.log(Level.SEVERE, "Units loading error: " + ex.getMessage(), ex.getMessage());
        }
    }

    /*
     * Date Formatting
     */
    private String formatDate(Object dateTime, String pattern) {
        SimpleDateFormat sdf = new SimpleDateFormat(pattern);
        return sdf.format(dateTime);
    }

    /*
     * Variant Operations
     */
    private void toggleVariantPanel() {
        if (isVariantBox.isSelected()) {
            jPanel5.setVisible(true);
        } else {
            jPanel5.setVisible(false);
        }
    }

    private void toggleVariantTable() {
        if (!tableVisible) {
            variantTableScroll.setVisible(true);
            tableVisible = true;
        }
    }

    private void addVariant() {
        if (!variantField.getText().equals("")) {
            for (String value : variantMap.values()) {
                if (value.equals(variantField.getText())) {
                    return;
                }
            }

            Vector v = new Vector();
            v.add(variantField.getText());

            DefaultTableModel model = (DefaultTableModel) variantTable.getModel();
            model.addRow(v);
            variantMap.put(model.getRowCount(), variantField.getText());
            variantTable.scrollRectToVisible(variantTable.getCellRect(variantTable.getRowCount() - 1, 0, true));
            variantField.setText("");
        }
    }

    private void reset() {
        JTextField[] fields = {barcodeField, costField, nameField, priceField};
        for (JTextField field : fields) {
            field.setText("");
        }
        //nameField.setText("");
        //barcodeField.setText("");
        //costField.setText("");
        //priceField.setText("");

        descriptionText.setText("");

        JComboBox[] comboBoxes = {barcodeTypeBox, brandBox, categoryBox, productUnitBox, purchaseUnitBox, saleUnitBox, taxTypeBox};
        for (JComboBox comboBox : comboBoxes) {
            comboBox.setSelectedItem("Select");
        }
        //barcodeTypeBox.setSelectedItem("Select");
        //brandBox.setSelectedItem("Select");
        //categoryBox.setSelectedItem("Select");
        //productUnitBox.setSelectedItem("Select");
        //purchaseUnitBox.setSelectedItem("Select");
        //saleUnitBox.setSelectedItem("Select");
        //taxTypeBox.setSelectedItem("Select");

        stockAlertField.setText("0");
        taxField.setText("0.00");

        JCheckBox[] checkBoxes = {isIMEIBox, isVariantBox};
        for (JCheckBox checkBox : checkBoxes) {
            checkBox.setSelected(false);
        }
        //isIMEIBox.setSelected(false);
        //isVariantBox.setSelected(false);

        resetVariantTable();
    }

    private void resetVariantTable() {
        DefaultTableModel model = (DefaultTableModel) variantTable.getModel();
        model.setRowCount(0);
        variantTable.setModel(model);
        variantMap.clear();
    }

    private void registerProduct() {
        String name = nameField.getText();
        String barcode = barcodeField.getText();
        String category = String.valueOf(categoryBox.getSelectedItem());
        String brand = String.valueOf(brandBox.getSelectedItem());
        String barcodeType = String.valueOf(barcodeTypeBox.getSelectedItem());
        String productUnit = String.valueOf(productUnitBox.getSelectedItem());
        String purchaseUnit = String.valueOf(purchaseUnitBox.getSelectedItem());
        String saleUnit = String.valueOf(saleUnitBox.getSelectedItem());
        String taxType = String.valueOf(taxTypeBox.getSelectedItem());
        String cost = costField.getText();
        String price = priceField.getText();
        String stockAlert = stockAlertField.getText();
        String tax = taxField.getText();
        String description = descriptionText.getText();
        boolean isIMEI = isIMEIBox.isSelected();
        boolean isVariant = isVariantBox.isSelected();

        if (name.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter Product Name", "Invalid Name", JOptionPane.WARNING_MESSAGE);
            nameField.requestFocus();
        } else if (barcode.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter Product Code", "Invalid Product Code", JOptionPane.WARNING_MESSAGE);
            barcodeField.requestFocus();
        } else if (category.equalsIgnoreCase("select")) {
            JOptionPane.showMessageDialog(this, "Please enter Product Category", "Invalid Category", JOptionPane.WARNING_MESSAGE);
            categoryBox.requestFocus();
        } else if (brand.equalsIgnoreCase("select")) {
            JOptionPane.showMessageDialog(this, "Please enter Product Brand", "Invalid Brand", JOptionPane.WARNING_MESSAGE);
            brandBox.requestFocus();
        } else if (barcodeType.equalsIgnoreCase("select")) {
            JOptionPane.showMessageDialog(this, "Please enter Product Barcode Symbology", "Invalid Barcode Symbology", JOptionPane.WARNING_MESSAGE);
            barcodeTypeBox.requestFocus();
        } else if (cost.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter Product Cost", "Invalid Product Cost", JOptionPane.WARNING_MESSAGE);
            costField.requestFocus();
        } else if (price.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter Product Price", "Invalid Product Price", JOptionPane.WARNING_MESSAGE);
            priceField.requestFocus();
        } else if (productUnit.equalsIgnoreCase("select")) {
            JOptionPane.showMessageDialog(this, "Please enter Product Unit", "Invalid Product Unit", JOptionPane.WARNING_MESSAGE);
            productUnitBox.requestFocus();
        } else if (saleUnit.equalsIgnoreCase("select")) {
            JOptionPane.showMessageDialog(this, "Please enter Product Sale Unit", "Invalid Product Sale Unit", JOptionPane.WARNING_MESSAGE);
            saleUnitBox.requestFocus();
        } else if (purchaseUnit.equalsIgnoreCase("select")) {
            JOptionPane.showMessageDialog(this, "Please enter Product Purchase Unit", "Invalid Product Purchase Unit", JOptionPane.WARNING_MESSAGE);
            purchaseUnitBox.requestFocus();
        } else if (stockAlert.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter Product Stock Alert", "Invalid Product Stock Alert", JOptionPane.WARNING_MESSAGE);
            stockAlertField.requestFocus();
            //} else if (tax.isEmpty()) {
            //JOptionPane.showMessageDialog(this, "Please enter Product Tax", "Invalid Tax", JOptionPane.WARNING_MESSAGE);
            //taxField.requestFocus();
        } else if (!tax.isEmpty() && !tax.equals("0.00") && taxType.equalsIgnoreCase("Select")) {
            JOptionPane.showMessageDialog(this, "Please select Product Tax Type", "Invalid Tax Type", JOptionPane.WARNING_MESSAGE);
            taxTypeBox.requestFocus();
        } else {
            if (tax.isEmpty()) {
                tax = "0.00";
                taxField.setText("0.00");
            }

            String dateTime = formatDate(System.currentTimeMillis(), "yyyy-mm-dd HH:mm:ss");

            String query = "INSERT INTO "
                    + "`products` (`code`, `name`, `cost`, `price`, `category_id`, `brand_id`, `unit_id`, `unit_sale_id`, `unit_purchase_id`, `TaxNet`, "
                    + "`note`, `stock_alert`, `is_variant`, `is_imei`, `is_active`, `created_at`, `barcode_type_id`, `tax_method_id`, `product_type`) "
                    + "VALUES ('" + barcode + "', '" + name + "', '" + cost + "', '" + price + "', '" + categoryMap.get(category) + "', '" + brandMap.get(brand) + "', "
                    + "'" + unitMap.get(productUnit) + "', '" + unitMap.get(saleUnit) + "', '" + unitMap.get(purchaseUnit) + "', '" + tax + "', '" + description + "', "
                    + "'" + stockAlert + "', '" + (isVariant ? "1" : "0") + "', '" + (isIMEI ? "1" : "0") + "', 1, "
                    + "'" + dateTime + "', '" + barcodeTypeMap.get(barcodeType) + "', '" + taxMethodMap.get(taxType) + "', 'product')";

//            if (!category.equalsIgnoreCase("select")) {
//                query = "INSERT INTO `products` (`code`, `cost`, `price`, `categories_id`, `name`, `barcode_type_id`, `description`) VALUES ('" + barcode + "', '" + cost + "', '" + price + "', '" + categoryMap.get(category) + "', '" + name + "', '" + barcodeTypeMap.get(barcodeType) + "', '" + description + "')";
//            }
            try {
                MySQL.execute(query);

                if (isVariant) {
                    String productId = getLastInsertId();
                    if (!productId.equals("")) {
                        registerProductVariant(productId);
                    }
                }

                reset();
            } catch (SQLException ex) {
                DatabaseLogger.logger.log(Level.SEVERE, "Product registration error: " + ex.getMessage(), ex.getMessage());
                JOptionPane.showMessageDialog(this, ex.getMessage(), "Something went wrong in Product Registration!!", JOptionPane.WARNING_MESSAGE);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage(), "Something went wrong!!", JOptionPane.WARNING_MESSAGE);
            }
        }
    }

    private String getLastInsertId() {
        try {
            ResultSet rs = MySQL.execute("SELECT LAST_INSERT_ID()");
            if (rs.next()) {
                String productId = String.valueOf(rs.getInt(0));
                return productId;
            }
        } catch (SQLException ex) {
            DatabaseLogger.logger.log(Level.SEVERE, "Unable to get last insert Product ID: " + ex.getMessage(), ex.getMessage());
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Something went wrong!! \nUnable to get last insert Product ID", JOptionPane.WARNING_MESSAGE);
        }
        return "";
    }

    private void registerProductVariant(String productId) {
        String dateTime = formatDate(System.currentTimeMillis(), "yyyy-mm-dd HH:mm:ss");
        for (String variant : variantMap.values()) {
            String variantQuery = "INSERT INTO `product_variants` (`product_id`, `name`, `qty`, `created_at`) VALUES ('" + productId + "', '" + variant + "', 0.00, '" + dateTime + "')";
            try {
                MySQL.execute(variantQuery);
            } catch (SQLException ex) {
                DatabaseLogger.logger.log(Level.SEVERE, "Product registration error: " + ex.getMessage(), ex.getMessage());
                JOptionPane.showMessageDialog(this, ex.getMessage(), "Something went wrong in variant registration!!", JOptionPane.WARNING_MESSAGE);
            }
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
        jScrollPane2 = new javax.swing.JScrollPane();
        jPanel4 = new javax.swing.JPanel();
        jPanel7 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        nameField = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        brandBox = new javax.swing.JComboBox<>();
        jLabel10 = new javax.swing.JLabel();
        barcodeField = new javax.swing.JTextField();
        categoryBox = new javax.swing.JComboBox<>();
        jLabel11 = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        barcodeTypeBox = new javax.swing.JComboBox<>();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        productUnitBox = new javax.swing.JComboBox<>();
        jLabel12 = new javax.swing.JLabel();
        saleUnitBox = new javax.swing.JComboBox<>();
        jLabel13 = new javax.swing.JLabel();
        purchaseUnitBox = new javax.swing.JComboBox<>();
        jLabel16 = new javax.swing.JLabel();
        jLabel17 = new javax.swing.JLabel();
        jButton1 = new javax.swing.JButton();
        jLabel18 = new javax.swing.JLabel();
        taxTypeBox = new javax.swing.JComboBox<>();
        jLabel19 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        descriptionText = new javax.swing.JTextArea();
        isIMEIBox = new javax.swing.JCheckBox();
        isVariantBox = new javax.swing.JCheckBox();
        jPanel5 = new javax.swing.JPanel();
        variantTableScroll = new javax.swing.JScrollPane();
        variantTable = new javax.swing.JTable();
        jPanel6 = new javax.swing.JPanel();
        variantField = new javax.swing.JTextField();
        jButton2 = new javax.swing.JButton();
        jPanel8 = new javax.swing.JPanel();
        jButton4 = new javax.swing.JButton();
        costField = new javax.swing.JFormattedTextField();
        priceField = new javax.swing.JFormattedTextField();
        stockAlertField = new javax.swing.JFormattedTextField();
        taxField = new javax.swing.JFormattedTextField();
        jButton3 = new javax.swing.JButton();
        jButton5 = new javax.swing.JButton();
        jButton6 = new javax.swing.JButton();

        setBorder(javax.swing.BorderFactory.createEmptyBorder(10, 10, 10, 10));

        jPanel1.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 1, 0, new java.awt.Color(0, 0, 0)));

        jLabel1.setFont(new java.awt.Font("Times New Roman", 1, 24)); // NOI18N
        jLabel1.setText("Create Product");

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

        jScrollPane2.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

        jPanel7.setPreferredSize(new java.awt.Dimension(1318, 700));

        jPanel2.setPreferredSize(new java.awt.Dimension(400, 899));

        jLabel2.setFont(new java.awt.Font("SansSerif", 0, 13)); // NOI18N
        jLabel2.setText("Name *");

        jLabel6.setFont(new java.awt.Font("SansSerif", 0, 13)); // NOI18N
        jLabel6.setText("Brand *");

        jLabel9.setFont(new java.awt.Font("Segoe UI", 0, 10)); // NOI18N
        jLabel9.setText("* Required");

        brandBox.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Select Brand" }));

        jLabel10.setFont(new java.awt.Font("SansSerif", 0, 13)); // NOI18N
        jLabel10.setText("Product Code *");

        categoryBox.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Select Category" }));

        jLabel11.setFont(new java.awt.Font("SansSerif", 0, 13)); // NOI18N
        jLabel11.setText("Category");

        jLabel14.setFont(new java.awt.Font("Segoe UI", 0, 10)); // NOI18N
        jLabel14.setText("Scan your barcode and select correct symbology below");

        jLabel3.setFont(new java.awt.Font("SansSerif", 0, 13)); // NOI18N
        jLabel3.setText("Barcode Symbology *");

        barcodeTypeBox.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Code 128" }));

        jLabel4.setFont(new java.awt.Font("SansSerif", 0, 13)); // NOI18N
        jLabel4.setText("Product Cost *");

        jLabel5.setFont(new java.awt.Font("SansSerif", 0, 13)); // NOI18N
        jLabel5.setText("Product Price *");

        jLabel8.setFont(new java.awt.Font("SansSerif", 0, 13)); // NOI18N
        jLabel8.setText("Product Unit *");

        productUnitBox.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "1 KG" }));
        productUnitBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                productUnitBoxActionPerformed(evt);
            }
        });

        jLabel12.setFont(new java.awt.Font("SansSerif", 0, 13)); // NOI18N
        jLabel12.setText("Sale Unit *");

        saleUnitBox.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "1 KG" }));

        jLabel13.setFont(new java.awt.Font("SansSerif", 0, 13)); // NOI18N
        jLabel13.setText(" Purchase Unit *");

        purchaseUnitBox.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "1 KG" }));

        jLabel16.setFont(new java.awt.Font("SansSerif", 0, 13)); // NOI18N
        jLabel16.setText("Stock Alert");

        jLabel17.setFont(new java.awt.Font("SansSerif", 0, 13)); // NOI18N
        jLabel17.setText("Order Tax");

        jButton1.setText("%");
        jButton1.setEnabled(false);
        jButton1.setMaximumSize(new java.awt.Dimension(34, 34));
        jButton1.setPreferredSize(new java.awt.Dimension(34, 34));

        jLabel18.setFont(new java.awt.Font("SansSerif", 0, 13)); // NOI18N
        jLabel18.setText("Tax Type *");

        taxTypeBox.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Exlusive" }));

        jLabel19.setFont(new java.awt.Font("SansSerif", 0, 13)); // NOI18N
        jLabel19.setText("Description");

        descriptionText.setColumns(20);
        descriptionText.setRows(2);
        jScrollPane1.setViewportView(descriptionText);

        isIMEIBox.setFont(new java.awt.Font("SansSerif", 0, 13)); // NOI18N
        isIMEIBox.setText("Product has IMEI / Serial Number");
        isIMEIBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                isIMEIBoxActionPerformed(evt);
            }
        });

        isVariantBox.setFont(new java.awt.Font("SansSerif", 0, 13)); // NOI18N
        isVariantBox.setText("This Product Has Multiple Variants");
        isVariantBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                isVariantBoxActionPerformed(evt);
            }
        });

        variantTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Varient"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        variantTableScroll.setViewportView(variantTable);

        variantField.setMinimumSize(new java.awt.Dimension(300, 22));
        variantField.setPreferredSize(new java.awt.Dimension(300, 22));

        jButton2.setText("Add");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addComponent(variantField, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 160, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(variantField, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(variantTableScroll)
            .addComponent(jPanel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addComponent(jPanel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(variantTableScroll, javax.swing.GroupLayout.DEFAULT_SIZE, 68, Short.MAX_VALUE)
                .addContainerGap())
        );

        jButton4.setText("Submit");
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel8Layout = new javax.swing.GroupLayout(jPanel8);
        jPanel8.setLayout(jPanel8Layout);
        jPanel8Layout.setHorizontalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jButton4, javax.swing.GroupLayout.PREFERRED_SIZE, 135, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel8Layout.setVerticalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jButton4, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(7, Short.MAX_VALUE))
        );

        costField.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("#0.00"))));

        priceField.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("#0.00"))));

        stockAlertField.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("#0"))));
        stockAlertField.setText("0");

        taxField.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("#0.00"))));
        taxField.setText("0");

        jButton3.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jButton3.setText("+");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        jButton5.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jButton5.setText("+");
        jButton5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton5ActionPerformed(evt);
            }
        });

        jButton6.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jButton6.setText("+");
        jButton6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton6ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jPanel8, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel5, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(taxTypeBox, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel18, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel16, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(saleUnitBox, javax.swing.GroupLayout.Alignment.LEADING, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel12, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel5, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(barcodeTypeBox, javax.swing.GroupLayout.Alignment.LEADING, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel9, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel3, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel11, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel19, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(nameField, javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(priceField)
                            .addComponent(stockAlertField)
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel2Layout.createSequentialGroup()
                                .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 255, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(0, 194, Short.MAX_VALUE))
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addComponent(categoryBox, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jButton5, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                                .addComponent(taxField)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(jLabel17, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(purchaseUnitBox, javax.swing.GroupLayout.Alignment.TRAILING, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel13, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel8, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel6, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel14, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 442, Short.MAX_VALUE)
                            .addComponent(jLabel10, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(barcodeField, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addComponent(brandBox, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(jLabel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(costField, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addComponent(productUnitBox, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jButton6, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE))))
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel2Layout.createSequentialGroup()
                        .addGap(6, 6, 6)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(isIMEIBox, javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(isVariantBox, javax.swing.GroupLayout.Alignment.LEADING))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addGap(12, 12, 12))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel10, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(barcodeField, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(nameField, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, 14, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel14))
                .addGap(18, 18, 18)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel11, javax.swing.GroupLayout.DEFAULT_SIZE, 25, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(brandBox, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(categoryBox, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jButton5, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(18, 18, 18)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(barcodeTypeBox, javax.swing.GroupLayout.DEFAULT_SIZE, 34, Short.MAX_VALUE)
                    .addComponent(costField))
                .addGap(20, 20, 20)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel8, javax.swing.GroupLayout.DEFAULT_SIZE, 25, Short.MAX_VALUE)
                    .addComponent(jLabel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(productUnitBox, javax.swing.GroupLayout.DEFAULT_SIZE, 34, Short.MAX_VALUE)
                        .addComponent(jButton6, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(priceField))
                .addGap(18, 18, 18)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel13, javax.swing.GroupLayout.DEFAULT_SIZE, 25, Short.MAX_VALUE)
                    .addComponent(jLabel12, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(saleUnitBox, javax.swing.GroupLayout.DEFAULT_SIZE, 34, Short.MAX_VALUE)
                    .addComponent(purchaseUnitBox))
                .addGap(18, 18, 18)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel17, javax.swing.GroupLayout.DEFAULT_SIZE, 25, Short.MAX_VALUE)
                    .addComponent(jLabel16, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(taxField)
                    .addComponent(jButton1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(stockAlertField))
                .addGap(18, 18, 18)
                .addComponent(jLabel18, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(taxTypeBox, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jLabel19, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 51, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(isIMEIBox)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(isVariantBox)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, 927, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, 952, Short.MAX_VALUE)
                .addContainerGap())
        );

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel7, javax.swing.GroupLayout.DEFAULT_SIZE, 939, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel7, javax.swing.GroupLayout.DEFAULT_SIZE, 964, Short.MAX_VALUE)
                .addContainerGap())
        );

        jScrollPane2.setViewportView(jPanel4);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jScrollPane2)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane2))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void isVariantBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_isVariantBoxActionPerformed
        // Show Add Product Variant Panel
        toggleVariantPanel();
    }//GEN-LAST:event_isVariantBoxActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        // Show Add Product Variant Table and Add Data
        toggleVariantTable();
        addVariant();
    }//GEN-LAST:event_jButton2ActionPerformed

    private void isIMEIBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_isIMEIBoxActionPerformed
        // IMEI or Serial

    }//GEN-LAST:event_isIMEIBoxActionPerformed

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
        // Register Product
        registerProduct();
    }//GEN-LAST:event_jButton4ActionPerformed

    private void productUnitBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_productUnitBoxActionPerformed
        // Set Default Unit
        String selected = String.valueOf(productUnitBox.getSelectedItem());
        if (!selected.equalsIgnoreCase("Select")) {
            JComboBox[] comboBoxes = {purchaseUnitBox, saleUnitBox};
            for (JComboBox comboBox : comboBoxes) {
                comboBox.setSelectedItem(selected);
            }
        }
    }//GEN-LAST:event_productUnitBoxActionPerformed

    private void jButton5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton5ActionPerformed
        // Add Category
        DialogModal modal = new DialogModal(this);
        modal.openCategoryReg();
        modal.setVisible(true);
    }//GEN-LAST:event_jButton5ActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        // Add Brand
        DialogModal modal = new DialogModal(this);
        modal.openBrandReg();
        modal.setVisible(true);        
    }//GEN-LAST:event_jButton3ActionPerformed

    private void jButton6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton6ActionPerformed
        // Add Unit        
        DialogModal modal = new DialogModal(this);
        modal.openUnitReg();
        modal.setVisible(true);
    }//GEN-LAST:event_jButton6ActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextField barcodeField;
    private javax.swing.JComboBox<String> barcodeTypeBox;
    private javax.swing.JComboBox<String> brandBox;
    private javax.swing.JComboBox<String> categoryBox;
    private javax.swing.JFormattedTextField costField;
    private javax.swing.JTextArea descriptionText;
    private javax.swing.JCheckBox isIMEIBox;
    private javax.swing.JCheckBox isVariantBox;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JButton jButton5;
    private javax.swing.JButton jButton6;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTextField nameField;
    private javax.swing.JFormattedTextField priceField;
    private javax.swing.JComboBox<String> productUnitBox;
    private javax.swing.JComboBox<String> purchaseUnitBox;
    private javax.swing.JComboBox<String> saleUnitBox;
    private javax.swing.JFormattedTextField stockAlertField;
    private javax.swing.JFormattedTextField taxField;
    private javax.swing.JComboBox<String> taxTypeBox;
    private javax.swing.JTextField variantField;
    private javax.swing.JTable variantTable;
    private javax.swing.JScrollPane variantTableScroll;
    // End of variables declaration//GEN-END:variables
}
